package Sigcom2009Simulation.sigcom.main;

import Sigcom2009Simulation.sigcom.datamining.DataMining;

public class Run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// flag to decide data process type
		// 0: Only process data in RawData folder
		// 1: Only process data in stage 1 folder
		// otherwise process both.
		int dataProcessFlag = 1;
		DataMining.dataProcess(dataProcessFlag);
		
		// Simulation.comparation();

	}

}
