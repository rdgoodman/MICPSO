package MN;

import java.util.ArrayList;

public class MarkovNetwork {
	
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	public MarkovNetwork(){
		// TODO: read in from file
	}
	
	
	/**
	 * Use this to create the network structure (known) for the network
	 */
	private void createNetworkStructure(){
		// TODO: finish this
		
		// step 1: create nodes
		
		// step 2: create edges 
		
		// step 3: ...with any luck, we can have the Node class keep track of its 
		// own Markov blanket		
	}

	/**
	 * Returns the result of Gibbs sampling this distribution
	 * @return
	 */
	private Sample Sample(){
		// TODO: stub
		
		// 1) generate an initial sample (probably randomly from vals(Vars)
		
		// 2) For some number of iterations (I think)...
		
		// 3) For each non-evidence variable (so, all of them) [order doesn't matter]...
		
		// 4) Pick a variable, X
		
		// 5) Calculate P(X|MB(X)) using current values for MB(X)
		
		// 6) Resample value of X using the above distribution
		
		// 6a) For each value in vals(X), calculate basically the clique potential
		// (in red on notes from 3/6)
		
		// 6b) Normalize results
		
		// 6c) Sample
		
		// end for
		
		
		return null;
	}
}
