package MN;

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

	public double getPotential() {
		return potential;
	}

	public void setPotential(double potential) {
		this.potential = potential;
	}
	

	
	
}
