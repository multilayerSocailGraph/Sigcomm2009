package st_andrewssassy.datamining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.AddExpression;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class RawDataProcess extends PreprocessBase {

	public RawDataProcess(String filefolder) {
		super(filefolder);
	}

	/**
	 * 
	 * Process activity data set
	 * 
	 * @param index
	 *            (instance index) Add duration attributes and discritizise it.
	 *            Save results to stage_1/activity files.
	 */
	public void dsn_processs() {
		// instance to work on
		Instances dsn = new Instances(instanceList.get(0));
		
		/**
		 * Create a new instance to store data
		 */
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new Attribute("device_having_encounter"));
		atts.add(new Attribute("device_seen"));
		atts.add(new Attribute("rawtime_start"));
		atts.add(new Attribute("rawtime_end"));
		atts.add(new Attribute("timeuploaded"));
		atts.add(new Attribute("rssivalue"));
		atts.add(new Attribute("errorval"));
		Instances newDSN = new Instances("traceData", atts, 0);

		// Fill data into new instances
		for (int i = 0; i < dsn.numInstances(); i++) {
			double[] vals = new double[dsn.numAttributes()];
			for (int j = 0; j < dsn.numAttributes() - 1; j++) {
				vals[j] = dsn.get(i).value(j);
			}
			vals[dsn.numAttributes() - 1] = getErrorVal(dsn.get(i));
			
			DenseInstance newIns = new DenseInstance(1.0, vals);
			newDSN.add(newIns);
		}
		
		// Filter to add duration attribute
		AddExpression addDurationfilter = new AddExpression();		
		addDurationfilter.setExpression("a4-a3");
		addDurationfilter.setName("duration");
		
		// data bytes discretizer options
		Discretize discrDuration = new Discretize();
		String[] discreteptions = new String[7];
		discreteptions[0] = "-F"; // Use equal-frequency instead of equal-width discretization.
		discreteptions[1] = "-B"; // Specifies the (maximum) number of bins to divide numeric attributes into
		discreteptions[2] = "2"; // number of bins
		discreteptions[3] = "-M"; // Specifies the desired weight of instances per bin for equal-frequency binning.
		discreteptions[4] = "-1"; // default
		discreteptions[5] = "-R"; // Attribute index
		discreteptions[6] = "6,8"; // the attribute index

		try {
			addDurationfilter.setInputFormat(newDSN);
			newDSN = Filter.useFilter(newDSN, addDurationfilter);
			
			// Discrete data size and convert to ordinal attribute
			discrDuration.setOptions(discreteptions);
			discrDuration.setInputFormat(newDSN);
			newDSN = Filter.useFilter(newDSN, discrDuration);
			
			Attribute att = newDSN.attribute(5);
			for (int n = 0; n < att.numValues(); n++) {
				newDSN.renameAttributeValue(att, att.value(n), "" + n);
			}
			
			att = newDSN.attribute(7);
			for (int n = 0; n < att.numValues(); n++) {
				newDSN.renameAttributeValue(att, att.value(n), "" + n);
			}
			
			// Save modified data
			ArffSaver saver = new ArffSaver();
			saver.setInstances(newDSN);
			saver.setFile(new File("./st_andrewssassy_Stage1/dsn.arff"));
			saver.writeBatch();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double getErrorVal(Instance inst) {
		boolean hasDays = false;
		String str = inst.stringValue(inst.numAttributes()-1);
		str = str.trim();
		// Check if have days or day
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == 'd') {
				hasDays = true;
				break;
			}
		}
		int i = 0;
		int days = 0, hours = 0, minutes = 0, seconds = 0;
		String strDays = "", strHours = "", strMinutes = "", strSeconds = "";

		if (hasDays) {
			// Count days
			for (; i < str.length(); i++) {
				if (str.charAt(i) != ' ') {
					strDays += str.charAt(i);
				} else {
					try{
						days = Integer.parseInt(strDays);
					}catch(Exception e){
						System.out.println(e.getMessage());
						System.out.println(inst.toString());
					}
					
					break;
				}
			}
		}

		// Count hours
		if (days != 0) {
			if (days == 1 || days == -1) {
				i = i + 5;
			} else {
				i = i + 6;
			}
		}

		for (; i < str.length(); i++) {
			if (str.charAt(i) != ':') {
				strHours += str.charAt(i);
			} else {
				try{
					hours = Integer.parseInt(strHours);
				}catch(Exception e){
					System.out.println(e.getMessage());
					System.out.println(inst.toString());
				}
				break;
			}
		}

		// Count minutes
		for (i++; i < str.length(); i++) {
			if (str.charAt(i) != ':') {
				strMinutes += str.charAt(i);
			} else {
				minutes = Integer.parseInt(strMinutes);
				break;
			}
		}

		// Count seconds
		for (i++; i < str.length(); i++) {
			if (i < str.length() - 1) {
				strSeconds += str.charAt(i);
			} else {
				strSeconds += str.charAt(i);
				seconds = Integer.parseInt(strSeconds);
			}
		}
		seconds = ((days * 24 + hours) * 60 + minutes) * 60 + seconds;

		// System.out.println(seconds);

		return seconds;
	}

	/*********************
	 * 
	 * Merge friends1 and friends2 file Save data sets to data/friends file
	 * 
	 ***************/
	public void srsn_processs() {
		// instance to work on
		Instances srsn = new Instances(instanceList.get(1));

		try {
			// Save modified data
			ArffSaver saver = new ArffSaver();
			saver.setInstances(srsn);
			saver.setFile(new File("./st_andrewssassy_Stage1/srsn.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
