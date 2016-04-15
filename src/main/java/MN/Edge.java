package MN;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class Edge {

	private LinkedList<Node> endpoints;
	private FactorEntry[] factors; // potentials
	private double[] velocity; // velocity vector

	/**
	 * Creates an edge between two nodes (order doesn't actually matter)
	 * 
	 * @param start
	 * @param end
	 */
	public Edge(Node endpoint1, Node endpoint2) {
		endpoints = new LinkedList<Node>();
		endpoints.add(endpoint1);
		endpoints.add(endpoint2);
		initializeFactors(endpoint1.getVals().length * endpoint2.getVals().length);
	}

	/**
	 * Initializes the factor potential table and velocities. Both are randomly
	 * initialized!
	 * 
	 * @param size
	 *            the number of potentials in the table
	 */
	private void initializeFactors(int size) {
		factors = new FactorEntry[size];
		velocity = new double[size];

		int counter = 0;

		// creates a new potential for each combination of start and end node
		// vals
		// and a new velocity entry to each as well
		for (int s = 0; s < endpoints.getFirst().getVals().length; s++) {
			for (int e = 0; e < endpoints.getLast().getVals().length; e++) {
				factors[counter] = new FactorEntry(endpoints.getFirst(), endpoints.getLast(),
						endpoints.getFirst().getVals()[s], endpoints.getLast().getVals()[e]);
				velocity[counter] = Math.random();
				counter++;
			}
		}
	}

	/**
	 * Adds the velocity vector to each potential value
	 */
	public void updateFactorPotentials() {
		for (int i = 0; i < factors.length; i++) {
			factors[i].setPotential(factors[i].getPotential() + velocity[i]);
		}
	}

	/**
	 * Returns the factor associated with a particular assignment to both
	 * endpoints
	 * 
	 * @param n1
	 * @param valN1
	 * @param n2
	 * @param valN2
	 * @return
	 */
	protected double getPotential(Node n1, double valN1, Node n2, double valN2) {
		// search through factorentry[]
		for (FactorEntry f : factors) {
			if (f.getValue(n1) == valN1 && f.getValue(n2) == valN2) {
				return f.getPotential();
			}
		}
		// TODO: throw exception
		return 0.0;
	}

	/**
	 * Returns true if the given node is an endpoint for this Edge
	 */
	public Boolean hasEndpoint(Node n) {
		if (endpoints.contains(n)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Create a deep copy of this edge
	 */
	public Edge copy(){
		Node A = endpoints.getFirst().copy();
		Node B = endpoints.getLast().copy();
		Edge e = new Edge(A, B);
		
		// TODO: USE THE NODES FROM THESE COPIED EDGES TO BUILD NODEARRAY
		
		FactorEntry[] f = new FactorEntry[factors.length];
		for(int i = 0; i < f.length; i++){
			// copy all the FactorEntries
			f[i] = factors[i].copy(e.getEndpoints().getFirst(), e.getEndpoints().getLast());
		}
		e.setFactors(f);
	
		// don't need to copy velocity because copy's position will never be updated again		
		return e;
	}
	

	public void handleGCConstraints() {
		for(int i = 0; i < factors.length; i++){
			// for graph-coloring problems, adjacent vertices
			// cannot have the same color
			if (factors[i].getValA() == factors[i].getValB()){
				factors[i].setPotential(0.0);
			}
		}
	}

	/**
	 * Carries out adjustment using scaling factor
	 */
	public void adjustPotentials(Sample s, double epsilon) {
		// adjust each factor potential
		double delta = 0;
		int kIndex = 0;
		// unequal to valK
		for (int i = 0; i < factors.length; i++) {
			if (factors[i].getValue(endpoints.getFirst()) != s.getValue(endpoints.getFirst()) || 
					factors[i].getValue(endpoints.getLast()) != s.getValue(endpoints.getLast()) ){
				factors[i].setPotential(factors[i].getPotential() * epsilon);
				// update delta
				delta += (factors[i].getPotential() - factors[i].getPotential() * epsilon);

			} else {
				// TODO: check this only happens once
				kIndex = i;
			}
		}
		// equal to valK
		// adjust probability that corresponds to sample value
		factors[kIndex].setPotential(factors[kIndex].getPotential() + delta);
	}

	/**
	 * Prints the factor potentials
	 */
	public void printFactors() {
		System.out.println("phi[" + endpoints.getFirst().getName() + ", " + endpoints.getLast().getName() + "]");
		System.out.println(endpoints.getFirst().getName() + "\t " + endpoints.getLast().getName() + "\t Factor");
		DecimalFormat fourd = new DecimalFormat("#.####");
		DecimalFormat oned = new DecimalFormat("#.####");

		int counter = 0;

		// creates a new potential for each combination of start and end node
		// vals
		for (int s = 0; s < endpoints.getFirst().getVals().length; s++) {
			for (int e = 0; e < endpoints.getLast().getVals().length; e++) {
				System.out.println(oned.format(endpoints.getFirst().getVals()[s]) + "\t "
						+ oned.format(endpoints.getLast().getVals()[e]) + "\t "
						+ fourd.format(factors[counter].getPotential()));
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

	public double[] getVelocity() {
		return velocity;
	}

	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	/**
	 * Returns an array consisting of all the factor entries in the edge's
	 * potential function
	 * 
	 * @return
	 */
	public double[] getAllEntries() {
		double[] e = new double[factors.length];
		for (int i = 0; i < factors.length; i++) {
			e[i] = factors[i].getPotential();
		}
		return e;
	}

	/**
	 * Returns an array consisting of all the factor entries in the edge's
	 * potential function
	 * 
	 * @return
	 */
	public void setEntries(double[] newEntries) {
		for (int i = 0; i < factors.length; i++) {
			factors[i].setPotential(newEntries[i]);
		}
	}

}
