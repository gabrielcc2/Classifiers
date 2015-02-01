package ml.model;

import java.util.ArrayList;
import java.util.List;

/*
 * CLASS CategoricalType
 * 	In this class the definition of a CategoricalType is implemented. This refers to types of data
 *  that are like Enums, having an attribute name, and a discrete set of possible values, which are defined as strings.
 *  This class stores the name of the attribute and such values, additionally it provides some functions to
 *  ease the use of these types.
 *   
 *  ATTRIBUTES
 *  String name: Name of the attribute or class
 *  List <String> categories: List with possible values of attributes, stored as strings.
 *  
 *  FUNCTIONS
 *  public String getName(): Returns name of attribute
 *  public int setName(String ): Sets name of attribute, taking as input such a name. Returns 1 always.
 *  public int addCategory(String ): Adds a new category, or possible value of attribute (used on build time). The name of the category is passed as input. Returns 1 always.
 *  public int addAllCategories(List<String> ): Adds new categories, or possible value of attribute (used on build time)
 *  public List<String> getAllCategories(): Returns all categories.
 *  public int getNumCategories(): Returns number of categories or possible values of attribute. 
 *  public String getCategory(int ): Maps between a number (or index) and the value of attribute (a string). 
 *  public int getNum(String ): Maps between the value of attribute (a string) and the number (or index) it represents
 *  public boolean isUsed(): Checks if the attribute has been used in learning a decision tree
 *  
 *  public String toString (): Useful for debugging, Returns a string of the form: AttrName:PossibleValue1,PossibleValue2,...,PossibleValueN
 *  
 *  AUTHOR: 
 *  Gabriel Campero, gabrielcampero@acm.org
 *  
 *  FROM THE ID3 ML HOMEWORK, WISE 2014-2015, MSc DKE, OvGU.
 *  */

public class CategoricalType {
	
	/////ATTRIBUTES
	private String name; //Name of the attribute or class
	private List <String> categories = new ArrayList<String>(); //List with possible values
	
	/////FUNCTIONS
	public CategoricalType(String newName){//Parametrical constructor
		name=newName;
	}
	
	public String getName(){//Returns name of attribute
		return name;
	}
	
	public int setName(String newName){//Sets name of attribute
		name=newName;
		return 1;
	}
	
	public int addCategory(String newCategory){//Adds a new category, or possible value of attribute (used on build time)
		categories.add(newCategory);
		return 1;
	}
	
	public int addAllCategories(List<String> newCategories){//Adds new categories, or possible value of attribute (used on build time)
		categories.clear();
		categories.addAll(newCategories);
		return 1;
	}
	
	public int getNumCategories(){//Returns number of categories or possible values of attribute
		return categories.size();
	}
	
	public String getCategory(int num){ //Maps between numbers and the value of attribute
		return categories.get(num);
	}
	
	public List<String> getAllCategories(){//Returns all categories
		return categories;
	}
	
	public int getNum(String searchCategory){ //Maps between the value of attribute and the number it represents
		return categories.indexOf(searchCategory); 
	}
	
	public String toString (){ //Useful for debugging. Returns a string of the form: AttrName:PossibleValue1,PossibleValue2,...,PossibleValueN
		String outputString=name+":";
		for (int i=0; i<categories.size(); i++){
			outputString+=categories.get(i);
			if (i<categories.size()-1){
				outputString+=",";
			}
		}
		return outputString;
	}
}
