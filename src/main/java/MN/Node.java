package MN;

import java.util.ArrayList;

public class Node {
	
	private ArrayList<Node> MB; // TODO: Will need to build this
	private int[] vals; // values this Node can take on (discrete problem)
	private String name;
		
	/**
	 * Creates a new Node
	 * @param vals the values this Node's variable can take on (must be a discrete problem, i.e. finite size)
	 */
	public Node(int[] vals, String name){
		this.vals = vals;
		MB = new ArrayList<Node>();
		this.name = name;
	}
	
	/**
	 * Adds a neighbor to this node (must create Edge separately)
	 */
	public void addNeighbor(Node n){
		MB.add(n);
	}
	
	public ArrayList<Node> getMB() {
		return MB;
	}

	public void setMB(ArrayList<Node> neighbors) {
		this.MB = neighbors;
	}

	public int[] getVals() {
		return vals;
	}

	public int getSpecificVal(int i) {
		return vals[i];
	}
	
	public void setVals(int[] vals) {
		this.vals = vals;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node copy() {
		// TODO remember, will never actually need the MB again
		Node cp = new Node(this.vals, this.name);
		return cp;
	}

}
