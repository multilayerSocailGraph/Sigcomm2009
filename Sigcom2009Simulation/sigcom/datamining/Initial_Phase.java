package Sigcom2009Simulation.sigcom.datamining;

import java.util.ArrayList;

import Sigcom2009Simulation.sigcom.datamining.RawDataProcess;

import weka.core.Instances;

public class Initial_Phase {
	
	public ArrayList<Instances> getdata(){
		/*********************************************
		 * 
		 * Prepare data
		 * 
		 * ***************************************/
		RawDataProcess raw_datasets = new RawDataProcess("./sigcom2009_RawData");
		return raw_datasets.instanceList;
	}
	
	
}
