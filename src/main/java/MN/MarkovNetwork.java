package MN;

import java.util.ArrayList;

public class MarkovNetwork {

	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;

	public MarkovNetwork() {
		// TODO: read in from file
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}

	/**
	 * Use this to create the network structure (known) for the network
	 */
	private void createNetworkStructure() {
		// TODO: finish this

		// step 1: create nodes

		// step 2: create edges

		// step 3: ...with any luck, we can have the Node class keep track of
		// its
		// own Markov blanket
	}

	/**
	 * Returns the result of Gibbs sampling this distribution
	 * 
	 * @return
	 */
	private Sample Sample() {

		Sample sample = new Sample(this);

		// 1) generate an initial sample (probably randomly from vals(Vars)
		for (Node n : nodes) {
			// TODO: chosen uniformly - does that work?
			double[] vals = n.getVals();
			double r = Math.random();
			double interval = 1 / vals.length;

			// TODO: holy guacamole, test this
			int counter = 0;
			for (double i = 0; i <= 1; i += interval) {
				if (r < i) {
					sample.setInitialValue(n, vals[counter]);
					break;
				}
				counter++;
			}
		}

		// 2) For each non-evidence variable (so, all of them) [order doesn't
		// matter]...

		// 3) Pick a variable, X

		// 4) Calculate P(X|MB(X)) using current values for MB(X)

		// 5) Resample value of X using the above distribution

		// 5a) For each value in vals(X), calculate basically the clique
		// potential
		// (in red on notes from 3/6)

		// 5b) Normalize results

		// 5c) Sample

		// end for

		// TODO: stub
		return null;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
}
