package St_andrewssassySimulation.st_andrewssassy.datamining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Instances;

public class PreprocessBase {
	String foldername = "";
	ArrayList<String> filestoread = new ArrayList<String>();
	protected ArrayList<Instances> instanceList = new ArrayList<Instances>();

	public PreprocessBase(String filefolder) {
		this.foldername = filefolder;
		this.listFilesForFolder(new File(filefolder));
		this.readFilesInFolder();
	}

	/**
	 * Read data in files Add data object to instanceList
	 * 
	 * @exception if
	 *                file is not found
	 */
	private void readFilesInFolder() {
		for (int i = 0; i < filestoread.size(); i++) {
			try {
				BufferedReader datafile = this.readDataFile(foldername + "/"
						+ filestoread.get(i));
				instanceList.add(new Instances(datafile));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * Read data in a file
	 * 
	 * @param filename
	 * @return BufferedReader inputReader
	 * @exception if
	 *                file is not found
	 */
	private BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;

		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}

		return inputReader;
	}

	/**
	 * Read file names to the list in the folder add file names to filestoread
	 * list.
	 * 
	 * @param folder
	 *            name the folder to read the data from
	 */
	private void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				filestoread.add(fileEntry.getName());
				// System.out.println(fileEntry.getName());
			}
		}
	}
}
