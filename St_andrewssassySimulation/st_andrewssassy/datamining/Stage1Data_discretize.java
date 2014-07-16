package St_andrewssassySimulation.st_andrewssassy.datamining;

import java.io.File;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Stage1Data_discretize extends PreprocessBase {
	public Stage1Data_discretize(String filefolder) {
		super(filefolder);
	}

	/****************************************************************
	 * 
	 * Methods for datasets2
	 * 
	 * */
	public void combineRelationships() {

		Instances dsn = new Instances(instanceList.get(0));
		Instances friends = new Instances(instanceList.get(1));
		
		/**
		 *  Create a new instance to store data
		 */
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new Attribute("detectorId"));
		atts.add(new Attribute("detectedId"));
		atts.add(new Attribute("detected_frequency"));
		atts.add(new Attribute("detected_daytime"));
		atts.add(new Attribute("detected_evening"));
		atts.add(new Attribute("detected_latenight"));
		atts.add(new Attribute("duration_short"));
		atts.add(new Attribute("duration_long"));
		atts.add(new Attribute("distance_close"));
		atts.add(new Attribute("distance_far"));
		atts.add(new Attribute("friendship")); // use initial friendship only
		Instances relationship = new Instances("relationships", atts, 0);
		
		double minId = dsn.instance(0).value(0);
		double maxId = dsn.instance(0).value(0);
		for(Instance inst : dsn){
			minId = (inst.value(0) < minId)? inst.value(0) : minId;
			minId = (inst.value(1) < minId)? inst.value(1) : minId;
			maxId = (inst.value(0) > maxId)? inst.value(0) : maxId;
			maxId = (inst.value(1) > maxId)? inst.value(1) : maxId;
		}
		
		// System.out.println("Min Value is " + minId + ". Max Value is " + maxId);
		
		// Fill data into new instances
		double[] vals;
		for (int i = (int) minId; i < maxId; i++) {
			for (int j = (int) minId; j < maxId; j++) {
				vals = new double[relationship.numAttributes()];
				vals[0] = i;
				// Source equals to destination
				if(j == i) continue;
				vals[1] = j;					
				
				// detect time slot
				double daytime_count = 0, evening_count = 0, latenight_count = 0;
				// total contact times
				double detect_count = 0;
				for(Instance ins : dsn){
					if(ins.value(0) == i && ins.value(1) == j){
						// Get detect count for each time slot
						int timeslot = (int) (ins.value(4) % 86400);
						if (timeslot <= 60 * 60 * 8) {
							latenight_count++;
						} else if (timeslot > 60 * 60 * 8 && timeslot <= 60 * 60 * 8 * 2) {
							daytime_count++;
						} else if (timeslot > 60 * 60 * 8 * 2) {
							evening_count++;
						}
						
						detect_count = daytime_count + evening_count + latenight_count;
						
						// detect duration
						if(ins.value(7) == 0)
							vals[6]++;
						else
							vals[7]++;
						
						// detect distance
						if(ins.value(5) == 0)
							vals[8]++;
						else
							vals[9]++;
							
					}
				}
				
				vals[2] = detect_count;
				vals[3] = daytime_count;
				vals[4] = evening_count;
				vals[5] = latenight_count;
								
				// friendship relationship
				vals[10] = 0;
				for(Instance friendins : friends){
					if(friendins.value(0) == i && friendins.value(1) == j){
						vals[10] = 1;
						break;
					}
				}				
								
				DenseInstance newIns = new DenseInstance(1.0, vals);				
				relationship.add(newIns);
			}
		}
		
		// Option for converting numeric to nominal
		NumericToNominal convert = new NumericToNominal();
		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "11"; // range of variables to make nominal		

		try {
			// Convert numeric attribute to nominal attribute
			convert.setOptions(options);
			convert.setInputFormat(relationship);
			relationship = Filter.useFilter(relationship, convert);			
			
			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(relationship);
			saver.setFile(new File("./st_andrewssassy_Stage2/beforeDiscretize.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// data bytes discretizer options
		Discretize discrBytes = new Discretize();
		String[] discreteptions = new String[7];
		discreteptions[0] = "-F"; // Use equal-frequency instead of equal-width discretization.
		discreteptions[1] = "-B"; // Specifies the (maximum) number of bins to divide numeric attributes into
		discreteptions[2] = "2"; // number of bins
		discreteptions[3] = "-M"; // Specifies the desired weight of instances per bin for equal-frequency binning.
		discreteptions[4] = "-1"; // default
		discreteptions[5] = "-R"; // Attribute index
		discreteptions[6] = "3-10"; // the second attribute
		
		try {
			// Discrete data size and convert to ordinal attribute
			discrBytes.setOptions(discreteptions);
			discrBytes.setInputFormat(relationship);
			relationship = Filter.useFilter(relationship, discrBytes);
			
			for(int i=1; i<10; i++){
				Attribute att = relationship.attribute(i);
				for (int n = 0; n < att.numValues(); n++) {
					relationship.renameAttributeValue(att, att.value(n), "" + n);
				}
			}
			
			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(relationship);
			saver.setFile(new File("./st_andrewssassy_Stage2/afterDiscretize.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(relationship.instance(0).value(1));

	}
}
