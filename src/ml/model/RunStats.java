package ml.model;


/**
 * 
 * @summary Object holding and calculating statistics over the data in a specific run.
 * 
 */

public class RunStats {
	private int [][] confusionMatrix;
	private int dimConfusionMatrix=0;
	private int numInstances=0;
	private int incorrectlyClassified=0;
	private int correctlyClassified=0;
	CategoricalType outputInfo= new CategoricalType("Output class");//Headers of the output class
	
	/**Calculates the run statistics given a DataSet with the resultingData and
	 * a DataSet with the expected data.*/
	public void calculateRunStats(DataSet resultingData, DataSet expectedData){
		/**First we initialize the headers*/		
		outputInfo= new CategoricalType(resultingData.getOutputInfo().getName());
		outputInfo.addAllCategories(resultingData.getOutputInfo().getAllCategories());
	
		/**We initialize the confusion matrix*/
		dimConfusionMatrix=resultingData.getOutputDim();
		numInstances=resultingData.getSize();
		confusionMatrix=new int [dimConfusionMatrix][dimConfusionMatrix];
		initConfusionMatrix();
		incorrectlyClassified=0;
		correctlyClassified=0;
		for (int i=0; i<numInstances; i++){
			int classifiedAs= resultingData.getOutputNum(i); 
			int reallyIs=expectedData.getOutputNum(i); 			
			confusionMatrix[classifiedAs][reallyIs]++;
			if (classifiedAs==reallyIs){
				correctlyClassified++;
			}
			else{
				incorrectlyClassified++;
			}
		}
	}
	
	/**Plots the confusion matrix*/
	public void plotConfusionMatrix(){
		System.out.println("ClassifiedAs | ReallyIs->");
		String auxString="*\t*";
		int [] totalsPerClass= new int[dimConfusionMatrix];
		for (int i=0; i<dimConfusionMatrix; i++){
			totalsPerClass[i]=0;
			auxString+=" "+ outputInfo.getCategory(i);
			auxString+="\t";
			auxString+="*";
		}
		System.out.println(auxString);
		
		for (int i=0; i<dimConfusionMatrix; i++){
			int counter=0;
			auxString="* "+outputInfo.getCategory(i)+"\t*";
			for (int j=0; j<dimConfusionMatrix; j++){
				totalsPerClass[j]+=confusionMatrix[i][j];
				auxString+=" "+confusionMatrix[i][j];
				counter+=confusionMatrix[i][j];
				auxString+="\t";
				auxString+="*";
			}
			auxString+=" Total classified as: "+outputInfo.getCategory(i)+": "+counter;
			System.out.println(auxString);
		}
		for (int i=0; i<dimConfusionMatrix; i++){
			auxString="Totals per class "+outputInfo.getCategory(i)+": "+totalsPerClass[i];
			System.out.println(auxString);
		}
		System.out.println("Total number of instances: "+numInstances);
		System.out.println("Number of correctly classified instances: "+correctlyClassified);
		System.out.println("Number of incorrectly classified instances: "+incorrectlyClassified);
		System.out.println("Error: "+this.getError());
	}
	
	public int getNumInstances(){
		return numInstances;
	}
	
	public int getCorrectlyClassified(){
		return correctlyClassified;
	}
	
	public int getIncorrectlyClassified(){
		return incorrectlyClassified;
	}

	public double getError(){
		if (numInstances==0){
			return 0;
		}
		return (double)incorrectlyClassified/numInstances;
	}
	
	public int [][] getConfusionMatrix(){
		return confusionMatrix;
	}
	
	public int getDimConfusionMatrix(){
		return dimConfusionMatrix;
	}
	
	/**Initializes the confusion matrix, filling it with zeros*/
	private void initConfusionMatrix(){
		for (int i=0; i<dimConfusionMatrix; i++){
			for (int j=0; j<dimConfusionMatrix; j++){
				confusionMatrix[i][j]=0;
			}
		}
	}
	
}
