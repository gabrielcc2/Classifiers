package ml.control;

import ml.model.DataSet;

/**
 * 
 * @summary A classifier object, standing for a super-class, which can be specialized as others, such as
 * 			Naive Bayes Classifier.
 * 
 *  		Sub-classes are expected to override the classify function in this class, which only returns
 *  		the testingData itself.
 * 
 */

public class Classifier {
	
	/**
	 * The main method to classify data, returning the results.
	 *
	 * @param trainingData
	 * @param testingData
	 * 
	 */
	public DataSet classify (DataSet trainingData, DataSet testingData){
		return testingData;
	}
}
