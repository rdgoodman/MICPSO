package PSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.Node;
import MN.ProbDist;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;
import applicationProblems.MaxSatProblem;
import applicationProblems.Predicate;

public class ICParticle implements Particle {

	private Sample pBest_sample;
	private ICParticle pBest_position;
	private ProbDist[] probs;
	private FitnessFunction f;
	private int numSamples;
	private double epsilon;
	private ApplicationProblem problem;

	// Array list for storing nodes and edges
	private ArrayList<Node> nodesArray = new ArrayList<Node>();
	private ArrayList<Edge> edgesArray = new ArrayList<Edge>(); // TODO: this is
																// only for
																// constraint-checking

	/**
	 * Creates a new particle for ICPSO (no dependency handling)
	 * 
	 * @param fileName
	 *            the file containing the problem encoding
	 * @param f
	 *            the fitness function for the problem
	 * @param numSamples
	 *            the number of samples to create during each fitness evaluation
	 * @param epsilon
	 *            scaling factor
	 * @throws FileNotFoundException
	 */
	public ICParticle(String fileName, ApplicationProblem problem, int numSamples, double epsilon)
			throws FileNotFoundException {
		this.f = problem.getFitnessFunction();
		this.problem = problem;
		this.numSamples = numSamples;
		this.epsilon = epsilon;

		System.out.println("///////////////////////////////////////////////////////////");

		if (problem instanceof GraphColoringProblem) {
			readGCParticle(fileName);
			System.out.println("VERTEX-COLOR problem");
		} else if (problem instanceof MaxSatProblem) {
			readMSParticle(fileName);
			System.out.println("MAXSAT Problem");
		}

	}

	/**
	 * Reads a MAXSAT problem in and creates the particle from that
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	private void readMSParticle(String fileName) throws FileNotFoundException {
		Scanner s = null;
		String tempVal = null;

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// Reads in the nodes, edges and values in from a specifically formatted
		// file
		try {
			s = new Scanner(new BufferedReader(new FileReader(file)));

			// Read the first line in the file
			tempVal = s.nextLine();

			// gets the type of problem first
			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = s.nextLine();
			}

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
				System.out.println(tempVal);

				// smush all the clause info together since 0 is the
				// actual breakpoint between them, not a line break
				allClauses += (tempVal + " ");
			}

			// break into individual predicates
			String[] clauses = allClauses.split("0");
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
				System.out.println("New predicate: ");
				System.out.println(p.toString());

				// add edges between nodes in same predicate
				ArrayList<Node> combinedNodes = new ArrayList<Node>();
				combinedNodes.addAll(positivePNodes);
				combinedNodes.addAll(negativePNodes);

				for (Node e1 : combinedNodes) {
					for (Node e2 : combinedNodes) {
						if (!e1.equals(e2) && !hasEdge(e1, e2)) {
							System.out.println("added an edge: (" + e1.getName() + " - " + e2.getName() + ")");
							edgesArray.add(new Edge(e1, e2));
						}
					}
				}
			}
			
			probs = new ProbDist[nodesArray.size()];
			for (int i = 0; i < nodesArray.size(); i++){
				probs[i] = new ProbDist(nodesArray.get(i).getVals(), nodesArray.get(i));
				probs[i].normalize();
			}


		} finally {
			if (s != null) {
				s.close();
			}
		}

	}

	/**
	 * Reads a graph-coloring problem in and creates the particle from that
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	private void readGCParticle(String fileName) throws FileNotFoundException {
		// Arrays for storing the string values read in from file
		String[] stringNodes = null;
		String[] stringValues = null;
		String[] stringEdges = null;
		Scanner s = null;

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// Reads in the nodes, edges and values in from a specifically formatted
		// file
		try {
			s = new Scanner(new BufferedReader(new FileReader(file)));

			String potential;

			while (s.hasNext()) {
				// Read the first line in the file
				potential = s.nextLine();
				// TODO: testing, remove
				System.out.println(potential);

				// gets the node info first
				// checks for comments, when present, discards them
				while (potential.startsWith("%")) {
					potential = s.nextLine();
				}

				////////////////////////////////////

				// all the stuff within this little section is read, but
				// ignored/not used
				// since the problem type/size is handled in the PSO class

				// keep scanning for the next non-empty line
				if (s.nextLine().equals("")) {
					potential = s.nextLine();
				}

				// checks for comments, when present, discards them
				while (potential.startsWith("%")) {
					potential = s.nextLine();
				}

				// keep scanning for the next non-empty line
				if (s.nextLine().equals("")) {
					potential = s.nextLine();
				}

				// checks for comments, when present, discards them
				while (potential.startsWith("%")) {
					potential = s.nextLine();
				}

				////////////////////////////////////

				// splits the string into an array of separate node objects
				if (!potential.startsWith("%")) {
					stringNodes = potential.split(",");
				}

				// trims extra whitespace from node objects
				for (int i = 0; i < stringNodes.length; i++) {
					if (stringNodes[i].startsWith(" ")) {
						stringNodes[i] = stringNodes[i].trim();
					}
				}

				// System.out.println("Nodes:");
				// for (int i = 0; i < stringNodes.length; i++){
				// System.out.println(stringNodes[i]);
				// }

				// keep scanning for the next non-empty line
				if (s.nextLine().equals("")) {
					potential = s.nextLine();
				}

				// reads the edge info, but we do not need it, so does not store
				// them
				// checks for comments, when present, discards them
				while (potential.startsWith("%")) {
					potential = s.nextLine();
				}

				// if the line is not a comment, per the file structure is the
				// edges
				if (!potential.startsWith("%")) {
					stringEdges = potential.split(";");
				}

				// trims extra whitespace from edge objects
				for (int i = 0; i < stringEdges.length; i++) {
					if (stringEdges[i].startsWith(" ")) {
						stringEdges[i] = stringEdges[i].trim();
					}
				}

				// keep scanning for the next non-empty line
				if (s.nextLine().equals("")) {
					potential = s.nextLine();
				}

				// get variable info
				// checks for comments, when present, discards them
				while (potential.startsWith("%")) {
					potential = s.nextLine();
				}

				// if the line is not a comment, per file structure is values
				if (!potential.startsWith("%")) {
					stringValues = potential.split(";");
				}

				// trims the extra information and gets the (in node order)
				// values
				for (int i = 0; i < stringValues.length; i++) {
					// gets the information in format NODE: X, X
					int startOfValues;
					// assists in getting the largest number of values found for
					// any node
					startOfValues = stringValues[i].lastIndexOf(":");
					stringValues[i] = stringValues[i].substring(startOfValues + 1, stringValues[i].length());
					// trims extra whitespace from edge objects
					if (stringValues[i].startsWith(" ")) {
						stringValues[i] = stringValues[i].trim();
					}
				}
			}

		} finally {
			if (s != null) {
				s.close();
			}
		}

		// create array of probdists
		probs = new ProbDist[stringNodes.length];

		// create nodes
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

			// System.out.println("New node: " + thisNode.getVals().length);

			// create ProbDists using nodes
			probs[i] = new ProbDist(thisNode.getVals(), thisNode);
			probs[i].normalize();

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
		}
	}

	/**
	 * This constructor is intended for use with the copy() method
	 * 
	 * @param f
	 */
	public ICParticle(FitnessFunction f, Sample pBest_sample) {
		this.f = f;
		this.pBest_sample = pBest_sample;
	}

	@Override
	public Sample sample() {
		Sample s = new Sample();

		// builds a sample by independently sampling every distribution
		for (int i = 0; i < probs.length; i++) {
			int val = probs[i].sample();
			s.setSampledValue(probs[i].getNode(), val);
		}

		s.setEdges(edgesArray);

//		System.out.println("Sample: ");
//		s.print();
		return s;
	}

	@Override
	public double calcFitness() {
		double particleFit = 0;
		// average over a certain number of samples
		for (int i = 0; i < numSamples; i++) {
			// 1) generate a sample and calculate its fitness
			Sample s = sample();
			double fit = f.calcFitness(s);

			// check constraints
			if (!problem.satisfiesConstraints(s, edgesArray)) {
				// penalize if doesn't pass constraints
				s.setFitness(fit + problem.getInvalidSolutionPenalty());
			} else {
				// don't count toward overall particle fitness if penalized
				particleFit += fit;
			}

			//System.out.println("Sample fitness: " + fit);

			// 2) Save this sample if it's the new pBest
			if (pBest_sample == null || problem.compare(pBest_sample.getFitness(), fit) == 1) {
				setPBest(s);
			}
		}
		return particleFit / numSamples;
	}

	@Override
	public void updateVelocity(double omega, double phi1, double phi2, Particle gBest) {
		// decide on multipliers
		double cognitive = Math.random() * phi1;
		double social = Math.random() * phi2;

		// update velocity for each prob dist
		for (int i = 0; i < probs.length; i++) {
			// call update for each element
			probs[i].updateVelocity(omega, cognitive, social, ((ICParticle) gBest).getProbs()[i],
					pBest_position.getProbs()[i]);
		}
	}

	@Override
	public void updatePosition() {
		for (int i = 0; i < probs.length; i++) {
			probs[i].updatePosition();
		}
	}

	@Override
	public void setPBest(Sample s) {
		//System.out.println(" ^ new ^ personal ^ best");
		pBest_sample = s;
		adjustPBest();
		// sets pBest dist!
		pBest_position = this.copy();
	}

	@Override
	public void adjustPBest() {

		// System.out.println("________________________ s");
		// System.out.println("Adjusting using sample: ");
		// pBest_sample.print();
		// print();

		// call bias() method for each variable
		for (int i = 0; i < probs.length; i++) {
			// retrieves value associated with this dist's node in sample
			double k = pBest_sample.getTable().get(probs[i].getNode());
			// calls bias() with that value
			probs[i].bias(k, epsilon);
		}
		// print();
		// System.out.println("________________________ f");
	}

	@Override
	public ICParticle copy() {
		ICParticle cp = new ICParticle(f, pBest_sample);

		ProbDist[] cpProbs = new ProbDist[probs.length];
		for (int i = 0; i < cpProbs.length; i++) {
			cpProbs[i] = probs[i].copy();
		}
		cp.setDist(cpProbs);

		return cp;
	}

	public void setDist(ProbDist[] probs) {
		this.probs = probs;
	}

	public ProbDist[] getProbs() {
		return probs;
	}

	@Override
	public Sample getBestSample() {
		return pBest_sample;
	}

	@Override
	public void print() {
		// TODO: uncomment at some point
//		for (int i = 0; i < probs.length; i++) {
//			probs[i].print();
//		}
	}

	@Override
	public ArrayList<Edge> getEdges() {
		return edgesArray;
	}

	private boolean hasEdge(Node e1, Node e2) {
		for (Edge e : edgesArray) {
			if (e.getEndpoints().contains(e1) && e.getEndpoints().contains(e2)) {
				return true;
			}
		}
		return false;
	}
}
