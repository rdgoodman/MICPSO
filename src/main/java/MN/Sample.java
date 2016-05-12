package MN;

import java.util.Hashtable;
import java.util.Set;

public class Sample implements Comparable{
	
	private Hashtable<Node, Integer> values;
	private double fitness;
	
	/**
	 * For MICPSO
	 * @param mn the Markov Network generating this sample
	 */
	public Sample(MarkovNetwork mn){
		values = new Hashtable<Node, Integer>();
	}
	
	/**
	 * For ICPSO
	 */
	public Sample(){
		values = new Hashtable<Node, Integer>();
	}
	
	/**
	 * Sets the value for a node's variable
	 * @param n Node which has been sampled
	 * @param value Value sampled for the node's variable
	 */
	public void setSampledValue(Node n, Integer value){	
		values.put(n, value);
	}
	
	/**
	 * Returns the value assigned to Node n
	 * @param n
	 * @return
	 */
	protected int getValue(Node n){
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
	
	public Hashtable<Node, Integer> getTable(){
		return values;
	}

	@Override
	public int compareTo(Object o) {
		if (o.equals(null)){
			return 0;
		} else {
			return Double.compare(this.getFitness(), ((Sample) o).getFitness());
		}		
	}
	
}
