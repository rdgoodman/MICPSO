package MN;

import java.util.ArrayList;
import java.util.Arrays;

public class Clique {

	ArrayList<Node> nodes;
	CliqueFactorEntry[] factor;
	// TODO: need velocity

	/**
	 * Creates a new clique consisting of these nodes
	 * 
	 * @param nodes
	 */
	public Clique(ArrayList<Node> nodes) {
		this.nodes = new ArrayList<Node>();
		this.nodes.addAll(nodes);
		
		createFactors();
	}

	/**
	 * Creates the factor potential for this clique
	 */
	private void createFactors() {
//		System.out.println("\n >>> Clique: ");
//		print();
		
		Node[] allNodes = new Node[nodes.size()];
		nodes.toArray(allNodes);
		
		ArrayList<int[]> allCombos = new ArrayList<int[]>();

		int[][] allVals = new int[nodes.size()][];
		for (int i = 0; i < allVals.length; i++) {
			allVals[i] = nodes.get(i).getVals();
		}

		getAllCombinations(allVals, 0, new int[0], allCombos);
		
		factor = new CliqueFactorEntry[allCombos.size()];
		
		//System.out.println("combinations:");
		for (int i = 0; i < allCombos.size(); i++){
			factor[i] = new CliqueFactorEntry(allNodes, allCombos.get(i));
			//System.out.println(Arrays.toString(allCombos.get(i)));
		}
	}
	
	/**
	 * Returns the factor potential's value corresponding to this assignment to the nodes
	 * @param nodes
	 * @param values
	 * @return
	 */
	public double getPotentialCorrespondingToAssignment(Node[] sNodes, int[] sValues){
		for (int i = 0; i < factor.length; i++){
			if (factor[i].correspondsToAssignment(sNodes, sValues)){
				System.out.println("Potential corresponding to this assignment: " + factor[i].getPotential());
				return factor[i].getPotential();
			}
		}
		return 0;
	}


	/**
	 * Creates all combinations of the values of the nodes (recursive)
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
			int[] newPrefix = Arrays.copyOfRange(prefix,0,prefix.length+1);
			newPrefix[newPrefix.length-1] = i;
			getAllCombinations(allVals, n + 1, newPrefix, allCombos);
		}
	}


	public void adjustPotentials(Sample s, double epsilon) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Returns true if this clique includes this node
	 * @param n
	 * @return
	 */
	public boolean includes(Node n){
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

	public Node[] getNodesAsArray() {
		Node[] array = nodes.toArray(new Node[nodes.size()]);
		return array;
	}
}
