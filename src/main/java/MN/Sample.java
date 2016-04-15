package MN;

import java.util.Hashtable;
import java.util.Set;

public class Sample {
	
	private Hashtable<Node, Double> values;
	private double fitness;
	
	/**
	 * For MICPSO
	 * @param mn the Markov Network generating this sample
	 */
	public Sample(MarkovNetwork mn){
		values = new Hashtable<Node, Double>();
	}
	
	/**
	 * For ICPSO
	 */
	public Sample(){
		values = new Hashtable<Node, Double>();
	}
	
	/**
	 * Sets the value for a node's variable
	 * @param n Node which has been sampled
	 * @param value Value sampled for the node's variable
	 */
	public void setSampledValue(Node n, Double value){	
		values.put(n, value);
	}
	
	/**
	 * Returns the value assigned to Node n
	 * @param n
	 * @return
	 */
	protected double getValue(Node n){
		if (!values.containsKey(n))
			System.out.println("Could not find key");
		return values.get(n);
	}
	
	public void print(){
        Set<Node> keys = values.keySet();
        for(Node key : keys){
            System.out.println(key.getName() + ": " + values.get(key));
        }
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public Hashtable<Node, Double> getTable(){
		return values;
	}
	
}
