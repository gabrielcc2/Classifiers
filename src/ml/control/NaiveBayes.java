package ml.control;

import ml.model.DataPoint;
import ml.model.DataSet;


/**
 * 
 * @summary A NaiveBayes classifier, extending the Classifier class. 
 * 
 * It overrides the classify function of the super-class.
 * 
 * NOTE: Special support for when possible attribute values do not appear in the training data
 * is not provided. This could be done in future work.
 * 
 * @author Pablo, Gabriel
 */

public class NaiveBayes extends Classifier {
	DataSet trainingData= new DataSet();//Will store the training data, for ease of use during the run.
	
	
	/**
	 * The main method to classify data, returning the results.
	 *
	 * @param trainingData
	 * @param testingData
	 * 
	 * @author Pablo, Gabriel (added the estimation of probability for the output class)
	 */
	public DataSet classify (DataSet newTrainingData, DataSet testingData){
		
		trainingData=newTrainingData.getCopy();//Stores a copy of the training data
		
		DataSet results=new DataSet();
		results=testingData.getCopy();
		
		/**We will start by estimating the probabilities for each output class*/
		double [] probabilitiesOfClasses=new double [testingData.getOutputDim()];
		for (int i=0; i<testingData.getOutputDim(); i++){
			probabilitiesOfClasses[i]=(double)countOccurrencesOfClass(i)/trainingData.getSize();
		}
		
		/**Following the definition of a naive bayes classifier, We will iterate over all 
		 * testing data. For each tuple we will find, according to trainingData, which is the
		 * most likely output class. This will be our best guess, and we will store it as the
		 * classification for this point. */
		for (int i=0; i<testingData.getSize(); i++){
				double pmax=0;
				int bestGuess=0;
				for(int j=0;j<trainingData.getOutputDim();j++){//Iterating over all output classes
					double p=1.0;
					for(int k=0;k<trainingData.getDim();k++){ //Iterating over all attributes in a tuple
						p=(double)p*(double)returnProbability(k,testingData.getTuple(i),j);
					}
					p*=(double)probabilitiesOfClasses[j];
					if(p>pmax){
						pmax=p;
						bestGuess=j;
					}
				}
				results.changeOutputOfTuple(i, bestGuess);//We classify this point, according to our best guess
		}
		return results;
	}
	
	/**
	 * Function that aids in calculating the probability of a Naive Bayes classifier. 
	 *
	 * @param attributeToEvaluate
	 * @param point (tuple)
	 * @param outputCategory
	 * 
	 * @author Pablo
	 */
	private double returnProbability(int attributeToEvaluate, DataPoint point, int outputCategory){
		double totalCasesInOutputCategory=0, totalCasesInOutputCategoryWithAttributeValueEqualToTheOneInPoint=0;
		for(int i=0;i<trainingData.getSize();i++){
			if(trainingData.getTuple(i).getOutput()==outputCategory){
				totalCasesInOutputCategory++;
				if(trainingData.getTuple(i).getValues()[attributeToEvaluate]==point.getValue(attributeToEvaluate)){
					totalCasesInOutputCategoryWithAttributeValueEqualToTheOneInPoint++;
				}
			}
		}
		if(totalCasesInOutputCategory==0){
			return 0;
		}
		return (double)totalCasesInOutputCategoryWithAttributeValueEqualToTheOneInPoint/totalCasesInOutputCategory;
	}
	
	/**
	 * Function that counts the occurrences of a given class in the training data. 
	 *
	 * @param classNumber
	 * 
	 * @author Gabriel
	 */
	private int countOccurrencesOfClass(int classNumber){
		int result=0;
		for (int i=0; i<trainingData.getSize(); i++){
			if (trainingData.getTuple(i).getOutput()==classNumber){
				result++;
			}
		}
		return result;
	}
}