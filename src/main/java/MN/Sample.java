package MN;

import java.util.Hashtable;
import java.util.Set;

public class Sample {

	// TODO: will contain the results of a Gibbs sampling run
	// This will (directly or indirectly) need to be able to be fed into our fitness function
	// will we be using an index or ID or something for nodes? I don't freaking know
	
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
	
}
