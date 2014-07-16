package simulation_1.sigcom.datamining;

import java.util.ArrayList;

import simulation_1.sigcom.datamining.RawDataProcess;
import weka.core.Instances;

public class Initial_Phase {
	
	public ArrayList<Instances> getdata(){
		/*********************************************
		 * 
		 * Prepare data
		 * 
		 * ***************************************/
		RawDataProcess raw_datasets = new RawDataProcess("./RawData");
		return raw_datasets.instanceList;
	}
	
	
}
