package MN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;
import applicationProblems.MaxSatProblem;
import applicationProblems.Predicate;

public class CliqueMarkovNetwork implements MarkovNetwork {
	// Array list for storing nodes and edges
	private ArrayList<Node> nodesArray = new ArrayList<Node>();
	private ArrayList<Edge> edgesArray = new ArrayList<Edge>();
	// TODO: in this network, edges are only for constraint-checking
	// as the potentials live elsewhere
	private ArrayList<Clique> maxCliques = new ArrayList<Clique>();

	// Variables for storing the string values read in from file
	String problemType = "";
	int optimalNo = 0;
	String[] stringNodes = null;
	String[] stringEdges = null;
	String[] stringValues = null;

	ApplicationProblem problem;

	// number of runs for Gibbs sampling
	// ultimately this should be tunable
	int runs = 2;
	// TODO: change back to 100

	/**
	 * Constructor when read in from file
	 * 
	 * @param inputFile
	 * @throws FileNotFoundException
	 */
	public CliqueMarkovNetwork(String inputFile) throws FileNotFoundException {

		// The scanner for reading in the Markov net file
		Scanner s = null;

		// The entire file name (passed in from RunModels),
		// for retrieving the Markov net file
		File file = new File(inputFile);

		// Reads in the nodes, edges and values in from a specifically formatted
		// file
		try {
			s = new Scanner(new BufferedReader(new FileReader(file)));

			String tempVal;

			// Read the first line in the file
			tempVal = s.nextLine();

			// gets the type of problem first
			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = s.nextLine();
			}

			problemType = tempVal;

			// read in the problem
			if (problemType.equals("GC")) {
				readGCProblemFromFile(s);
				// creates the Markov network
				createNetworkStructure();
			} else if (problemType.equals("MS")) {
				readMaxSatProblemFromFile(s);
				// MN structure automatically created by this function too
			}

		} finally {
			if (s != null) {
				s.close();
			}
		}

		// creates the list of cliques
		ArrayList<Node> R = new ArrayList<Node>();
		ArrayList<Node> X = new ArrayList<Node>();
		ArrayList<Node> P = new ArrayList<Node>();
		P.addAll(nodesArray);
		bronKerbosch(R, P, X);
	}

	public CliqueMarkovNetwork(String problemType2, int optimalNo2, int runs2) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Reads in a MAXSAT problem from a specially formatted file
	 * 
	 * @param s
	 */
	private void readMaxSatProblemFromFile(Scanner s) {
		String tempVal = null;
		String allClauses = "";
		ArrayList<Predicate> predicates = new ArrayList<Predicate>();

		// keep scanning for the next non-empty line
		if (s.nextLine().equals("")) {
			tempVal = s.nextLine();
		}

		// checks for comments, when present, discards them
		while (tempVal.startsWith("c")) {
			tempVal = s.nextLine();
		}

		// reads preamble
		if (tempVal.startsWith("p")) {
			// split on spaces
			String[] preamble = tempVal.split("\\s+");
			// error-check
			if (!preamble[1].equals("cnf")) {
				throw new RuntimeException("Not in CNF, can't read file");
			}
			// read number of variables
			int numNodes = Integer.valueOf(preamble[2]);

			// create correct number of nodes
			for (int i = 1; i <= numNodes; i++) {
				int[] binaryVals = { 0, 1 };
				nodesArray.add(new Node(binaryVals, String.valueOf(i)));
			}

		}

		// create our predicates
		while (s.hasNext()) {
			tempVal = s.nextLine();
			// smush all the clause info together since 0 is the
			// actual breakpoint between them, not a line break
			allClauses += (tempVal + " ");
		}

		// break into individual predicates
		String[] clauses = allClauses.split(" 0 ");
		for (int i = 0; i < clauses.length; i++) {

			// read which nodes are in this predicate
			ArrayList<Node> positivePNodes = new ArrayList<Node>();
			ArrayList<Node> negativePNodes = new ArrayList<Node>();
			String[] clauseNodes = clauses[i].split("\\s+");
			for (int j = 0; j < clauseNodes.length; j++) {
				if (clauseNodes[j].trim().startsWith("-")) {
					// add as a negated variable
					for (Node n : nodesArray) {
						if (n.getName().equals(clauseNodes[j].trim().substring(1))) {
							negativePNodes.add(n);
						}
					}
				} else {
					// add as a positive variable
					for (Node n : nodesArray) {
						if (n.getName().equals(clauseNodes[j].trim())) {
							positivePNodes.add(n);
						}
					}
				}
			}

			// build list of names for predicate
			ArrayList<String> pNames = new ArrayList<String>();
			ArrayList<String> nNames = new ArrayList<String>();

			for (Node n : positivePNodes) {
				pNames.add(n.getName());
			}

			for (Node n : negativePNodes) {
				nNames.add(n.getName());
			}

			// actually make predicate
			Predicate p = new Predicate(pNames, nNames);
			predicates.add(p);
			// System.out.println("New predicate: ");
			// System.out.println(p.toString());

			// add edges between nodes in same predicate
			ArrayList<Node> combinedNodes = new ArrayList<Node>();
			combinedNodes.addAll(positivePNodes);
			combinedNodes.addAll(negativePNodes);

			for (Node e1 : combinedNodes) {
				for (Node e2 : combinedNodes) {
					if (!e1.equals(e2) && !hasEdge(e1, e2)) {
						// System.out.println("added an edge: (" + e1.getName()
						// + " - " + e2.getName() + ")");
						edgesArray.add(new Edge(e1, e2));
						e1.addNeighbor(e2);
						e2.addNeighbor(e1);
					}
				}
			}
		}

		// create problem
		problem = new MaxSatProblem(predicates);
	}

	/**
	 * Reads in a graph-coloring problem from a specially formatted file
	 * 
	 * @param s
	 */
	private void readGCProblemFromFile(Scanner s) {

		String tempVal = null;
		int optimalNo = 0;

		while (s.hasNext()) {
			// keep scanning for the next non-empty line
			if (s.nextLine().equals("")) {
				tempVal = s.nextLine();
			}

			// gets the number associated with the optimal solution
			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = s.nextLine();
			}

			// if the line is not a comment, per the file structure is the
			// optimal number
			// associated with the solution
			if (!tempVal.startsWith("%")) {
				optimalNo = Integer.parseInt(tempVal);
			}

			// keep scanning for the next non-empty line
			if (s.nextLine().equals("")) {
				tempVal = s.nextLine();
			}

			// gets the node info
			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = s.nextLine();
			}

			// splits the string into an array of separate node objects
			if (!tempVal.startsWith("%")) {
				stringNodes = tempVal.split(",");
			}

			// trims extra whitespace from node objects
			for (int i = 0; i < stringNodes.length; i++) {
				if (stringNodes[i].startsWith(" ")) {
					stringNodes[i] = stringNodes[i].trim();
				}
			}

			// keep scanning for the next non-empty line
			if (s.nextLine().equals("")) {
				tempVal = s.nextLine();
			}

			// gets the edge info first
			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = s.nextLine();
			}

			// if the line is not a comment, per the file structure is the
			// edges
			if (!tempVal.startsWith("%")) {
				stringEdges = tempVal.split(";");
			}

			// trims extra whitespace from edge objects
			for (int i = 0; i < stringEdges.length; i++) {
				if (stringEdges[i].startsWith(" ")) {
					stringEdges[i] = stringEdges[i].trim();
				}
			}

			// keep scanning for the next non-empty line
			if (s.nextLine().equals("")) {
				tempVal = s.nextLine();
			}

			// get variable info
			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = s.nextLine();
			}

			// if the line is not a comment, per file structure is values
			if (!tempVal.startsWith("%")) {
				stringValues = tempVal.split(";");
			}

			// trims the extra information and gets the (in node order)
			// values
			for (int i = 0; i < stringValues.length; i++) {
				// gets the information in format NODE: X, X
				int startOfValues;
				startOfValues = stringValues[i].lastIndexOf(":");
				stringValues[i] = stringValues[i].substring(startOfValues + 1, stringValues[i].length());
				// trims extra whitespace from edge objects
				if (stringValues[i].startsWith(" ")) {
					stringValues[i] = stringValues[i].trim();
				}
			}
		}

		// create problem
		problem = new GraphColoringProblem(optimalNo);
	}

	/**
	 * Sets the potentials for adjacent nodes' values being the same to zero
	 */
	private void handleConstraints() {
		for (Edge e : edgesArray) {
			problem.handleEdgeConstraints(e);
		}
	}

	/**
	 * Copy constructor
	 */
	public CliqueMarkovNetwork copy() {
		CliqueMarkovNetwork mnCopy = new CliqueMarkovNetwork(problemType, optimalNo, runs);
		// TODO: will probably need a different constructor

		ArrayList<Edge> e = new ArrayList<Edge>();
		// copies over all the edges
		for (int i = 0; i < edgesArray.size(); i++) {
			e.add(edgesArray.get(i).copy());
		}
		// TODO: will need to copy the cliques!

		// pulls all the new copied nodes
		ArrayList<Node> n = new ArrayList<Node>();
		for (Edge edge : e) {
			n.addAll(edge.getEndpoints());
		}

		mnCopy.setEdges(e);
		mnCopy.setNodes(n);
		return mnCopy;
	}

	/**
	 * Use this to create the network structure (known) for the network
	 */
	public void createNetworkStructure() {

		// step 1: create nodes
		// initialize node and value objects from the string arrays
		for (int i = 0; i < stringNodes.length; i++) {
			String nodeName = stringNodes[i];

			String[] tempValues = stringValues[i].split(",");

			int numValues = tempValues.length;
			int[] values = new int[numValues];

			for (int j = 0; j < numValues; j++) {
				values[j] = Integer.parseInt(tempValues[j]);
			}

			// creates a node with the appropriate values and node name
			Node thisNode = new Node(values, nodeName);
			nodesArray.add(thisNode);
		}

		// step 2: create edges
		// makes temporary node objects to store the start/end of an edge
		Node startingNode = null;
		Node endingNode = null;

		// goes through each edge in the array of edges (from the file read in
		// earlier)
		for (int e = 0; e < stringEdges.length; e++) {
			// for each node in the array of Nodes gets the starting and ending
			// nodes
			for (int n = 0; n < nodesArray.size(); n++) {
				// gets the first node from the string array of edges
				if (stringEdges[e].startsWith(nodesArray.get(n).getName())) {
					startingNode = nodesArray.get(n);
				}
			}

			for (int n = 0; n < nodesArray.size(); n++) {
				// get the last node from the string array of edges
				if (stringEdges[e].endsWith(nodesArray.get(n).getName())) {
					endingNode = nodesArray.get(n);
				}
			}

			Edge E = new Edge(startingNode, endingNode);
			edgesArray.add(E);
			// each time an edge A-B is created, do: A.addNeighbor(B) and
			// B.addNeighbor(A)
			startingNode.addNeighbor(endingNode);
			endingNode.addNeighbor(startingNode);
		}

		// for testing, can remove when finished
		handleConstraints();
		// print();
	}

	/**
	 * Creates a VALID sample from a uniform distribution
	 * 
	 * @return
	 */
	public Sample createRandomValidSample() {
		Sample sample = new Sample(this);

		// 1) generate an initial sample (probably randomly from vals(Vars)
		for (Node n : nodesArray) {
			// chosen uniformly - does that work?
			int[] vals = n.getVals();
			double r = Math.random();
			double interval = (double) 1 / vals.length;

			// TODO: this might be stupid
			if (vals.length != 2) {

				int counter = 0;
				for (double i = interval; i <= 1; i += interval) {
					if (r < i) {
						sample.setSampledValue(n, vals[counter]);
						break;
					} else if (counter == vals.length - 2) {
						sample.setSampledValue(n, vals[counter - 1]);
						break;
					}
					counter++;
				}
			} else {
				if (r < 0.5) {
					sample.setSampledValue(n, vals[0]);
				} else {
					sample.setSampledValue(n, vals[1]);
				}
			}
		}

		if (!problem.satisfiesConstraints(sample, edgesArray)) {
			sample = createRandomValidSample();
		}

		return sample;
	}

	/**
	 * Creates a sample from a uniform distribution
	 * 
	 * @return
	 */
	public Sample createRandomSample() {
		Sample sample = new Sample(this);

		// 1) generate an initial sample (probably randomly from vals(Vars)
		for (Node n : nodesArray) {
			// chosen uniformly - does that work?
			int[] vals = n.getVals();
			double r = Math.random();
			double interval = (double) 1 / vals.length;

			// TODO: this might be stupid
			if (vals.length != 2) {

				int counter = 0;
				for (double i = interval; i <= 1; i += interval) {
					if (r < i) {
						sample.setSampledValue(n, vals[counter]);
						break;
					} else if (counter == vals.length - 2) {
						sample.setSampledValue(n, vals[counter - 1]);
						break;
					}
					counter++;
				}
			} else {
				if (r < 0.5) {
					sample.setSampledValue(n, vals[0]);
				} else {
					sample.setSampledValue(n, vals[1]);
				}
			}
		}
		return sample;
	}

	/**
	 * Returns the result of Gibbs sampling this distribution
	 * 
	 * @return
	 */
	public Sample sample() {

		// TODO: redo this with clique potentials
		Sample sample = createRandomValidSample();

		System.out.println(" Initial sample: ");
		sample.print();

		for (int i = 0; i < runs; i++) {
			System.out.println("\n ********** RUN # " + (i + 1));

			// 2) For each non-evidence variable (so, all of them) [order
			// doesn't
			// matter]...
			Collections.shuffle(nodesArray);
			for (Node N : nodesArray) {
				System.out.println("\n >>>>> Resampling Node " + N.getName());

				// get all cliques with factors containing N
				System.out.println("Relevant cliques:");
				ArrayList<Clique> relevantCliques = new ArrayList<Clique>();
				for (Clique c : maxCliques) {
					if (c.includes(N)) {
						relevantCliques.add(c);
						c.print();
					}
				}

				// stores the distribution generated by calculations
				ProbDist probs = new ProbDist(N.getVals(), N);

				// for each value of N
				int[] nVals = N.getVals();
				for (int n = 0; n < nVals.length; n++) {
					System.out.println();
					System.out.print("\n P~ (" + N.getName() + " == " + nVals[n] + ") \n");

					// TODO: for each clique involving N,
					// get the assignments to the other nodes (M)
					// then multiply through by that factor entry
					for (Clique c : relevantCliques) {
						c.print();
						// 1) identify nodes in clique
						Node[] cliqueNodes = c.getNodesAsArray();
						// 2) get corresponding values
						int[] sampleValues = new int[cliqueNodes.length];
						for (int v = 0; v < sampleValues.length; v++) {
							if (cliqueNodes[v].equals(N)) {
								// set as value for which we're getting the
								// probability
								sampleValues[v] = nVals[n];
							} else {
								// set as current value from sample
								sampleValues[v] = sample.getValue(cliqueNodes[v]);
							}
						}

						System.out.println("Nodes: ");
						for (int u = 0; u < sampleValues.length; u++) {
							System.out.println("Node " + cliqueNodes[u].getName() + ": " + sampleValues[u]);
						}

						// 3) extract potential
						double p = c.getPotentialCorrespondingToAssignment(cliqueNodes, sampleValues);
						System.out.println("Potential: " + p);
						System.out.println("Change to: " + (probs.getProb(nVals[n]) * p));
						probs.setProb(nVals[n], probs.getProb(nVals[n]) * p);
					}
				}

				System.out.println("\n Unnormalized");
				probs.print();

				// re-normalize
				probs.normalize();

				System.out.println("Normalized");
				probs.print();

				// re-sample resulting distribution for N
				sample.setSampledValue(N, probs.sample());
			}
		}

		// testing, remove
		System.out.println("\n Final sample: ");
		sample.print();

		return sample;
	}

	/**
	 * Carries out adjustment using scaling factor
	 */
	public void adjustPotentials(Sample s, double epsilon) {
		// TODO: this should occur on cliques, not edges
		for (Clique c : maxCliques) {
			c.adjustPotentials(s, epsilon);
			// shouldn't need to-zero adjustment, but let's check
		}

		// still check constraints
		for (Edge e : edgesArray) {
			problem.checkEdgeConstraints(e);
		}
	}

	/**
	 * Performs position update
	 */
	public void updatePotentials() {
		// TODO: nope, not for this
		for (Edge e : edgesArray) {
			e.updateFactorPotentials();
			problem.handleEdgeConstraints(e);
		}
	}

	/**
	 * Finds the maximal cliques using the Bron-Kerbosch algorithm with
	 * degeneracy ordering
	 */
	public void bronKerbosch(ArrayList<Node> R, ArrayList<Node> P, ArrayList<Node> X) {

		// // create degeneracy ordering of vertices
		// ArrayList<Node> ordering = new ArrayList<Node>();
		// ArrayList<Node> allNodes = new ArrayList<Node>();
		// allNodes.addAll(nodesArray);
		// // repeatedly select vertex of minimum degree
		// while (!allNodes.isEmpty()){
		// double minDegree = nodesArray.size() + 1;
		// Node toSelect = null;
		// for (Node n : allNodes){
		// if (n.getMB().size() < minDegree){
		// minDegree = n.getMB().size();
		// toSelect = n;
		// }
		// }
		// ordering.add(toSelect);
		// allNodes.remove(toSelect);
		// }
		//
		// System.out.println("Degeneracy Ordering:");
		// for (Node n : ordering){
		// System.out.println(n.getName() + ": " + n.getMB().size());
		// }
		// // TODO: this ordering needs to be of P...
		//
		if (P.isEmpty() && X.isEmpty()) {
			// R is a maximal clique
			maxCliques.add(new Clique(R));
		}

		// avoid concurrent modification issues
		// TODO: could this cause issues...?
		ArrayList<Node> cp = new ArrayList<Node>();
		cp.addAll(P);
		for (Node v : cp) {
			// System.out.println(">>> Node " + v.getName());

			// union of R and v
			ArrayList<Node> RuV = new ArrayList<Node>();
			RuV.addAll(R);
			RuV.add(v);
			// System.out.println("RuV:");
			// for (Node a : RuV){
			// System.out.println(a.getName());
			// }
			// intersection of P and neighbors of v
			ArrayList<Node> PiN = new ArrayList<Node>();
			for (Node a : P) {
				if (v.getMB().contains(a)) {
					PiN.add(a);
				}
			}
			for (Node a : v.getMB()) {
				if (P.contains(a) && !PiN.contains(a)) {
					PiN.add(a);
				}
			}
			// System.out.println("PiN:");
			// for (Node a : PiN){
			// System.out.println(a.getName());
			// }
			// intersection of X and neighbors of v
			ArrayList<Node> XiN = new ArrayList<Node>();
			for (Node a : X) {
				if (v.getMB().contains(a)) {
					XiN.add(a);
				}
			}
			for (Node a : v.getMB()) {
				if (X.contains(a) && !XiN.contains(a)) {
					XiN.add(a);
				}
			}
			// System.out.println("XiN:");
			// for (Node a : XiN){
			// System.out.println(a.getName());
			// }

			bronKerbosch(RuV, PiN, XiN);
			P.remove(v);
			X.add(v);
		}

	}

	public ArrayList<Node> getNodes() {
		return nodesArray;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodesArray = nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edgesArray;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edgesArray = edges;
	}

	public double[][] getAllPotentials() {
		double[][] entries = new double[edgesArray.size()][];
		// each row represents all the factor potentials from one edge
		for (int i = 0; i < edgesArray.size(); i++) {
			entries[i] = edgesArray.get(i).getAllEntries();
		}
		return entries;
	}

	public double[][] getAllVelocities() {
		double[][] velocities = new double[edgesArray.size()][];
		// each row represents all the velocity entries from one edge
		for (int i = 0; i < edgesArray.size(); i++) {
			velocities[i] = edgesArray.get(i).getVelocity();
		}
		return velocities;
	}

	/**
	 * Should only be called with the output of a particle's velocity update
	 * 
	 * @param newV
	 */
	public void adjustAllVelocities(double[][] newV) {
		// replaces the velocity vectors with the new ones, incoming from
		// velocity update
		for (int e = 0; e < newV.length; e++) {
			edgesArray.get(e).setVelocity(newV[e]);
		}
	}

	public void print() {
		System.out.println("NODES");
		for (int i = 0; i < nodesArray.size(); i++) {
			System.out.println("> " + nodesArray.get(i).getName());
		}
		System.out.println("EDGES");
		for (int i = 0; i < edgesArray.size(); i++) {
			edgesArray.get(i).printFactors();
		}
	}

	// private boolean hasNode(String name) {
	// for (Node n : nodesArray) {
	// if (n.getName().equals(name)) {
	// return true;
	// }
	// }
	// return false;
	// }

	private boolean hasEdge(Node e1, Node e2) {
		for (Edge e : edgesArray) {
			if (e.getEndpoints().contains(e1) && e.getEndpoints().contains(e2)) {
				return true;
			}
		}
		return false;
	}

	public ApplicationProblem getProblem() {
		return problem;
	}

	public ArrayList<Clique> getMaxCliques() {
		return maxCliques;
	}
}
