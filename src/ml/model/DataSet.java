package ml.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @summary Object easing management of data that can be internally represented by CategoricalTypes
 * and DataPoints.
 * 
 */

public class DataSet {
	int numTuples=0;
	/**Headers allowing mapping from DataPoints to CategoricalTypes, or from integers to Strings*/
	List<CategoricalType> info = new ArrayList<CategoricalType>();//Includes the headers
	CategoricalType outputInfo= new CategoricalType("Output class");
	
	/**Tuples as DataPoints*/
	List<DataPoint> data=new ArrayList<DataPoint>(); //Masks an array of ints, for each attribute; and the output.
	
	
	/**Default constructor*/
	public DataSet(){
	}

	/**Returns number of tuples of the DataSet*/
	public int getSize (){
		return data.size();
	}
	
	/**Returns number of attributes or dimension of the DataSet*/
	public int getDim (){
		return info.size();
	}	
	
	/**Allows to map a string representing a specific value of an attribute, to the number that indexes it in 
	 * the object.*/
	public int getInfoValue(int dim, String name){
		return info.get(dim).getNum(name);
	}

	/**Allows to map an integer representing a specific value of an attribute within the object, to the string that 
	 * it was originally.
	 * */
	public String getCategory(int dim, int pos){
		return info.get(dim).getCategory(pos);
	}

	/**Returns a CategoricalType standing as headers of the output (names of classes)*/
	public CategoricalType getOutputInfo(){
		return outputInfo;
	}

	/**Returns the number for a specific string value of an output class*/
	public int getOutputNum(String name){
		return outputInfo.getNum(name);
	}
	
	/**Returns the string value of an output class, given a number representing it*/
	public String getOutputName(int num){
		return outputInfo.getCategory(num);
	}

	/**Returns the number of output classes*/
	public int getOutputDim(){
		return outputInfo.getNumCategories();
	}

	/**Changes the name of the output as a class (by default the name is "Output class")*/
	public void setOutputName(String name){
		outputInfo.setName(name);
	}
	
	/**Adds an output category*/
	public void addOutputCategory(String name){
		outputInfo.addCategory(name);
	}

	/**Returns a tuple given its position*/
	public DataPoint getTuple (int pos){
		return data.get(pos);
	}
	
	/**Returns the values of the tuple as an array of strings, without its output value*/
	public String[] getTupleValues(int pos){
		int dim= info.size();
		String [] results= new String[dim];
		for (int i=0; i<dim; i++){
			results[i]=info.get(i).getCategory(data.get(pos).getValue(i));
		}
		return results;
	}
	
	/**Returns the values of the tuple as an array of integers, without its output value*/
	public int[] getTupleValuesAsIntegers(int pos){
		int dim= info.size();
		int [] results= new int[dim];
		for (int i=0; i<dim; i++){
			results[i]=data.get(pos).getValue(0);
		}
		return results;
	}
	
	/**Returns the output value of a tuple as a string*/
	public String getOutputValue(int pos){
		return outputInfo.getCategory(data.get(pos).getOutput());
	}
	
	/**Returns the output value of a tuple as a number*/
	public int getOutputNum(int pos){
		return getOutputNum(getOutputValue(pos));
	}
	
	/** Adds a tuple and its output*/
	public void addTuple(int[] newValues, int newOutput){
		data.add(new DataPoint(newValues, info.size(), newOutput));
		numTuples++;
	}
	
	public void setTupleId(int pos, int newId){
		data.get(pos).setId(newId);
	}
	/**Adds output info (the name of output class and a list of possible classes)*/
	public void addOutputInfo (String name, List<String> values){
		outputInfo=new CategoricalType(name);
		outputInfo.addAllCategories(values);
	}
	
	
	/**Adds info for attributes (the name of an attribute and a list of possible values)*/
	public void addInfo (String name, List<String> values){//For adding a set of attributes, must be called one by one
		info.add(new CategoricalType(name));
		info.get(info.size()-1).addAllCategories(values);
	}
	
	/**Allows to change the output value of a tuple, to a new value*/
	public void changeOutputOfTuple(int pos, int newVal){
		data.get(pos).setOutput(newVal);
	}
	
	/**Shuffles the data points randomly, using java.util.Collections*/
	public void shufflePoints(){
		Collections.shuffle(data);
		Collections.shuffle(data);
		Collections.shuffle(data);
	}
	
	/**Returns a copy of the DataSet*/
	public DataSet getCopy(){
		DataSet result= new DataSet();
		for (int i=0; i<data.size(); i++){
			result.addTuple(data.get(i).getValues(), data.get(i).getOutput());
		}
		for (int i=0; i<info.size(); i++){
			result.addInfo(info.get(i).getName(), info.get(i).getAllCategories());
		}
		result.addOutputInfo(outputInfo.getName(), outputInfo.getAllCategories());
		return result;
	}

	/**Returns a partial copy of the DataSet, covering a percentage passed as input*/
	public DataSet getCopyOfFirstChunk(double percentage){
		DataSet result= new DataSet();
		for (int i=0; i<(int)Math.floor(percentage*(data.size())); i++){
			result.addTuple(data.get(i).getValues(), data.get(i).getOutput());
		}
		for (int i=0; i<info.size(); i++){
			result.addInfo(info.get(i).getName(), info.get(i).getAllCategories());
		}
		result.addOutputInfo(outputInfo.getName(), outputInfo.getAllCategories());
		return result;
	}

	/**Returns a partial copy of the DataSet, covering what remains from getCopyOfFirstChunk*/
	public DataSet getCopyOfLastChunk(double percentage){
		DataSet result= new DataSet();
		for (int i=(int)Math.floor(percentage*(data.size())); i<data.size(); i++){
			result.addTuple(data.get(i).getValues(), data.get(i).getOutput());
		}
		for (int i=0; i<info.size(); i++){
			result.addInfo(info.get(i).getName(), info.get(i).getAllCategories());
		}
		result.addOutputInfo(outputInfo.getName(), outputInfo.getAllCategories());
		return result;
	}
}