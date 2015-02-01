package ml.model;

/**
 * 
 * @summary Implementation of a DataPoint, distinguishing its values as
 * an array of integers, from its output, as an integer apart.
 * 
 *  These numbers themselves represent the position of values in a CategoricalType
 *  representation of the data. In this way, they are a complement to that class.
 *  
 * 
 */


public class DataPoint {
	private int output=-1;
	private int [] values;
	private int id;

	public DataPoint(){
		
	}
	
	/**Parametric constructor 
	 * 
	 * @param newValues: array with values
	 * @param dim: size of newValues array
	 * @param newOutput
	 * 
	 * */
	public DataPoint (int [] newValues, int dim, int newOutput){
		this.setValues(newValues, dim);
		this.setOutput(newOutput);
	}
	
	public Integer getOutput(){
		return output;
	}
	
	public void setOutput(int output){
		this.output=output;
	}
	
	public int [] getValues(){
		return values;
	}
	
	public Integer getValue(int pos){//Returns the value in a specific position.
		return values[pos];
	}
	
	public void setValues (int[] newValues, int dim){
		values=new int [dim];
		values=newValues.clone();
	}
	
	public int getId(){
		return id;
	}
	public void setId(int newId){
		this.id=newId;
	}
}