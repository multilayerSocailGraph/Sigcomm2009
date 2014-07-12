package sigcom.datamining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
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
	public void activity_processs() {
		// instance to work on
		Instances activities = new Instances(instanceList.get(0));
		// Filter to add duration attribute
		AddExpression filter1 = new AddExpression();		
		filter1.setExpression("a2-a1");
		filter1.setName("duration");
		
		// Filter to add discretized duration nominal attribute
		Discretize filter2 = new Discretize();		
		filter2.setBins(4);
		filter2.setAttributeIndices("last");
		
		try {			
			filter1.setInputFormat(activities);
			activities = Filter.useFilter(activities, filter1);
			filter2.setInputFormat(activities);
			activities = Filter.useFilter(activities, filter2);

			// Rename attribute
			Attribute att = activities.attribute(3);
			for (int n = 0; n < att.numValues(); n++) {
				activities.renameAttributeValue(att, att.value(n), "" + n);
			}

			// Save modified data
			ArffSaver saver = new ArffSaver();
			saver.setInstances(activities);
			saver.setFile(new File("./stage_1/activity.arff"));
			saver.writeBatch();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*********************
	 * 
	 * Merge friends1 and friends2 file Save data sets to data/friends file
	 * 
	 ***************/
	public void mergeFriends() {
		// instances to work on
		Instances friends1 = new Instances(instanceList.get(1));
		Instances friends2 = new Instances(instanceList.get(2));
		
		// Filters to use
		AddExpression addAtt = new AddExpression();
		addAtt.setExpression("0");
		addAtt.setName("time_added");

		try {
			addAtt.setInputFormat(friends1);
			friends1 = Filter.useFilter(friends1, addAtt);
			// Merge friend2 into friends1 and sort
			friends1.addAll(friends2);
			friends1.sort(0);
			
			// Save modified data
			ArffSaver saver = new ArffSaver();
			saver.setInstances(friends1);			
			saver.setFile(new File("./stage_1/friends.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**********************
	 * 
	 * Merge interets1 and interets2 file Save data sets to data/interets file
	 * 
	 *******************/
	public void mergeInterests() {
		// instances to work on
		Instances interests1 = new Instances(instanceList.get(3));
		Instances interests2 = new Instances(instanceList.get(4));

		// Filters to use
		AddExpression addAtt = new AddExpression();
		addAtt.setExpression("0");
		addAtt.setName("time_added");

		try {
			addAtt.setInputFormat(interests1);
			interests1 = Filter.useFilter(interests1, addAtt);
			// Merge interest2 into interests1 and sort
			interests1.addAll(interests2);
			interests1.sort(0);
			
			// Save modified data
			ArffSaver saver = new ArffSaver();
			saver.setInstances(interests1);
			saver.setFile(new File("./stage_1/interests.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************
	 * 
	 * Remove instances with dst = -1 Save data to data/messages file
	 * 
	 *********************************/

	public void messagesProce() {
		// instances to work on
		Instances messages = new Instances(instanceList.get(5));

		// Filters to use
		RemoveWithValues filter = new RemoveWithValues();
		String[] options = new String[4];
		options[0] = "-C"; // attribute index
		options[1] = "5"; // 5
		options[2] = "-S"; // match if value is smaller than
		options[3] = "0"; // threshold is 0
		
		try {
			filter.setOptions(options);
			filter.setInputFormat(messages);
			messages = Filter.useFilter(messages, filter);
			
			// Save modified data
			ArffSaver saver = new ArffSaver();
			saver.setInstances(messages);			
			saver.setFile(new File("./stage_1/messages.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************
	 * 
	 * Save data to stage1/participants file
	 * 
	 *********************************/

	public void participantsProce() {
		// instances to work on
		Instances participants = new Instances(instanceList.get(6));
		
		// Create a new instance
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new Attribute("user_id"));
		atts.add(new Attribute("institution"));
		atts.add(new Attribute("city"));
		atts.add(new Attribute("country"));
		Instances newparticipants = new Instances("newparticipants", atts, 0);
		// Fill data into new instances
		double[] vals;
		for (int i = 1; i < 77; i++) {
			vals = new double[newparticipants.numAttributes()];
			vals[0] = i;
			for (int j = 0; j < participants.numInstances(); j++) {
				if (participants.instance(j).value(0) == i) {
					if (participants.instance(j).value(1) == 0) {
						vals[1] = participants.instance(j).value(2);
					}
					if (participants.instance(j).value(1) == 1) {
						vals[2] = participants.instance(j).value(2);
					}
					if (participants.instance(j).value(1) == 2) {
						vals[3] = participants.instance(j).value(2);
					}
				}
			}
			newparticipants.add(new DenseInstance(1.0, vals));
		}		

		// Save old instances
		ArffSaver saver = new ArffSaver();
		saver.setInstances(participants);
		try {
			saver.setFile(new File("./stage_1/participants.arff"));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Save new instances
		ArffSaver saver2 = new ArffSaver();
		saver2.setInstances(newparticipants);
		try {
			saver2.setFile(new File("./stage_1/newparticipants.arff"));
			saver2.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**********************************
	 * 
	 * Remove instances with seen_user_id >= 100 Remove attributes
	 * device_major_cod and device_minor_cod Save data to data/proximities file
	 * 
	 ***********************************/
	public void proximityProce() {
		// instances to work on
		Instances proximities = new Instances(instanceList.get(7));
		
		// Filters to remove columns
		Remove rmatt = new Remove();
		String rangeList = "4, 5";
		
		// Filters to remove rows
		RemoveWithValues rminstances = new RemoveWithValues();
		String[] options_rmi = new String[5];
		options_rmi[0] = "-C"; // attribute index
		options_rmi[1] = "3"; // 5
		options_rmi[2] = "-S"; // match if value is smaller than
		options_rmi[3] = "100"; // 10
		options_rmi[4] = "-V"; // 10
		
		
		try {
			rmatt.setAttributeIndices(rangeList);
			rmatt.setInputFormat(proximities);
			proximities = Filter.useFilter(proximities, rmatt);

			rminstances.setOptions(options_rmi);
			rminstances.setInputFormat(proximities);
			proximities = Filter.useFilter(proximities, rminstances);
			
			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(proximities);
			saver.setFile(new File("./stage_1/proximities.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Remove instances with msg_id = -1 Save data to data/receptions file
	 * 
	 */
	public void receptionProce() {
		// instances to work on
		Instances receptions = new Instances(instanceList.get(8));

		// Filters to remove columns
		Remove rmatt = new Remove();
		String rangeList = "1";

		// Filters to remove rows
		RemoveWithValues filter = new RemoveWithValues();
		String[] options = new String[4];
		options[0] = "-C"; // attribute index
		options[1] = "2"; // 5
		options[2] = "-S"; // match if value is smaller than
		options[3] = "0"; // 10
		
		try {
			filter.setOptions(options);
			filter.setInputFormat(receptions);
			receptions = Filter.useFilter(receptions, filter);

			rmatt.setAttributeIndices(rangeList);
			rmatt.setInputFormat(receptions);
			receptions = Filter.useFilter(receptions, rmatt);
			
			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(receptions);
			saver.setFile(new File("./stage_1/receptions.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove instances with msg_id = -1 Save data to data/transmissions file
	 */
	public void transmissionProce() {
		// instances to work on
		Instances transmissions = new Instances(instanceList.get(9));

		// Remove first attribute
		Remove rmatt = new Remove();
		String rangeList = "2,7";
				
		// data bytes discretizer options
		Discretize discrBytes = new Discretize();
		String[] discreteptions = new String[7];
		discreteptions[0] = "-F"; // Use equal-frequency instead of equal-width discretization.
		discreteptions[1] = "-B"; // Specifies the (maximum) number of bins to divide numeric attributes into
		discreteptions[2] = "3"; // number of bins
		discreteptions[3] = "-M"; // Specifies the desired weight of instances per bin for equal-frequency binning.
		discreteptions[4] = "-1"; // default
		discreteptions[5] = "-R"; // Attribute index
		discreteptions[6] = "2"; // the second attribute
		
		try {
			rmatt.setAttributeIndices(rangeList);
			rmatt.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, rmatt);
			
			// Discrete data size and convert to ordinal attribute
			discrBytes.setOptions(discreteptions);
			discrBytes.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, discrBytes);
			Attribute att = transmissions.attribute(1);
			for (int n = 0; n < att.numValues(); n++) {
				transmissions.renameAttributeValue(att, att.value(n), "" + n);
			}
			
			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(transmissions);
			saver.setFile(new File("./stage_1/transmissions.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
