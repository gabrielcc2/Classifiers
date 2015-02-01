package ml.model;
/**
 * 
 * @summary Implementation of a ClusteredDataPoint. 
 *  It extends the DataPoint model, by including the distance to another point.
 *  It can be extended to include additional data useful for clustering.
 *    
 * 
 */
public class ClusteredDataPoint extends DataPoint {
	private double distance;
	
	/**Parametric constructor 
	 * 
	 * @param newValues: array with values
	 * @param dim: size of newValues array
	 * @param newOutput
	 * @param newDistance
	 * 
	 * */
	public ClusteredDataPoint (int [] newValues, int dim, int newOutput, double newDistance){
		this.setValues(newValues, dim);
		this.setOutput(newOutput);
		this.distance=newDistance;
	}
	
	public double getDistance(){
		return distance;
	}

	public void setDistance(double newDistance){
		this.distance=newDistance;
	}
	
	
}
