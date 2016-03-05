package MN;

import java.util.ArrayList;

public class Node {
	
	ArrayList<Node> neighbors; // TODO: this is also the MB. Will need to build this
	double[] vals; // values this Node can take on (discrete problem)
	
	// TODO: need some way to indicate the variable in this...
	
	/**
	 * Creates a new Node
	 * @param vals the values this Node's variable can take on (must be a discrete problem, i.e. finite size)
	 */
	public Node(double[] vals){
		this.vals = vals;
		neighbors = new ArrayList<Node>();
	}
	
	/**
	 * Adds a neighbor to this node (must create Edge separately)
	 */
	public void addNeighbor(Node n){
		neighbors.add(n);
	}
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}

}
