package ml.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.model.DataPoint;
import ml.model.DataSet;

/**
 * 
 * @summary A K-Means classifier, extending the KNearestNeighbor class. 
 * 
 * It overrides the classify function of the super-class, and also the distance functions in order
 * to support using doubles as cluster centers. 
 * 
 */

public class KMeans extends KNearestNeighbor {

	
	public KMeans (int newK, String typeOfDistance) {
		super(newK, typeOfDistance);
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

		/**We start this classifier by combining the training and testing data into one
		 * large set. However to distinguish them, we use the ids of the data points.
		 * Those points whose ids are lower than a threshold will be training points.*/
		int threshold=trainingData.getSize();
		for (int i=0; i<threshold; i++){
			trainingData.setTupleId(i, i);
		}
		
		DataSet results=new DataSet();
		results=testingData.getCopy();
		
		for (int i=0; i<results.getSize(); i++){
			trainingData.addTuple(results.getTupleValuesAsIntegers(i),results.getOutputNum(i));
			trainingData.setTupleId(threshold+i, threshold+i);
			results.setTupleId(i, threshold+i);
		}
		
		/**
		 * Then we call the k-means algorithm over all this data. 
		 * 
		 * */
		
		Map<Integer,Integer> clustering= new HashMap<Integer,Integer>(200); /**Maps a position (identified by its id)
		* to a cluster, identified by its location in the cluster centers array.
		*/
		
		double[][] clusterCenters= new double[k][trainingData.getDim()];
		
		/**We initialize the cluster centers to be the first k points*/
		for (int i=0; i<k; i++){
			for (int j=0; j<trainingData.getDim(); j++){
				clusterCenters[i][j]=(double)trainingData.getTuple(i).getValue(j);
			}
			clustering.put(i, i);
		}
		boolean aChangeHappened=true;
		
		/**The k-means loop*/
		while(aChangeHappened){
			aChangeHappened=false;
			/**First we assign all points to clusters*/
			for (int i=0; i<trainingData.getSize(); i++){
				double previousDistance, currentDistance;
				for (int j=0; j<k; j++){
					if (clustering.get(i)!=null){
						if(useHamming){
							previousDistance=getHammingDistance(trainingData.getTuple(i), clusterCenters[clustering.get(i)]);
							currentDistance=getHammingDistance(trainingData.getTuple(i), clusterCenters[j]);
						}
						else{
							previousDistance=getOrderDistance(trainingData.getTuple(i), clusterCenters[clustering.get(i)]);
							currentDistance=getOrderDistance(trainingData.getTuple(i), clusterCenters[j]);
						}
						if(currentDistance<previousDistance){ //Now we check if updating is required.
							aChangeHappened=true;
							clustering.put(i, j);
						}
					}
					else{
						clustering.put(i, j);
						aChangeHappened=true;
					}
				}
			}
			/**Then, if a change happened, we re-calculate all cluster centers, and continue..*/
			if(aChangeHappened){
				List<DataPoint> cluster = new ArrayList<DataPoint>();
				for (int i=0; i<k; i++){
					cluster.clear();
					for (int j=0; j<trainingData.getSize(); j++){
						if (clustering.get(j)==i){
							cluster.add(trainingData.getTuple(j));
						}
					}
					double tentativeCenter[]=getNewClusterCenter(cluster, clusterCenters[i]);
					for (int j=0; j<trainingData.getDim(); j++){
							clusterCenters[i][j]=tentativeCenter[j];
					}
				}
			}
		}
		
		 /** When all points are satisfactorily clustered, we will go through each cluster and find the most common
		 * output. Then we will iterate on all ids within the cluster higher than threshold, and
		 * go to the results array threshold-number, update its output. And then we return*/
		
		for (int i=0; i<k; i++){
			List<DataPoint> cluster = new ArrayList<DataPoint>();
			for (int j=0; j<trainingData.getSize(); j++){
				if (clustering.get(j)==i){
					cluster.add(trainingData.getTuple(j));
				}
			}
			int output=getMostCommonOutput(cluster);
			for (int j=0; j<cluster.size(); j++){
				if (cluster.get(j).getId()>=threshold){
					results.getTuple(cluster.get(j).getId()-threshold).setOutput(output);
				}
			}
		}		 
		return results;
	}
		
	private int getMostCommonOutput (List<DataPoint> cluster){
		int outputDim=trainingData.getOutputDim();
		int [] electionArray = new int [outputDim];
		for (int i=0; i<outputDim; i++){
			electionArray[i]=0;
		}
		for (int i=0; i<cluster.size(); i++){
			electionArray[cluster.get(i).getOutput()]++;
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
	
	
	private double[] getNewClusterCenter (List<DataPoint> cluster, double[] currentCenter){
		double[] result= new double[currentCenter.length];
		result=currentCenter.clone();
		for (int i=0; i<trainingData.getDim(); i++){//We will iterate over each attribute
			double center=0;
			for (int j=0; j<cluster.size(); j++){
				center+=cluster.get(j).getValue(i);//For each attribute we will add all the values observed in the cluster
			}
			center=(double)center/cluster.size();//We will find the average and consider this to be the center.
			result[i]=center;	
		}
		return result;
	}
	
	/**Returns hamming distance: Start with 0, add 1 if attribute values are different, 0 if they are the same.
	 * The final distance is the result of these comparisons over all attributes of the points passed as input*/
	protected double getHammingDistance(DataPoint point1, double[] point2){
		int length= point1.getValues().length;
		int [] point1Values= point1.getValues();
		double distance=0;
		for (int i=0; i<length; i++){
			if (point1Values[i]!=point2[i]){
				distance+=1.0;
			}
		}
		return distance;
	}

	/**Returns order distance: Assuming there is an order between the attributes. */
	protected double getOrderDistance(DataPoint point1, double[] point2){
		int length= point1.getValues().length;
		int [] point1Values= point1.getValues();
		double distance=0;
		for (int i=0; i<length; i++){
			if (point1Values[i]>point2[i]){
				distance+=point1Values[i]-point2[i];
			}
			else{
				distance+=point2[i]-point1Values[i];
			}
		}
		return distance;
	}
}
