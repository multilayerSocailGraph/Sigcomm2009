package st_andrewssassy.datamining;

public class DataMining {
	public static void dataProcess(int dataProcessFlag) {

		switch (dataProcessFlag) {
		case 0:
			processRawdata(); // Process raw data files in RawData folder
			break;
		case 1:
			// Process data files in stage_1 folder
			processStage1data();
			break;
		default:
			processRawdata();
			processStage1data();
			break;
		}
	}

	/*********************************************
	 * 
	 * Process data files in stage_1 folder
	 * 
	 ****************************************/
	private static void processStage1data() {
		
		Stage1Data_discretize stage1_dataset = new Stage1Data_discretize("./st_andrewssassy_Stage1");
		stage1_dataset.combineRelationships();
		System.out.println("Stage 1 Data discretize is done.");
		
		// Not ready yet
		//Stage1TemporalFriends stage1_dataset3 = new Stage1TemporalFriends("./st_andrewssassy_Stage1");
		//stage1_dataset3.combineRelationships();		
		//System.out.println("Stage 1 Dataset process Temporal Friends is done.");
	}

	/*********************************************
	 * 
	 * Process raw data files in RawData folder
	 * 
	 * ***************************************/
	private static void processRawdata() {
		RawDataProcess raw_datasets = new RawDataProcess("./st_andrewssassy_RawData");
		// change and Save dsn file to Stage 1
		raw_datasets.dsn_processs();

		// Save srsn file to Stage 1
		raw_datasets.srsn_processs();

		System.out.println("Raw Data process is done.");
	}
}
