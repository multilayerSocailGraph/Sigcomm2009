package St_andrewssassySimulation.st_andrewssassy.datamining;

import java.util.ArrayList;
import weka.core.Instances;

public class Initial_Phase {
	
	public ArrayList<Instances> getdata(){
		/*********************************************
		 * 
		 * Prepare data
		 * 
		 * ***************************************/
		RawDataProcess raw_datasets = new RawDataProcess("./st_andrewssassy_RawData");
		return raw_datasets.instanceList;
	}
	
	
}
