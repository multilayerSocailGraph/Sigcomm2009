package sigcom.datamining;

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
<<<<<<< HEAD
	private static void processStage1data2() {
		Stage1Process3 stage1_dataset = new Stage1Process3("./Stage_1");
		stage1_dataset.combineRelationships();
		System.out.println("Stage 1 Dataset process 3 is done.");

		Stage1Process4 stage1_dataset4 = new Stage1Process4("./Stage_1");
		stage1_dataset4.combineRelationships();
		System.out.println("Stage 1 Dataset process 4 is done.");

=======
	private static void processStage1data() {
		Stage1Process stage1_dataset1 = new Stage1Process("./Stage_1");
		stage1_dataset1.combineRelationships();
		System.out.println("Stage 1 Data process is done.");
		
		Stage1Data_discretize stage1_dataset2 = new Stage1Data_discretize("./Stage_1");
		stage1_dataset2.combineRelationships();
		System.out.println("Stage 1 Data discretize is done.");
		
		Stage1TemporalFriends stage1_dataset3 = new Stage1TemporalFriends("./Stage_1");
		stage1_dataset3.combineRelationships();		
		System.out.println("Stage 1 Dataset process Temporal Friends is done.");
>>>>>>> ChaoBranch
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
