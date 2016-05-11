package MN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;

public class MarkovNetwork {
	// Array list for storing nodes and edges
	private ArrayList<Node> nodesArray = new ArrayList<Node>();
	private ArrayList<Edge> edgesArray = new ArrayList<Edge>();

	// Variables for storing the string values read in from file
	String problemType = "";
	int optimalNo = 0;
	String[] stringNodes = null;
	String[] stringEdges = null;
	String[] stringValues = null;
	
	ApplicationProblem problem;

	// number of runs for Gibbs sampling
	// TODO: ultimately this should be tunable
	int runs = 1;

	/**
	 * Constructor when read in from file
	 * 
	 * @param inputFile
	 * @throws FileNotFoundException
	 */
	public MarkovNetwork(String inputFile) throws FileNotFoundException {
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

			/*
			 * Reads the file, ignoring lines with % (which are comment lines).
			 * File is structured so that the problem description (GC or DS) is
			 * first, the number associated with the optimal solution is second,
			 * nodes are next (comma separated), followed by the edges (in form
			 * A, B semi-colon separated) and then the values for the variables
			 * (in form A: 0, 1). The values need to be in the same order as the
			 * node variables. At this stage, the variables are read in as
			 * strings, and after the file is closed they are converted to the
			 * appropriate object type (i.e., Node or Edge objects).
			 * 
			 */
			while (s.hasNext()) {
				// Read the first line in the file
				tempVal = s.nextLine();

				// gets the type of problem first
				// checks for comments, when present, discards them
				while (tempVal.startsWith("%")) {
					tempVal = s.nextLine();
				}

				problemType = tempVal;

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
			// creates the Markov network
			createNetworkStructure();
			
			// TODO: create problem
			if (problemType.equals("GC")) {
				problem = new GraphColoringProblem(optimalNo);
			}

		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	/**
	 * Sets the potentials for adjacent nodes' values being the same to zero
	 */
	private void handleConstraints() {
		if (problemType.equals("GC")) {
			for (Edge e : edgesArray) {
				e.handleGCConstraints();
			}
		}
	}

	/**
	 * Copy constructor
	 */
	public MarkovNetwork(String problemType, int optimalNo, int runs) {
		this.problemType = problemType;
		this.optimalNo = optimalNo;
		this.runs = runs;
	}

	/**
	 * Copy constructor
	 */
	public MarkovNetwork copy() {
		MarkovNetwork mnCopy = new MarkovNetwork(problemType, optimalNo, runs);

		ArrayList<Edge> e = new ArrayList<Edge>();
		// copies over all the edges
		for (int i = 0; i < edgesArray.size(); i++) {
			e.add(edgesArray.get(i).copy());
		}

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
		//print();
	}
	
	/**
	 * Creates a VALID sample from a uniform distribution
	 * @return
	 */
	public Sample createRandomValidSample(){
		Sample sample = new Sample(this);

		// 1) generate an initial sample (probably randomly from vals(Vars)
		for (Node n : nodesArray) {
			// chosen uniformly - does that work?
			int[] vals = n.getVals();
			double r = Math.random();
			double interval = (double) 1 / vals.length;
			
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
		}
		
		if (!problem.satisfiesConstraints(sample, edgesArray)){
			sample = createRandomValidSample();
		}		
		
		return sample;
	}
	
	/**
	 * Creates a sample from a uniform distribution
	 * @return
	 */
	public Sample createRandomSample(){
		Sample sample = new Sample(this);

		// 1) generate an initial sample (probably randomly from vals(Vars)
		for (Node n : nodesArray) {
			// chosen uniformly - does that work?
			int[] vals = n.getVals();
			double r = Math.random();
			double interval = (double) 1 / vals.length;
			
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
		}
		return sample;
	}

	/**
	 * Returns the result of Gibbs sampling this distribution
	 * 
	 * @return
	 */
	public Sample sample() {

		Sample sample = new Sample(this);

		// 1) generate an initial sample (probably randomly from vals(Vars)
		for (Node n : nodesArray) {
			// chosen uniformly - does that work?
			int[] vals = n.getVals();
			double r = Math.random();
			double interval = (double) 1 / vals.length;
			
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
		}

		//System.out.println(" Initial sample: ");
		//sample.print();
		
		
		for (int i = 0; i < runs; i++) {

			// 2) For each non-evidence variable (so, all of them) [order
			// doesn't
			// matter]...
			
			Collections.shuffle(nodesArray);
			
			for (Node N : nodesArray) {
				
				//System.out.println(">>>>> Resampling Node " + N.getName());

				// 4) Calculate P(X|MB(X)) using current values for MB(X)
				ArrayList<Node> MB = N.getMB();
				ArrayList<Edge> E = new ArrayList<Edge>();

				// Get all edges adjacent to N (there must be a better way to do
				// this)
				// (gives a more limited list to search later on)
				for (Edge e : edgesArray) {
					if (e.getEndpoints().contains(N)) {
						E.add(e);
					}
				}

				// stores the distribution generated by calculations
				ProbDist probs = new ProbDist(N.getVals(), N);

				// for each value of N
				int[] nVals = N.getVals();
				for (int n = 0; n < nVals.length; n++) {
					// System.out.println();
					// System.out.print("\n P~ (" + N.getName() + " == " +
					// nVals[n]
					// + ") \n");
						
					for (Node M : MB) {
						Edge edge = null;
						// pull the edge between N and M
						for (Edge e : E) {
							if (e.getEndpoints().contains(M)) {
								edge = e;
							}
						}
						
						// get sample value of M
						double mVal = sample.getValue(M);

						// get potential where M=mVal and N=nVals[n]
						double p = edge.getPotential(N, nVals[n], M, mVal);
						// System.out.print(p + "[" + M.getName() + " == " +
						// mVal + "] *");

						// multiply current P(N==n) by p
						probs.setProb(nVals[n], probs.getProb(nVals[n]) * p);
					}

					// System.out.println("\n Unnormalized");
					// probs.print();

					// re-normalize
					// TODO: does this need to move?
					probs.normalize();

					// System.out.println("Normalized");
					// probs.print();

					// re-sample resulting distribution for N
					sample.setSampledValue(N, probs.sample());
				}

			}

			// testing, remove
//			System.out.println("\n Final sample: ");
//			sample.print();
		}

		return sample;
	}

	/**
	 * Carries out adjustment using scaling factor
	 */
	public void adjustPotentials(Sample s, double epsilon) {
		for (Edge e : edgesArray) {			
			e.adjustPotentials(s, epsilon);		
			// shouldn't need to-zero adjustment, but let's check
			if (problemType.equals("GC")) {
				e.checkGCConstraints();
			}
		}
	}

	/**
	 * Performs position update
	 */
	public void updatePotentials() {
		for (Edge e : edgesArray) {
			e.updateFactorPotentials();
			
			// adjust to zero
			if (problemType.equals("GC")) {
				e.handleGCConstraints();
			}
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
		
		// TODO: uncomment some time
//		System.out.println("NODES");
//		for (int i = 0; i < nodesArray.size(); i++) {
//			System.out.println("> " + nodesArray.get(i).getName());
//		}
//		System.out.println("EDGES");
//		for (int i = 0; i < edgesArray.size(); i++) {
//			edgesArray.get(i).printFactors();
//		}
	}
}
