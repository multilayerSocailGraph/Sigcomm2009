package sigcom.datamining;

import java.io.File;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddExpression;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Stage1Process extends PreprocessBase{
	public Stage1Process(String filefolder) {
		super(filefolder);
	}

	/****************************************************************
	 * 
	 * Methods for datasets2
	 * 
	 **********************************************************/
	public void combineRelationships() {

		// Instances to work on
		Instances transmissions = new Instances(instanceList.get(8));
		Instances friends = new Instances(instanceList.get(1));
		Instances interests = new Instances(instanceList.get(2));
		Instances participants = new Instances(instanceList.get(5));
		Instances proximities = new Instances(instanceList.get(6));
		Instances oldtransmissions = new Instances(instanceList.get(8));

		// filter to add friendship attribute
		AddExpression filter_friend = new AddExpression();
		filter_friend.setExpression("0");
		filter_friend.setName("friendship");
		
		// filter to add common interest attribute
		AddExpression filter_interest = new AddExpression();
		filter_interest.setExpression("0");		
		filter_interest.setName("interest");
		
		// filter to add time slot attribute
		AddExpression filter_time = new AddExpression();
		filter_time.setExpression("0");
		filter_time.setName("timeslot");
		
		// filter to add nearby frequency attribute
		AddExpression filter_near = new AddExpression();
		filter_near.setExpression("0");
		filter_near.setName("near_frequency");
		
		// filter to add contact frequency attribute
		AddExpression filter_contact = new AddExpression();
		filter_contact.setExpression("0");
		filter_contact.setName("contact_frequency");

		// filter to add common institution attribute
		AddExpression filter_institution = new AddExpression();
		filter_institution.setExpression("0");
		filter_institution.setName("same_institution");
		
		// filter to add common city attribute
		AddExpression filter_city = new AddExpression();
		filter_city.setExpression("0");
		filter_city.setName("same_city");
		
		// filter to add common country attribute
		AddExpression filter_country = new AddExpression();
		filter_country.setExpression("0");
		filter_country.setName("same_country");		

		// filter to discretize data bytes
		Discretize discrBytes = new Discretize();
		String[] discreteptions = new String[7];
		discreteptions[0] = "-F"; // Use equal-frequency instead of equal-width discretization.
		discreteptions[1] = "-B"; // Specifies the (maximum) number of bins to divide numeric attributes into
		discreteptions[2] = "3"; // number of bins
		discreteptions[3] = "-M"; // Specifies the desired weight of instances per bin for equal-frequency binning.
		discreteptions[4] = "-1"; // default
		discreteptions[5] = "-R"; // Attribute index
		discreteptions[6] = "2"; // the second attribute
		
		// filter to Discretize near frequency
		Discretize discrNearFreq = new Discretize();
		String[] DNFoptions = new String[7];
		DNFoptions[0] = "-F"; // Use equal-frequency instead of equal-width discretization.
		DNFoptions[1] = "-B"; // Specifies the (maximum) number of bins to divide numeric attributes into
		DNFoptions[2] = "3"; // number of bins
		DNFoptions[3] = "-M"; // Specifies the desired weight of instances per bin for equal-frequency binning.
		DNFoptions[4] = "-1"; // default
		DNFoptions[5] = "-R"; // Attribute index
		DNFoptions[6] = "12"; // the 12th attribute
		
		// filter to Discretize contact frequency
		Discretize discrContactFreq = new Discretize();
		String[] DCFoptions = new String[7];
		DCFoptions[0] = "-F"; // Use equal-frequency instead of equal-width discretization.
		DCFoptions[1] = "-B"; // Specifies the (maximum) number of bins to divide numeric attributes into
		DCFoptions[2] = "3"; // number of bins
		DCFoptions[3] = "-M"; // Specifies the desired weight of instances per bin for equal-frequency binning.
		DCFoptions[4] = "-1"; // default
		DCFoptions[5] = "-R"; // Attribute index
		DCFoptions[6] = "13"; // the second attribute

		try {
			// Add friendship attribute
			filter_friend.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_friend);
			// Add interest attribute
			filter_interest.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_interest);
			// Add time slot attribute
			filter_time.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_time);
			// Add institution attribute
			filter_institution.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_institution);
			// Add city attribute
			filter_city.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_city);
			// Add country attribute
			filter_country.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_country);
			// Add near frequency attribute
			filter_near.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_near);
			// Add contact frequency attribute
			filter_contact.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, filter_contact);

			int tranS, tranD, tranT, tranSGid = -100, tranDGid = -100, timeslot;
			int source_inst = 0, dest_inst = 0, source_city = 0, dest_city = 0, source_country = 0, dest_country = 0;
			int flag1 = 0, flag2 = 0;

			for (int i = 0; i < transmissions.numInstances(); i++) {
				tranS = (int) transmissions.instance(i).value(2);
				tranD = (int) transmissions.instance(i).value(3);
				tranT = (int) transmissions.instance(i).value(4);
				// -------------------------------------------------------
				// Set friendship attribute value
				for (int j = 0; j < friends.numInstances(); j++) {
					if (tranS == friends.instance(j).value(0)
							&& tranD == friends.instance(j).value(1)
							&& tranT >= friends.instance(j).value(2)) {
						transmissions.instance(i).setValue(5, 1);
						break;
					}
				}
				// -------------------------------------------------------
				// Find source and destination nodes group id
				flag1 = 0; flag2 = 0;
				for (int j = 0; j < interests.numInstances(); j++) {
					if (flag1 == 0 && tranS == interests.instance(j).value(0)
							&& tranT >= interests.instance(j).value(2)) {
						tranSGid = (int) interests.instance(j).value(1);
						flag1++;
					}else if (flag2 == 0 && tranD == interests.instance(j).value(0)
							&& tranT >= interests.instance(j).value(2)) {
						tranDGid = (int) interests.instance(j).value(1);
						flag2++;
					}
					if(flag1==1 && flag2==1){
						break;
					}
				}
				
				// Set interest attribute value
				if (tranSGid == tranDGid) {
					transmissions.instance(i).setValue(6, 1);
				}

				// Discretize time stamps
				timeslot = (int) (transmissions.instance(i).value(4) % 86400);
				if (timeslot <= 60 * 60 * 8) {
					transmissions.instance(i).setValue(7, 1);
				} else if (timeslot > 60 * 60 * 8
						&& timeslot <= 60 * 60 * 8 * 2) {
					transmissions.instance(i).setValue(7, 2);
				} else if (timeslot > 60 * 60 * 8 * 2) {
					transmissions.instance(i).setValue(7, 3);
				}
				
				// -------------------------------------------------------
				// Set source and destination institution, city, country attribute value.
				flag1 = 0; flag2 = 0;
				for (int j = 0; j < participants.numInstances()-2; j+=3) {
					if (flag1 == 0 && tranS == participants.instance(j).value(0)) {
						source_inst = (int) participants.instance(j).value(2);
						source_city = (int) participants.instance(j+1).value(2);
						source_country = (int) participants.instance(j+2).value(2);
						flag1++;
					}else if (flag2 == 0 && tranD == participants.instance(j).value(0)) {
						dest_inst = (int) participants.instance(j).value(2);
						dest_city = (int) participants.instance(j+1).value(2);
						dest_country = (int) participants.instance(j+2).value(2);
						flag2++;
					}
					if(flag1==1 && flag2==1){
						break;
					}
				}
				
				if(source_inst == dest_inst){
					transmissions.instance(i).setValue(8, 1);
				}
				if(source_city == dest_city){
					transmissions.instance(i).setValue(9, 1);
				}
				if(source_country == dest_country){
					transmissions.instance(i).setValue(10, 1);
				}				
				// -------------------------------------------------------
				// Set near frequency attribute value
				for (int j = 0; j < proximities.numInstances(); j++) {
					if (tranS == proximities.instance(j).value(1)
							&& tranD == proximities.instance(j).value(2)) {
						double newfreq = transmissions.instance(i).value(11)+1;
						transmissions.instance(i).setValue(11, newfreq);						
					}
				}
				// -------------------------------------------------------
				// Set contact frequency attribute value
				for (int j = 0; j < oldtransmissions.numInstances(); j++) {
					if (tranS == oldtransmissions.instance(j).value(1)
							&& tranD == oldtransmissions.instance(j).value(2)) {
						double newfreq = transmissions.instance(i).value(12)+1;
						transmissions.instance(i).setValue(12, newfreq);						
					}
				}
			}

			// Discrete data size and convert to ordinal attribute
			discrBytes.setOptions(discreteptions);
			discrBytes.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, discrBytes);
			Attribute att = transmissions.attribute(1);
			for (int n = 0; n < att.numValues(); n++) {
				transmissions.renameAttributeValue(att, att.value(n), "" + n);
			}
			
			// Discrete contact frequency and convert to ordinal attribute
			discrNearFreq.setOptions(DNFoptions);
			discrNearFreq.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, discrNearFreq);
			att = transmissions.attribute(11);
			for (int n = 0; n < att.numValues(); n++) {
				transmissions.renameAttributeValue(att, att.value(n), "" + n);
			}
			
			// Discrete contact frequency and convert to ordinal attribute
			discrContactFreq.setOptions(DCFoptions);
			discrContactFreq.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, discrContactFreq);
			att = transmissions.attribute(12);
			for (int n = 0; n < att.numValues(); n++) {
				transmissions.renameAttributeValue(att, att.value(n), "" + n);
			}

			// Convert numeric attribute to nominal attribute
			NumericToNominal convert = new NumericToNominal();
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "1, 2, 6-13"; // range of variables to make numeric
			convert.setOptions(options);
			convert.setInputFormat(transmissions);
			transmissions = Filter.useFilter(transmissions, convert);

			// Save modified data with discretized duration attribute
			ArffSaver saver = new ArffSaver();
			saver.setInstances(transmissions);
			saver.setFile(new File("./Stage_2/friendshipDM.arff"));
			saver.writeBatch();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(transmissions.instance(0).value(1));

	}
}
