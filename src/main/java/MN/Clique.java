package MN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Clique {

	private ArrayList<Node> nodes;
	private CliqueFactorEntry[] factor;
	private double[] velocity;

	/**
	 * Creates a new clique consisting of these nodes
	 * 
	 * @param nodes
	 */
	public Clique(ArrayList<Node> nodes) {
		this.nodes = new ArrayList<Node>();
		this.nodes.addAll(nodes);

		createFactors();
		initializeVelocity();
	}

	/**
	 * Initializes velocity vector
	 */
	private void initializeVelocity() {
		velocity = new double[factor.length];
		for (int i = 0; i < velocity.length; i++){
			velocity[i] = Math.random();
		}
	}

	/**
	 * Creates the factor potential for this clique
	 */
	private void createFactors() {
		// System.out.println("\n >>> Clique: ");
		// print();

		Node[] allNodes = new Node[nodes.size()];
		nodes.toArray(allNodes);
		
		

		ArrayList<int[]> allCombos = new ArrayList<int[]>();

		int[][] allVals = new int[nodes.size()][];
		for (int i = 0; i < allVals.length; i++) {
			allVals[i] = nodes.get(i).getVals();
		}

		getAllCombinations(allVals, 0, new int[0], allCombos);

		factor = new CliqueFactorEntry[allCombos.size()];

		// System.out.println("combinations:");
		for (int i = 0; i < allCombos.size(); i++) {
			factor[i] = new CliqueFactorEntry(allNodes, allCombos.get(i));
			// System.out.println(Arrays.toString(allCombos.get(i)));
		}
	}

	/**
	 * Returns the factor potential's value corresponding to this assignment to
	 * the nodes
	 * 
	 * @param nodes
	 * @param values
	 * @return
	 */
	public double getPotentialCorrespondingToAssignment(Node[] sNodes, int[] sValues) {
		for (int i = 0; i < factor.length; i++) {
			if (factor[i].correspondsToAssignment(sNodes, sValues)) {
				//System.out.println("Potential corresponding to this assignment: " + factor[i].getPotential());
				return factor[i].getPotential();
			}
		}
		return 0;
	}

	/**
	 * Creates all combinations of the values of the nodes (recursive)
	 * 
	 * @param allVals
	 * @param n
	 * @param prefix
	 */
	private void getAllCombinations(int[][] allVals, int n, int[] prefix, ArrayList<int[]> allCombos) {
		if (n >= allVals.length) {
			allCombos.add(prefix);
			return;
		}

		for (int i : allVals[n]) {
			int[] newPrefix = Arrays.copyOfRange(prefix, 0, prefix.length + 1);
			newPrefix[newPrefix.length - 1] = i;
			getAllCombinations(allVals, n + 1, newPrefix, allCombos);
		}
	}

	/**
	 * Adds the velocity vector to each potential value
	 */
	public void updateFactorPotentials() {
		for (int i = 0; i < factor.length; i++){
			factor[i].setPotential(factor[i].getPotential() + velocity[i]);
		}
	}

	/**
	 * Carries out adjustment using scaling factor
	 * @param s
	 * @param epsilon
	 */
	public void adjustPotentials(Sample s, double epsilon) {
		// adjust each factor potential
		double delta = 0;
		int kIndex = 0;
		// unequal to valK
		for (int i = 0; i < factor.length; i++) {
			if (!factor[i].correspondsToAssignment(s)) {
				factor[i].setPotential(factor[i].getPotential() * epsilon);
				// update delta
				delta += (factor[i].getPotential() - factor[i].getPotential() * epsilon);

			} else {
				// should only happen once
				kIndex = i;
			}
		}
		// equal to valK
		// adjust probability that corresponds to sample value
		factor[kIndex].setPotential(factor[kIndex].getPotential() + delta);
	}

	/**
	 * Returns true if this clique includes this node
	 * 
	 * @param n
	 * @return
	 */
	public boolean includes(Node n) {
		return nodes.contains(n);
	}

	public void print() {
		String s = "(";
		for (Node n : nodes) {
			s += (" " + n.getName() + " ");
		}
		s += ")";
		System.out.println(s);
	}
	
	public String toString(){
		String s = "(";
		for (Node n : nodes) {
			s += (" " + n.getName() + " ");
		}
		s += ")";
		return s;
	}

	public Node[] getNodesAsArray() {
		Node[] array = nodes.toArray(new Node[nodes.size()]);
		return array;
	}
	
	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Prints the factor potentials
	 */
	public void printFactors() {
				
		System.out.println("phi" + toString());
		
		String header = "";		
		for (Node n : nodes){
			header += n.getName() + "\t ";
		}
		header += "Factor";
		
		System.out.println(header);
		
		DecimalFormat fourd = new DecimalFormat("#.####");

		// for each factor
		for (int i = 0; i < factor.length; i++){
			factor[i].print(fourd);
		}
	}

	public Clique copy() {
		Clique c = new Clique(nodes);
		CliqueFactorEntry[] factorCopy = new CliqueFactorEntry[factor.length];
		for (int i = 0; i < factor.length; i++){
			factorCopy[i] = factor[i].copy();
		}		
		
		// don't need to copy velocity because copy's position will never be updated again		
		return c;
	}

	public double[] getVelocity() {
		return velocity;
	}

	public double[] getAllEntries() {
		double[] e = new double[factor.length];
		for (int i = 0; i < factor.length; i++) {
			e[i] = factor[i].getPotential();
		}
		return e;
	}
}
