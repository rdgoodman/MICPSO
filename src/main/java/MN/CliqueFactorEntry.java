package MN;

import java.text.DecimalFormat;

public class CliqueFactorEntry {

	private Node[] nodes;
	private int[] values;
	private double potential;
	
	public CliqueFactorEntry(Node[] nodes, int[] values){
		this.nodes = nodes;
		this.values = values;
		// TODO: might wanna rethink this random initialization, I guess
		this.potential = Math.random();
	}
	
	
	/**
	 * For use only with copy method
	 */
	public CliqueFactorEntry(Node[] nodes){
		this.nodes = nodes;
	}
	
	
	/**
	 * Basically a copy constructor with same potentials/vals but different nodes
	 * @param A
	 * @param B
	 */
	public CliqueFactorEntry copy(Node[] nodes){
		CliqueFactorEntry fe = new CliqueFactorEntry(nodes);
		fe.copyAndSetValues(values);
		fe.setPotential(potential);		
		return fe;
	}
	
	
	/**
	 * Creates a deep copy of the passed-in values array and sets that as this entry's values array
	 * @param oldValues
	 */
	private void copyAndSetValues(int[] oldValues) {
		values = new int[oldValues.length];
		for (int i = 0; i < values.length; i++){
			values[i] = oldValues[i];
		}
	}

	/**
	 * Gets the value of the specified node
	 */
	public int getValue(Node n){
		// get index of node
		int index = 0;
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].equals(n)){
				index = i;
			}
		}		
		// retrieve value at corresponding index
		return values[index];
	}
	
	/**
	 * Returns true if this factor corresponds to the given assignment to the nodes
	 * @param nodes
	 * @param values
	 * @return
	 */
	public boolean correspondsToAssignment(Node[] sNodes, int[] sValues){
		for (int i = 0; i < sNodes.length; i++){
			// get index in nodes of the sNode
			int valueIndex = 0;
			for (int j = 0; j < nodes.length; j++){
				if (sNodes[i].equals(nodes[j])){
					valueIndex = j;
					break;
				}
			}
			// if inputted value is not equal to stored value,
			// doesn't correspond to assignment
			if (sValues[i] != values[valueIndex]){
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if this factor corresponds to the sample's assignment to the nodes
	 * @param s
	 * @return
	 */
	public boolean correspondsToAssignment(Sample s) {
		
		for (int i = 0; i < nodes.length; i++){
			if (values[i] != s.getValue(nodes[i])){
				return false;
			}
		}
		return true;
	}


	public double getPotential() {
		return potential;
	}

	public void setPotential(double potential) {
		this.potential = potential;
	}


	public void print(DecimalFormat fourd) {
		String s = "";
		for (int v = 0; v < values.length; v++){
			s += (values[v] + "\t");
		}	
		s += fourd.format(potential);
		System.out.println(s);
	}


	public CliqueFactorEntry copy() {
		CliqueFactorEntry fe = new CliqueFactorEntry(nodes, values);
		fe.setPotential(potential);
		return fe;
	}
	
	
}
