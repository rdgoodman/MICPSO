package MN;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class Edge {

	private LinkedList<Node> endpoints;
	private FactorEntry[] factors; // potentials
	
	/**
	 * Creates an edge between two nodes (order doesn't actually matter)
	 * @param start
	 * @param end
	 */
	public Edge(Node endpoint1, Node endpoint2){
		endpoints = new LinkedList<Node>();
		endpoints.add(endpoint1);
		endpoints.add(endpoint2);
		initializeFactors(endpoint1.getVals().length * endpoint2.getVals().length);
	}

	/**
	 * Initializes the factor potential table. Potentials are randomly initialized!
	 * @param size the number of potentials in the table
	 */
	private void initializeFactors(int size) {
		factors = new FactorEntry[size];
		
		int counter = 0;
		
		// creates a new potential for each combination of start and end node vals
		for (int s = 0; s < endpoints.getFirst().getVals().length; s++){
			for (int e = 0; e < endpoints.getLast().getVals().length; e++){
				factors[counter] = new FactorEntry(endpoints.getFirst(), endpoints.getLast(), endpoints.getFirst().getVals()[s], endpoints.getLast().getVals()[e]);
				counter++;
			}
		}
	}
	
	/**
	 * Returns the factor associated with a particular assignment to both endpoints
	 * @param n1
	 * @param valN1
	 * @param n2
	 * @param valN2
	 * @return
	 */
	protected double getPotential(Node n1, double valN1, Node n2, double valN2){	
		// search through factorentry[]
		for (FactorEntry f : factors){
			if (f.getValue(n1) == valN1 && f.getValue(n2) == valN2){
				return f.getPotential();
			}
		}
		// TODO: throw exception
		return 0.0;
	}
	
	/**
	 * Returns true if the given node is an endpoint for this Edge
	 */
	public Boolean hasEndpoint(Node n){
		if (endpoints.contains(n)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Prints the factor potentials
	 */
	public void printFactors(){
		System.out.println("phi[" + endpoints.getFirst().getName() + ", " + endpoints.getLast().getName() + "]");
		System.out.println(endpoints.getFirst().getName() + "\t " + endpoints.getLast().getName() + "\t Factor");
		DecimalFormat fourd = new DecimalFormat("#.####");
		DecimalFormat oned = new DecimalFormat("#.####");

		
		int counter = 0;
		
		// creates a new potential for each combination of start and end node vals
		for (int s = 0; s < endpoints.getFirst().getVals().length; s++){
			for (int e = 0; e < endpoints.getLast().getVals().length; e++){
				System.out.println(oned.format(endpoints.getFirst().getVals()[s]) + "\t " + oned.format(endpoints.getLast().getVals()[e]) + "\t " + fourd.format(factors[counter].getPotential()));
				counter++;
			}
		}
		
	}

	public LinkedList<Node> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(LinkedList<Node> endpoints) {
		this.endpoints = endpoints;
	}

	public FactorEntry[] getFactors() {
		return factors;
	}

	public void setFactors(FactorEntry[] factors) {
		this.factors = factors;
	}

}
