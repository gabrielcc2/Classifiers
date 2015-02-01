package ml.control;

import ml.model.RunStats;

/**
 * 
 * @summary This is the main class of the project. 
 *          
 *          In here a AnalysisHandler object is created to load the data, then a number of classifications
 *          can be performed and evaluated using this object.
 * 
 */


public class AnalyzeData {
	public static void main(String[] args) {

		String NAME_FILE="input/car.c45-names"; //Defines the name of the names file.
		String DATA_FILE="input/car.data";//Defines the name of the data file.
		int NUM_REPEATS=100;//Number of repetitions for getting the average error.
		int K= 5;
		String typeOfDistance="ORDER"; //Can be HAMMING or ORDER.
		
		//First we load the data and configure the analysis handler.
		AnalysisHandler handler = new AnalysisHandler();
		handler.loadData(NAME_FILE, DATA_FILE);
		handler.setPartitionConfig((double)2/3); //Signals to select 2/3 as training data, 1/3 as testing data.		
		
		/**Naive Bayes*/
		
		Classifier classifier = new NaiveBayes(); //We create a new classifier of type Naive Bayes.
		
		System.out.println("Getting results for a NaiveBayes Classifier....");
		
		//Now we are ready to run the classification
		RunStats stats = handler.run(classifier); //And we store statistics of the run
				
		stats.plotConfusionMatrix(); //Plot the statistics in a confusion matrix

		//Now we calculate the average error:
		System.out.println("Now we will calculate the average error over "+NUM_REPEATS+" repetitions. It might take a minute.");
		
		System.out.println("Average error over "+NUM_REPEATS+" repetitions: "+handler.getAvgErrorRate(classifier, NUM_REPEATS));
		
		System.out.println("*******************************************************************************************");
		
		
		
		/**KNN*/
		
		System.out.println("Getting results for a K-Nearest Neighbor Classifier, with k="+K+", using "+typeOfDistance+" as distance measure....");
		
		/**We create a new classifier of type KNN, passing K to the constructor and also HAMMING if using hamming distance
		 * or ORDER if using by order distance. The default is by order.*/
		classifier = new KNearestNeighbor(K, typeOfDistance);
		
		//Now we are ready to run the classification
		stats = handler.run(classifier); //And we store statistics of the run

		stats.plotConfusionMatrix(); //Plot the statistics in a confusion matrix

		//Now we calculate the average error:
		System.out.println("Now we will calculate the average error over "+NUM_REPEATS+" repetitions. It might take a minute.");
		
		System.out.println("Average error over "+NUM_REPEATS+" repetitions: "+handler.getAvgErrorRate(classifier, NUM_REPEATS));
		
		System.out.println("*******************************************************************************************");
		
		
		/**K-Means*/
		
		System.out.println("Getting results for a K-Means Classifier, with k="+K+", using "+typeOfDistance+" as distance measure....");
		
		/**We create a new classifier of type KMeans, passing K to the constructor and also HAMMING if using hamming distance
		 * or ORDER if using by order distance. The default is by order.*/
		classifier = new KMeans(K, typeOfDistance);

		//Now we are ready to run the classification
		stats = handler.run(classifier); //And we store statistics of the run

		stats.plotConfusionMatrix(); //Plot the statistics in a confusion matrix

		//Now we calculate the average error:
		System.out.println("Now we will calculate the average error over "+NUM_REPEATS+" repetitions. It might take a minute.");
		
		System.out.println("Average error over "+NUM_REPEATS+" repetitions: "+handler.getAvgErrorRate(classifier, NUM_REPEATS));
		
	}
}
