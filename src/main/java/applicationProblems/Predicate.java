package applicationProblems;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import MN.Node;
import MN.Sample;

public class Predicate {
	
	private ArrayList<String> nonNegatedNodes; // names of non-negated variables in this predicate
	private ArrayList<String> negatedNodes; // names of negated variables in this predicate
	
	/**
	 * Encodes a logical predicate as a set of "OR"ed variables, negated or otherwise
	 * @param positive the non-negated variables in the predicate
	 * @param negated the negated variables in the predicate
	 */
	public Predicate(ArrayList<String> positive, ArrayList<String> negated){
		nonNegatedNodes = positive;
		negatedNodes = negated;
	}
	
	/**
	 * Returns true if the predicate is satisfied, false otherwise
	 * @return
	 */
	public boolean isSatisfied(Sample s){
		Hashtable<Node, Integer> ht = s.getTable();
		
		// build the lists of nodes from names
		Set<Node> keys =  ht.keySet();
		ArrayList<Node> N = new ArrayList<Node>();
		ArrayList<Node> P = new ArrayList<Node>();
		
		for (Node k : keys){
			// figure out whether this is positive or negative
			// (or neither) in this predicate
			if (nonNegatedNodes.contains(k.getName())){
				P.add(k);
			} else if (negatedNodes.contains(k.getName())){
				N.add(k);
			} 
		}
		
		// look for a TRUE in the non-negated nodes
		for (Node n : P){
			if (ht.get(n) == 1){
				return true;
			}
		}
		
		// look for a FALSE in the negated nodes
		for (Node n : N){
			if (ht.get(n) == 0){
				return true;
			}
		}
		
		return false;
	}

	
//	/**
//	 * Returns a deep copy of this predicate
//	 * @return
//	 */
//	public Predicate copy(){
//		ArrayList<Node> newP = new ArrayList<Node>();
//		ArrayList<Node> newN = new ArrayList<Node>();
//		
//		for (Node n : nonNegatedNodes){
//			newP.add(n.copy());
//		}
//		
//		for (Node n : negatedNodes){
//			newN.add(n.copy());
//		}
//		
//		return new Predicate(newP, newN);
//	}
	
	/**
	 * Returns a string representing this predicate
	 */
	public String toString(){
		String s = "(";
		for (String n : nonNegatedNodes){
			s += ( n + " v ");
		}
		
		for (String n : negatedNodes){
			s += ( "!" + n + " v ");
		}
		
		s += ") ";
		
		return s;
	}
}

