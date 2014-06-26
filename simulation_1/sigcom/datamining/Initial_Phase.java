package sigcom.datamining;

import java.util.ArrayList;

import sigcom.datamining.RawDataProcess;
import weka.core.Instance;
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
