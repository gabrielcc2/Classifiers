package ml.control;

import ml.model.ClusteredDataPoint;
import ml.model.DataPoint;
import ml.model.DataSet;

/**
 * 
 * @summary A K-Nearest Neighbor classifier, extending the Classifier class. 
 * 
 * It overrides the classify function of the super-class.
 * 
 */

public class KNearestNeighbor extends Classifier {
	DataSet trainingData= new DataSet();//Will store the training data, for ease of use during the run.
	int k;
	boolean useHamming=false;
	
	public KNearestNeighbor (int newK, String typeOfDistance){
		if (typeOfDistance.contains("HAMMING")){
			useHamming=true;
		}
		this.k=newK;
	}
	
	/**
	 * The main method to classify data, returning the results.
	 *
	 * @param trainingData
	 * @param testingData
	 * 
	 */
	public DataSet classify (DataSet newTrainingData, DataSet testingData){
	
		trainingData=newTrainingData.getCopy();//Stores a copy of the training data
		
		DataSet results=new DataSet();
		results=testingData.getCopy();
		
		/**We will iterate over all the testing data. For each point we will find the closest KNN
		 * in the training data. After this, we will select as our best guess the most frequent 
		 * output class among them.
		 * */
		int bestGuess;
		for (int i=0; i<testingData.getSize(); i++){
			bestGuess=vote(getKNN(testingData.getTuple(i)));
			results.changeOutputOfTuple(i, bestGuess);
		}
		return results;
	}
	
	/**
	 * Finds a set of points, belonging to the training data, which are the 
	 * k-nearest neighbors of a given point.
	 *
	 * @param DataPoint point
	 * 
	 */
	private ClusteredDataPoint[] getKNN(DataPoint point){
		ClusteredDataPoint[] candidates= new ClusteredDataPoint[k];
		int numPointsSoFar=0;
		int dim=trainingData.getDim();
		double auxDistance;
		for (int i=0; i<trainingData.getSize(); i++){
			if (useHamming){
				auxDistance=getHammingDistance(point, trainingData.getTuple(i));
			}
			else{
				auxDistance=getOrderDistance(point, trainingData.getTuple(i));
			}
			if (numPointsSoFar<k){
				//If not enough points in k-set, we add the point.
				candidates[numPointsSoFar]=new ClusteredDataPoint(trainingData.getTuple(i).getValues(),
																  dim,
																  trainingData.getTuple(i).getOutput(),
																  auxDistance);
				numPointsSoFar++;
			}
			else{
				ClusteredDataPoint auxPoint=null;
				boolean auxPointUsed=false;
				for (int j=0; j<k; j++){
					if (auxDistance<candidates[j].getDistance()){//Now we add the point if the distance is less than the previous
						if (auxPointUsed){
							ClusteredDataPoint auxPoint2=new ClusteredDataPoint(candidates[j].getValues(),
									  dim,
									  candidates[j].getOutput(),
									  candidates[j].getDistance());
							candidates[j]= new ClusteredDataPoint(auxPoint.getValues(), 
										dim,
										auxPoint.getOutput(),
										auxPoint.getDistance());
							auxPoint=new ClusteredDataPoint(auxPoint2.getValues(),
									  dim,
									  auxPoint2.getOutput(),
									  auxPoint2.getDistance());
							auxDistance=auxPoint.getDistance();
						}
						else{
							auxPoint=new ClusteredDataPoint(candidates[j].getValues(),
								  dim,
								  candidates[j].getOutput(),
								  candidates[j].getDistance());
							candidates[j]=new ClusteredDataPoint(trainingData.getTuple(i).getValues(),
								  dim,
								  trainingData.getTuple(i).getOutput(),
								  auxDistance);
							auxDistance=auxPoint.getDistance();
							auxPointUsed=true;
						}
					}//The highest distance will get expelled from the array
				}
			}
		}
		return candidates;
	}
	
	/**In this function, all members of a k-set, will be studied
	 * so the most common output is selected.*/
	private int vote(ClusteredDataPoint[] candidates){
		int outputDim=trainingData.getOutputDim();
		int [] electionArray = new int [outputDim];
		for (int i=0; i<outputDim; i++){
			electionArray[i]=0;
		}
		for (int i=0; i<k; i++){
			electionArray[candidates[i].getOutput()]++;
		}
		int bestGuess=0;
		int numVotes=0;
		for (int i=0; i<outputDim; i++){
			if (electionArray[i]>numVotes){
				bestGuess=i;
				numVotes=electionArray[i];
			}
		}
		return bestGuess;
	}
	
	/**Returns hamming distance: Start with 0, add 1 if attribute values are different, 0 if they are the same.
	 * The final distance is the result of these comparisons over all attributes of the points passed as input*/
	protected double getHammingDistance(DataPoint point1, DataPoint point2){
		int length= point1.getValues().length;
		int [] point1Values= point1.getValues();
		int [] point2Values= point2.getValues();
		double distance=0;
		for (int i=0; i<length; i++){
			if (point1Values[i]!=point2Values[i]){
				distance+=1.0;
			}
		}
		return distance;
	}

	/**Returns order distance: Assuming there is an order between the attributes. */
	protected double getOrderDistance(DataPoint point1, DataPoint point2){
		int length= point1.getValues().length;
		int [] point1Values= point1.getValues();
		int [] point2Values= point2.getValues();
		double distance=0;
		for (int i=0; i<length; i++){
			if (point1Values[i]>point2Values[i]){
				distance+=point1Values[i]-point2Values[i];
			}
			else{
				distance+=point2Values[i]-point1Values[i];
			}
		}
		return distance;
	}
}
