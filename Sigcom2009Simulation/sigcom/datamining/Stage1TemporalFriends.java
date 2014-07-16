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

public class Stage1TemporalFriends extends PreprocessBase {

	Instances friendshipIns, transmissions, friends, 
				interests, participants, proximities;

	public Stage1TemporalFriends(String filefolder) {
		super(filefolder);
	}

	/****************************************************************
	 * 
	 * Methods for datasets2
	 * 
	 ***************************************************************/
	public void combineRelationships() {

		transmissions = new Instances(instanceList.get(8));
		friends = new Instances(instanceList.get(1));
		interests = new Instances(instanceList.get(2));
		participants = new Instances(instanceList.get(4));
		proximities = new Instances(instanceList.get(6));

		friendshipIns = instanceFactory.createIns();

		filldata(friendshipIns);

		try {
			// Convert numeric attribute to nominal attribute
			NumericToNominal convert = new NumericToNominal();
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "3, 4, 5, 21"; // range of variables to make numeric
			convert.setOptions(options);
			convert.setInputFormat(friendshipIns);
			friendshipIns = Filter.useFilter(friendshipIns, convert);

			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(friendshipIns);
			saver.setFile(new File("./sigcom2009_Stage2/NewfriendshipAll.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(friendshipIns.instance(0).value(1));

	}

	// Fill data into new instances
	private void filldata(Instances friendshipIns) {		
		double[] valsInitial, valsAfter = null;
		double milestone = -1;
		for (int i = 1; i < 77; i++) {			
			for (int j = 1; j < 77; j++) {
				if (j == i)
					continue;
				
				// friendship relationship				
				valsInitial = new double[friendshipIns.numAttributes()];
				for (Instance ins : friends) {
					if (ins.value(0) == i && ins.value(1) == j){
						if(ins.value(2) <= 0) {
							valsInitial[20] = 1;
						}else{
							valsInitial[20] = 0;
							valsAfter = new double[friendshipIns.numAttributes()];
							valsAfter[20] = 1;
							milestone = ins.value(2);
						}
					}
					break;
				}
				
				valsInitial[0] = i;				
				valsInitial[1] = j;
				if(milestone != -1){
					valsAfter[0] = i;				
					valsAfter[1] = j;
				}				

				// Get institution, city and country ids
				double source_institution = 0, source_city = 0, source_country = 0;
				double des_institution = 0, des_city = 0, des_country = 0;
				for (Instance ins : participants) {
					if (ins.value(0) == i) { // found source
						source_institution = ins.value(1);
						source_city = ins.value(2);
						source_country = ins.value(3);
					} else if (ins.value(0) == j) { // found destination
						des_institution = ins.value(1);
						des_city = ins.value(2);
						des_country = ins.value(3);
					}
					if (source_institution != 0 && source_city != 0
							&& source_country != 0 && des_institution != 0
							&& des_city != 0 && des_country != 0)
						break;
				}
				// Check whether same
				if (source_institution == des_institution){
					valsInitial[2] = 1;
					if(milestone != -1){
						valsAfter[2] = 1;
					}
				}
					
				if (source_city == des_city){
					valsInitial[3] = 1;
					if(milestone != -1){
						valsAfter[3] = 1;
					}
				}
					
				if (source_country == des_country){
					valsInitial[4] = 1;
					if(milestone != -1){
						valsAfter[4] = 1;
					}
				}					

				// transmission type
				double uni_count = 0, multi_count = 0, broad_count = 0;
				// message size count
				double large_count = 0, medium_count = 0, small_count = 0;
				// contact time slot
				double morning_count = 0, afternoon_count = 0, evening_count = 0;
				// total contact times
				double contact_count = 0;
				for (Instance ins : transmissions) {
					if (ins.value(2) == i && ins.value(3) == j) {

						// Get contact type
						if (ins.value(0) == 1)
							uni_count++;
						else if (ins.value(0) == 2)
							multi_count++;
						else if (ins.value(0) == 3)
							broad_count++;

						// Get contact message size
						if (ins.value(1) == 2)
							large_count++;
						else if (ins.value(1) == 1)
							medium_count++;
						else if (ins.value(1) == 0)
							small_count++;

						// Get contact time slot
						// Discretize time stamps
						int timeslot = (int) (ins.value(4) % 86400);
						if (timeslot <= 60 * 60 * 8) {
							morning_count++;
						} else if (timeslot > 60 * 60 * 8
								&& timeslot <= 60 * 60 * 8 * 2) {
							afternoon_count++;
						} else if (timeslot > 60 * 60 * 8 * 2) {
							evening_count++;
						}

						contact_count = large_count + medium_count
								+ small_count;
					}
				}
				valsInitial[5] = uni_count;
				valsInitial[6] = multi_count;
				valsInitial[7] = broad_count;
				valsInitial[8] = large_count;
				valsInitial[9] = medium_count;
				valsInitial[10] = small_count;
				valsInitial[11] = morning_count;
				valsInitial[12] = afternoon_count;
				valsInitial[13] = evening_count;
				valsInitial[14] = contact_count;

				// Proximities time slot
				morning_count = 0;
				afternoon_count = 0;
				evening_count = 0;
				for (Instance ins : proximities) {
					if (ins.value(1) == i && ins.value(2) == j) {
						// Get Proximities time slot
						// Discretize time stamps
						int timeslot = (int) (ins.value(0) % 86400);
						if (timeslot <= 60 * 60 * 8) {
							morning_count++;
						} else if (timeslot > 60 * 60 * 8
								&& timeslot <= 60 * 60 * 8 * 2) {
							afternoon_count++;
						} else if (timeslot > 60 * 60 * 8 * 2) {
							evening_count++;
						}
					}
				}
				valsInitial[15] = morning_count;
				valsInitial[16] = afternoon_count;
				valsInitial[17] = evening_count;
				valsInitial[18] = morning_count + afternoon_count + evening_count;				

				// interest relationship
				ArrayList<Double> sourceGroup = new ArrayList<Double>();
				ArrayList<Double> destGroup = new ArrayList<Double>();
				double commonInterest_count = 0;

				for (Instance ins : interests) {
					if (ins.value(0) == i && ins.value(2) == 0) {
						sourceGroup.add(ins.value(1));
					} else if (ins.value(0) == j && ins.value(2) == 0) {
						destGroup.add(ins.value(1));
					}
				}

				for (Double source : sourceGroup) {
					for (Double dest : destGroup) {
						if (source.equals(dest)) {
							commonInterest_count++;
						}
					}
				}
				valsInitial[19] = commonInterest_count;

				DenseInstance newIns = new DenseInstance(1.0, valsInitial);
				friendshipIns.add(newIns);
			}
		}

	}
}
