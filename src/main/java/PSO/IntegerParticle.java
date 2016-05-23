package PSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import MN.Edge;
import MN.Node;
import MN.ProbDist;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;
import applicationProblems.MaxSatProblem;
import applicationProblems.Predicate;

public class IntegerParticle {

	private int[] position; // build sample out of position for fitness eval
	private double[] velocity;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edgesArray = new ArrayList<Edge>();
	private FitnessFunction f;
	private ApplicationProblem problem;

	private int[] pBest;
	private double pBest_fitness;
	private double fitness; // TODO: this contains the penalty

	// Arrays for storing the string values read in from file
	String[] stringNodes = null;
	String[] stringValues = null;
	String[] stringEdges = null;
	Scanner scanner = null;

	public IntegerParticle(String fileName, ApplicationProblem problem) throws FileNotFoundException {
		this.f = problem.getFitnessFunction();
		this.problem = problem;

		System.out.println("///////////////////////////////////////////////////////////");

		if (problem instanceof GraphColoringProblem) {
			readGCParticle(fileName);
			System.out.println("VERTEX-COLOR problem");
		} else if (problem instanceof MaxSatProblem) {
			readMSParticle(fileName);
			System.out.println("MAXSAT Problem");
		}

//		System.out.println("NODES:");
//		for (Node n : nodes) {
//			System.out.println(n.getName());
//		}
//
//		System.out.println("EDGES");
//		for (Edge e : edgesArray) {
//			e.printFactors();
//		}

		initializeVelocityAndPosition();
	}

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
					nodes.add(new Node(binaryVals, String.valueOf(i)));
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
			String[] clauses = allClauses.split(" 0 ");
			for (int i = 0; i < clauses.length; i++) {

				// read which nodes are in this predicate
				ArrayList<Node> positivePNodes = new ArrayList<Node>();
				ArrayList<Node> negativePNodes = new ArrayList<Node>();
				String[] clauseNodes = clauses[i].split("\\s+");
				for (int j = 0; j < clauseNodes.length; j++) {
					if (clauseNodes[j].trim().startsWith("-")) {
						// add as a negated variable
						for (Node n : nodes) {
							if (n.getName().equals(clauseNodes[j].trim().substring(1))) {
								negativePNodes.add(n);
							}
						}
					} else {
						// add as a positive variable
						for (Node n : nodes) {
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


		} finally {
			if (s != null) {
				s.close();
			}
		}

	}

	private void readGCParticle(String fileName) throws FileNotFoundException {
		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// Reads in the nodes, edges and values in from a specifically formatted
		// file
		try {
			scanner = new Scanner(new BufferedReader(new FileReader(file)));

			String tempVal;

			while (scanner.hasNext()) {
				// Read the first line in the file
				tempVal = scanner.nextLine();

				// gets the node info first
				// checks for comments, when present, discards them
				while (tempVal.startsWith("%")) {
					tempVal = scanner.nextLine();
				}

				////////////////////////////////////
				// all the stuff within this little section is read, but
				//////////////////////////////////// ignored/not used
				// since the problem type/size is handled in the PSO class

				// keep scanning for the next non-empty line
				if (scanner.nextLine().equals("")) {
					tempVal = scanner.nextLine();
				}

				// checks for comments, when present, discards them
				while (tempVal.startsWith("%")) {
					tempVal = scanner.nextLine();
				}

				// keep scanning for the next non-empty line
				if (scanner.nextLine().equals("")) {
					tempVal = scanner.nextLine();
				}

				// checks for comments, when present, discards them
				while (tempVal.startsWith("%")) {
					tempVal = scanner.nextLine();
				}

				////////////////////////////////////

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
				if (scanner.nextLine().equals("")) {
					tempVal = scanner.nextLine();
				}

				// reads the edge info, but we do not need it, so does not store
				// them
				// checks for comments, when present, discards them
				while (tempVal.startsWith("%")) {
					tempVal = scanner.nextLine();
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
				if (scanner.nextLine().equals("")) {
					tempVal = scanner.nextLine();
				}

				// get variable info
				// checks for comments, when present, discards them
				while (tempVal.startsWith("%")) {
					tempVal = scanner.nextLine();
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
			if (scanner != null) {
				scanner.close();
			}
		}

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
			nodes.add(thisNode);
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
			for (int n = 0; n < nodes.size(); n++) {
				// gets the first node from the string array of edges
				if (stringEdges[e].startsWith(nodes.get(n).getName())) {
					startingNode = nodes.get(n);
				}
			}

			for (int n = 0; n < nodes.size(); n++) {
				// get the last node from the string array of edges
				if (stringEdges[e].endsWith(nodes.get(n).getName())) {
					endingNode = nodes.get(n);
				}
			}

			Edge E = new Edge(startingNode, endingNode);
			edgesArray.add(E);
		}
	}

	/**
	 * Updates particle velocity
	 * 
	 * @param omega
	 * @param phi1
	 * @param phi2
	 * @param gBest
	 */
	public void updateVelocity(double omega, double phi1, double phi2, int[] gBest) {
		// decide on multipliers
		double cognitive = Math.random() * phi1;
		double social = Math.random() * phi2;

		for (int i = 0; i < velocity.length; i++) {
			velocity[i] = (omega * velocity[i]) + (cognitive * pBest[i]) + (social * gBest[i]);
		}
	}

	/**
	 * Randomly initializes velocity and position vectors
	 */
	private void initializeVelocityAndPosition() {

		velocity = new double[nodes.size()];
		position = new int[nodes.size()];

		for (int i = 0; i < velocity.length; i++) {
			position[i] = (int) (Math.random() * 10);
			velocity[i] = Math.random();
		}

	}

	/**
	 * Updates particle position
	 */
	public void updatePosition() {
		for (int i = 0; i < position.length; i++) {
			double move = position[i] + velocity[i];

			// snap to nearest integer
			if (move - Math.floor(move) > 0.5) {
				position[i] = (int) Math.ceil(move);
			} else {
				position[i] = (int) Math.floor(move);
			}

			int[] possible = nodes.get(i).getVals();
			boolean validValue = false;

			// checks to see if the value at position i is possible, given the
			// list of valid values
			for (int j = 0; j < possible.length; j++) {
				if (position[i] == possible[j]) {
					// checks that the position value is in fact in the list of
					// valid values
					validValue = true;
				}
			}

			// if the value is outside the acceptable range
			if (!validValue) {
				// re-initialize to random value within range
				Random rand = new Random();
				int randomNum = rand.nextInt(possible.length);
				position[i] = possible[randomNum];
			}
		}
	}

	/**
	 * Builds a sample using the current particle position
	 * 
	 * @return
	 */
	public Sample buildSampleFromPosition() {
		Sample s = new Sample();

		for (int i = 0; i < nodes.size(); i++) {
			s.setSampledValue(nodes.get(i), position[i]);
		}

		return s;
	}

	/**
	 * Calculates particle's fitness
	 * 
	 * @return
	 */
	public double calcFitness() {
		// build a sample
		Sample s = buildSampleFromPosition();
		double fit = f.calcFitness(s);

		// check constraints
		if (!problem.satisfiesConstraints(s, edgesArray)) {
			// penalize if doesn't pass constraints
			s.setFitness(fit + problem.getInvalidSolutionPenalty());
		}

		// set pBest
		if (pBest == null || problem.compare(pBest_fitness, fit) == 1) {
			pBest_fitness = fit;
			pBest = new int[position.length];
			// copy this position
			for (int i = 0; i < pBest.length; i++) {
				pBest[i] = position[i];
			}
		}

		fitness = s.getFitness();

		return fit;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public void print() {
		System.out.println("Fitness: " + calcFitness());
	}

	public double getFitness() {
		return fitness;
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
