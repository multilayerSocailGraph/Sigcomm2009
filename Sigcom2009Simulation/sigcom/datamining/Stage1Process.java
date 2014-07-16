package Sigcom2009Simulation.sigcom.datamining;

import java.io.File;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Stage1Process extends PreprocessBase {
	public Stage1Process(String filefolder) {
		super(filefolder);
	}

	/****************************************************************
	 * 
	 * Methods for datasets2
	 * 
	 * */
	public void combineRelationships() {

		Instances transmissions = new Instances(instanceList.get(8));
		Instances friends = new Instances(instanceList.get(1));
		Instances interests = new Instances(instanceList.get(2));
		Instances participants = new Instances(instanceList.get(4));
		Instances proximities = new Instances(instanceList.get(6));

		/**
		 *  Create a new instance to store data
		 */
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new Attribute("senderId"));
		atts.add(new Attribute("recieverId"));	
		atts.add(new Attribute("same_institution"));
		atts.add(new Attribute("same_city"));
		atts.add(new Attribute("same_country"));
		atts.add(new Attribute("trans_type_u"));
		atts.add(new Attribute("trans_type_m"));
		atts.add(new Attribute("trans_type_b"));		
		atts.add(new Attribute("trans_byte_large"));
		atts.add(new Attribute("trans_byte_medium"));
		atts.add(new Attribute("trans_byte_small"));
		atts.add(new Attribute("trans_morning"));
		atts.add(new Attribute("trans_afternoon"));
		atts.add(new Attribute("trans_evening"));
		atts.add(new Attribute("contact_frequency"));
		atts.add(new Attribute("proximity_morning"));
		atts.add(new Attribute("proximity_afternoon"));
		atts.add(new Attribute("proximity_evening"));
		atts.add(new Attribute("proximity_frequency"));		
		atts.add(new Attribute("interest")); // use initial interest only
		atts.add(new Attribute("friendship")); // use initial friendship only
		Instances firstFriendship = new Instances("Initailfriends", atts, 0);
		// Fill data into new instances
		double[] vals;
		for (int i = 1; i < 77; i++) {
			for (int j = 1; j < 77; j++) {
				vals = new double[firstFriendship.numAttributes()];
				vals[0] = i;
				// Source equals to destination
				if(j == i) continue;
				vals[1] = j;
				
				// Get institution, city and country ids
				double source_institution = 0, source_city = 0, source_country = 0;
				double des_institution = 0, des_city = 0, des_country = 0;
				for(Instance ins : participants){
					if(ins.value(0) == i){ // found source
						source_institution = ins.value(1);
						source_city = ins.value(2);
						source_country = ins.value(3);
					}else if(ins.value(0) == j){ // found destination
						des_institution = ins.value(1);
						des_city = ins.value(2);
						des_country = ins.value(3);
					}
					if(source_institution != 0 && source_city != 0 && source_country != 0 &&
							des_institution != 0 && des_city != 0 && des_country != 0)
						break;
				}
				// Check whether same
				if(source_institution == des_institution)
					vals[2] = 1;
				if(source_city == des_city)
					vals[3] = 1;
				if(source_country == des_country)
					vals[4] = 1;
					
				// transmission type
				double uni_count = 0, multi_count = 0, broad_count = 0;
				// message size count
				double large_count = 0, medium_count = 0, small_count = 0;
				// contact time slot
				double morning_count = 0, afternoon_count = 0, evening_count = 0;
				// total contact times
				double contact_count = 0;				
				for(Instance ins : transmissions){
					if(ins.value(2) == i && ins.value(3) == j){
						
						// Get contact type
						if(ins.value(0) == 1)
							uni_count++;
						else if(ins.value(0) == 2)
							multi_count++;
						else if(ins.value(0) == 3)
							broad_count++;
						
						// Get contact message size
						if(ins.value(1) == 2)
							large_count++;
						else if(ins.value(1) == 1)
							medium_count++;
						else if(ins.value(1) == 0)
							small_count++;
						
						// Get contact time slot
						// Discretize time stamps
						int timeslot = (int) (ins.value(4) % 86400);
						if (timeslot <= 60 * 60 * 8) {
							morning_count++;
						} else if (timeslot > 60 * 60 * 8 && timeslot <= 60 * 60 * 8 * 2) {
							afternoon_count++;
						} else if (timeslot > 60 * 60 * 8 * 2) {
							evening_count++;
						}
						
						contact_count = large_count+medium_count+small_count;
					}
				}
				vals[5] = uni_count;
				vals[6] = multi_count;
				vals[7] = broad_count;
				vals[8] = large_count;
				vals[9] = medium_count;
				vals[10] = small_count;
				vals[11] = morning_count;
				vals[12] = afternoon_count;
				vals[13] = evening_count;
				vals[14] = contact_count;
				
				// Proximities time slot
				morning_count = 0;
				afternoon_count = 0;
				evening_count = 0;
				for(Instance ins : proximities){
					if(ins.value(1) == i && ins.value(2) == j){
						// Get Proximities time slot
						// Discretize time stamps
						int timeslot = (int) (ins.value(0) % 86400);
						if (timeslot <= 60 * 60 * 8) {
							morning_count++;
						} else if (timeslot > 60 * 60 * 8 && timeslot <= 60 * 60 * 8 * 2) {
							afternoon_count++;
						} else if (timeslot > 60 * 60 * 8 * 2) {
							evening_count++;
						}
					}
				}
				vals[15] = morning_count;
				vals[16] = afternoon_count;
				vals[17] = evening_count;
				vals[18] = morning_count+afternoon_count+evening_count;
				
				// friendship relationship
				vals[20] = 0;
				for(Instance ins : friends){
					if(ins.value(0) == i && ins.value(1) == j && ins.value(2) == 0){
						vals[20] = 1;
						break;
					}
				}
				
				// interest relationship
				ArrayList<Double> sourceGroup = new ArrayList<Double>();
				ArrayList<Double> destGroup = new ArrayList<Double>();
				double commonInterest_count = 0;
				
				for(Instance ins : interests){
					if(ins.value(0) == i && ins.value(2) == 0){
						sourceGroup.add(ins.value(1));
					}else if(ins.value(0) == j && ins.value(2) == 0){
						destGroup.add(ins.value(1));
					}
				}
				
				for(Double source : sourceGroup){
					for(Double dest : destGroup){
						if(source.equals(dest)){
							commonInterest_count++;
						}
					}
				}
				vals[19] = commonInterest_count;
				
				DenseInstance newIns = new DenseInstance(1.0, vals);				
				firstFriendship.add(newIns);
			}
		}

		try {
			// Convert numeric attribute to nominal attribute
			NumericToNominal convert = new NumericToNominal();
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "3, 4, 5"; // range of variables to make numeric
			convert.setOptions(options);
			convert.setInputFormat(firstFriendship);
			firstFriendship = Filter.useFilter(firstFriendship, convert);
						
			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(firstFriendship);
			saver.setFile(new File("./Stage_2/Newfriendship0.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(firstFriendship.instance(0).value(1));

	}
}
