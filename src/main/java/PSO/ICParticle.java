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

		///////////////////////////////////////////////////////////

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

			/*
			 * Reads the file, ignoring lines with % (which are comment lines).
			 * File is structured so that the nodes are first (comma separated),
			 * followed by the edges (in form A, B semi-colon separated) and
			 * then the values for the variables (in form A: 0, 1). The values
			 * need to be in the same order as the node variables. At this
			 * stage, the variables are read in as strings, and after the file
			 * is closed they are converted to the appropriate object type
			 * (i.e., Node or Edge objects).
			 * 
			 */
			while (s.hasNext()) {
				// Read the first line in the file
				potential = s.nextLine();

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

		// System.out.println("Number of edges: " + edgesArray.size());
		// for (Edge e: edgesArray){
		// System.out.println(e.getEndpoints().getFirst().getName() + "-" +
		// e.getEndpoints().getLast().getName());
		// }

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

		System.out.println("Sample: ");
		s.print();
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

			System.out.println("Sample fitness: " + fit);

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
		System.out.println(" ^ new ^ personal ^ best");
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
		for (int i = 0; i < probs.length; i++) {
			probs[i].print();
		}
	}

}
