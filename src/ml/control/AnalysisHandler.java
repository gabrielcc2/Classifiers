package ml.control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ml.model.DataSet;
import ml.model.RunStats;

/**
 * 
 * @summary This object is in charge of loading the data and managing an analysis of it, using a classifier.
 *          
 * 
 */

public class AnalysisHandler {
	private DataSet data, trainingData, testingData, latestResults; //Several data sets. 
	/**The DataSet data is for the original data
	 * trainingData and testingData are for holding the partitioned data used in classification
	 * latestResults stores the latest results of a classification.
	 * 
	 * */
	
	private boolean hasLoadedData=false;
	private double partitionPercentage=0.5; //Refers to how much of the data will be used for the training.
	
	/**
	 * The main method to load the data from files into corresponding data structures
	 *
	 * @param namesFile names file in a specific format
	 * @param dataFile data file in a specific format
	 * 
	 */
	public int loadData(String namesFile, String dataFile){
		BufferedReader reader=null;  
		data = new DataSet();
		
		/*First we read the names file*/
		try {
			reader = new BufferedReader(new FileReader(namesFile));
			String line = null;
			boolean skip=false;
			while ((line = reader.readLine()) !=null & !skip)   {
				if (line.contains("class values")){ //Here we will read the categories for the output variable or class
					if ((line = reader.readLine())!=null){
						if (line.length()<3){
							line = reader.readLine();//Skip this line
						}
						String[] splited = line.split(",");
						for (String part : splited) {
							part=part.replaceAll("\\s","");
							data.addOutputCategory(part);
						}
						skip=true;
					}
				}
			}
			skip=false;
			while (!skip & (line = reader.readLine()) !=null)   {
				if (line.contains("| attributes")){ //Here we will read the attributes
					skip=true;
					while ((line = reader.readLine()) !=null){
						if (line.length()>1){
							String auxName=line.substring(0, line.indexOf(':'));
							line=line.substring(line.indexOf(':')+1, line.length()-1);
							String[] splited = line.split(",");
							List<String> auxCategories= new ArrayList<String>();
							for (String part : splited) {
								part=part.replaceAll("\\s","");
								auxCategories.add(part);
							}
							data.addInfo(auxName, auxCategories);
						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			  e.printStackTrace();
			}
		}
		
		/*Now we read the data file*/
		try {
			reader = new BufferedReader(new FileReader(dataFile));
			String line = null;
			while ((line = reader.readLine()) !=null){
				int auxArray[] = new int [data.getDim()];
				int auxOutput=-1;
				String[] splited = line.split(",");
				int i=0;
				for (String part : splited) {
					if (i<data.getDim()){
						part=part.replaceAll("\\s","");
						auxArray[i]=data.getInfoValue(i, part);
					}
					else {
						part=part.replaceAll("\\s","");
						auxOutput=data.getOutputNum(part);
					}
					i++;
				}
				data.addTuple(auxArray, auxOutput);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			  e.printStackTrace();
			}
		}
		hasLoadedData=true;
		return 1;
	}

	/**Series of getters for the data sets*/
	
	public DataSet getData(){
		return data.getCopy();
	}
	
	public DataSet getLatestResults(){
		return latestResults.getCopy();
	}
	
	public DataSet getTestingData(){
		return testingData.getCopy();
	}
	
	public DataSet getTrainingData(){
		return trainingData.getCopy();
	}
	
	/**
	 * Allows to set the percentage of data that will be used for training
	 *
	 * @param num percentage of the data that will be used for training, 
	 * by default: 0.5
	 * 
	 */
	public void setPartitionConfig(double num){
		partitionPercentage=num;
	}

	/**
	 * Allows to check the percentage of data that will be used for training
	 * 
	 */
	public double getPartitionConfig(){
		return partitionPercentage;
	}
	
	/**
	 * Launches the classification over the randomly partitioned data, using a specific classifier, 
	 * passed as input. Returns an object of type RunStats, which holds the stats for this run.
	 *
	 * @param classifier
	 * 
	 * NOTE: Returns an empty RunStats object if data has not been loaded.
	 */
	public RunStats run (Classifier classifier){
		RunStats stats= new RunStats();
		if (hasLoadedData){
			partitionData(); //Randomly partitions the data according to the partitionPercentage variable
			latestResults=classifier.classify(trainingData, testingData); //Stores the latest results
			stats.calculateRunStats(latestResults, testingData); //Calculates the stats
		}
		return stats; //Returns the stats
	}
	
	/**
	 * Similar to the run function, it launches a number of repeats of the classification, and returns
	 * only the average error rate. 
	 * 
	 * @param classifier
	 * @param numRepeats
	 * 
	 * NOTE: Returns -1 if data has not been loaded.
	 */
	public double getAvgErrorRate (Classifier classifier, int numRepeats){
		if (hasLoadedData){
			double accError=0;
			for (int i=0; i<numRepeats; i++){
				RunStats stats= new RunStats();
				this.partitionData();
				latestResults=classifier.classify(trainingData, testingData);
				stats.calculateRunStats(latestResults, testingData);
				accError+=stats.getError();
			}
			return (double)accError/numRepeats;
		}
		return -1;
	}
	
	/**
	 * Function that partitions the data. To do so it shuffles the data, according to a function implemented
	 * in the DataSet class, and then splits it according to functions in the DataSet class, and also
	 * to the partitionPercentage variable. 
	 * 
	 */
	private int partitionData(){
		trainingData= new DataSet();
		testingData= new DataSet();
		DataSet auxDataSet = new DataSet();
		auxDataSet=data.getCopy();
		auxDataSet.shufflePoints(); //In charge of the shuffling
		trainingData=auxDataSet.getCopyOfFirstChunk(partitionPercentage); 
		testingData=auxDataSet.getCopyOfLastChunk(partitionPercentage);
		return 1;
	}
}
