package St_andrewssassySimulation.st_andrewssassy.main;

import St_andrewssassySimulation.st_andrewssassy.datamining.DataMining;

public class Run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// flag to decide data process type
		// 0: Only process data in RawData folder
		// 1: Only process data in stage 1 folder
		// otherwise process both.
		int dataProcessFlag = 2;
		DataMining.dataProcess(dataProcessFlag);
		
		// Simulation.comparation();
	}

}
