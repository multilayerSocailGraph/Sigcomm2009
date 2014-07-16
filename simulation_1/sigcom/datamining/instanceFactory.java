package simulation_1.sigcom.datamining;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instances;

public class instanceFactory {

	public static Instances createIns() {
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
		Instances newIns = new Instances("Initailfriends", atts, 0);
		
		return newIns;
	}

}
