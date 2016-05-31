package MN;

import java.util.ArrayList;

public class Clique {

	ArrayList<Node> nodes;
	// TODO: will also need potential
	
	/**
	 * Creates a new clique consisting of these nodes
	 * @param nodes
	 */
	public Clique (ArrayList<Node> nodes){
		this.nodes = new ArrayList<Node>();
		this.nodes.addAll(nodes);
	}
	
	public void print(){
		String s = "(";
		for (Node n : nodes){
			s += (" " + n.getName() + " ");
		}
		s += ")";
		System.out.println(s);
	}
}
