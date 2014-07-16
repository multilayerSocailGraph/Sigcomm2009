package simulation_1.sigcom.datamining;

import java.util.ArrayList;
<<<<<<< HEAD:simulation_1/sigcom/datamining/Initial_Phase.java

import simulation_1.sigcom.datamining.RawDataProcess;
=======
>>>>>>> origin/ChaoBranch:Sigcom2009Simulation/sigcom/datamining/Initial_Phase.java
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
