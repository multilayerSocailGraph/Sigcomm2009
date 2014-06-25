package sigcom.datamining;

public class DataMining {
	public static void dataProcess(int dataProcessFlag) {

		switch (dataProcessFlag) {
		case 0:
			processRawdata(); // Process raw data files in RawData folder
			break;
		case 1:
			// Process data files in stage_1 folder
			processStage1data2();
			break;
		default:
			processRawdata();
			processStage1data2();
			break;
		}
	}

	/*********************************************
	 * 
	 * Process data files in stage_1 folder to new format
	 * 
	 ****************************************/
	private static void processStage1data2() {
		Stage1Process3 stage1_dataset = new Stage1Process3("./Stage_1");
		stage1_dataset.combineRelationships();

		System.out.println("Stage 1 Dataset process 3 is done.");

		Stage1Process4 stage1_dataset4 = new Stage1Process4("./Stage_1");
		stage1_dataset4.combineRelationships();

		System.out.println("Stage 1 Dataset process 4 is done.");

	}

	/*********************************************
	 * 
	 * Process raw data files in RawData folder
	 * 
	 * ***************************************/
	private static void processRawdata() {
		RawDataProcess raw_datasets = new RawDataProcess("./RawData");
		// change activity file
		raw_datasets.activity_processs();

		// Merge friends1 and friends2 file
		raw_datasets.mergeFriends();

		// Merge interets1 and interets2 file
		raw_datasets.mergeInterests();

		// Remove redundant informations from files
		// messages,receptions,transmissions
		raw_datasets.messagesProce();

		// Change format of participants
		raw_datasets.participantsProce();

		// Remove users who are not participated
		raw_datasets.proximityProce();

		// Remove message id
		raw_datasets.receptionProce();

		// Remove message id and success status
		raw_datasets.transmissionProce();

		System.out.println("Raw Data process is done.");
	}
}
