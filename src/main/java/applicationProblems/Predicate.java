package applicationProblems;

import java.util.ArrayList;
import java.util.Hashtable;

import MN.Node;
import MN.Sample;

public class Predicate {
	
	private ArrayList<Node> nonNegatedNodes; // the non-negated variables in this predicate
	private ArrayList<Node> negatedNodes; // the negated variables in this predicate
	
	/**
	 * Encodes a logical predicate as a set of "OR"ed variables, negated or otherwise
	 * @param positive the non-negated variables in the predicate
	 * @param negated the negated variables in the predicate
	 */
	public Predicate(ArrayList<Node> positive, ArrayList<Node> negated){
		nonNegatedNodes = positive;
		negatedNodes = negated;
	}
	
	/**
	 * Returns true if the predicate is satisfied, false otherwise
	 * @return
	 */
	public boolean isSatisfied(Sample s){
		Hashtable<Node, Integer> ht = s.getTable();
		
		// look for a TRUE in the non-negated nodes
		for (Node n : nonNegatedNodes){
			if (ht.get(n) == 1){
				return true;
			}
		}
		
		// look for a FALSE in the negated nodes
		for (Node n : negatedNodes){
			if (ht.get(n) == 0){
				return true;
			}
		}
		
		return false;
	}

//	/**
//	 * Flips the truth assignment to this Node's variable
//	 * @param n
//	 */
//	public void flipValue(Node n){
//		if (nonNegatedNodes.contains(n)){
//			nonNegatedNodes.remove(n);
//			negatedNodes.add(n);
//		} else if (negatedNodes.contains(n)){
//			negatedNodes.add(n);
//			nonNegatedNodes.add(n);
//		}
//		// do nothing if that variable isn't in this predicate
//	}
	
	/**
	 * Returns a deep copy of this predicate
	 * @return
	 */
	public Predicate copy(){
		ArrayList<Node> newP = new ArrayList<Node>();
		ArrayList<Node> newN = new ArrayList<Node>();
		
		for (Node n : nonNegatedNodes){
			newP.add(n.copy());
		}
		
		for (Node n : negatedNodes){
			newN.add(n.copy());
		}
		
		return new Predicate(newP, newN);
	}
}
