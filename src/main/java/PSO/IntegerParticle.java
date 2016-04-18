package PSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.Node;
import MN.Sample;

public class IntegerParticle {

	private int[] position; // build sample out of position for fitness eval
	private double[] velocity;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edgesArray = new ArrayList<Edge>();
	private FitnessFunction f;
	
	private int[] pBest;
	private double pBest_fitness;
	
	// Arrays for storing the string values read in from file
	String[] stringNodes = null;
	String[] stringValues = null;
	String[] stringEdges = null;
	Scanner s = null;
	
	public IntegerParticle(String fileName, FitnessFunction f) throws FileNotFoundException {
		this.f = f;
		
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
						// all the stuff within this little section is read, but ignored/not used
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
				
				// create nodes
				// initialize node and value objects from the string arrays
				for (int i = 0; i < stringNodes.length; i++) {
					String nodeName = stringNodes[i];

					// holds the number of values associated with any nod
					int numValues = 0;
					
					// determines the number of values for each nodes
					for (int j = 0; j < stringValues[i].length(); j++) {
						if (Character.isDigit(stringValues[i].charAt(j))) {
					    	numValues++;
					    }
					}
					
					int[] values = new int[numValues];
					
					int startIndex = 0;
					int stopIndex = 1;

					
					// gets values from the value array
					for (int n = 0; n < numValues; n++) {
						// reminder: values are comma separated
						int thisVal = Integer.parseInt(stringValues[i].substring(startIndex, stopIndex));
						values[n] = thisVal;
						//handles the value and the comma
						startIndex = startIndex + 2;
						stopIndex = startIndex + 1;
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
		
				System.out.println("NODES:");
				for (Node n: nodes) {
					System.out.println(n.getName());
				}
				
				System.out.println("EDGES");
				for (Edge e: edgesArray) {
					e.printFactors();
				}			
				
				
		initializeVelocityAndPosition();
	}
	
	/**
	 * Updates particle velocity
	 * @param omega
	 * @param phi1
	 * @param phi2
	 * @param gBest
	 */
	public void updateVelocity(double omega, double phi1, double phi2, int[] gBest) {
		// decide on multipliers
		double cognitive = Math.random() * phi1;
		double social = Math.random() * phi2;
		
		for (int i = 0; i < velocity.length; i++){
			velocity[i] = (omega * velocity[i]) + (cognitive * pBest[i]) + (social * gBest[i]);
		}
	}

	
	/**
	 * Randomly initializes velocity and position vectors
	 */
	private void initializeVelocityAndPosition(){
		
		velocity = new double[nodes.size()] ;
		position = new int[nodes.size()] ;		
		
		for (int i = 0; i < velocity.length; i++){
			position[i] = (int)(Math.random()*10);
			velocity[i] = Math.random();
		}
		
	}
	
	/**
	 * Updates particle position
	 */
	public void updatePosition(){
		
		for (int i = 0; i < position.length; i++){
			double move = position[i] + velocity[i];
			
			int smallestVal = Integer.MAX_VALUE;
			int largestVal = Integer.MIN_VALUE;
			
			for (int j = 0; j < nodes.get(i).getVals().length; j++) {
				
				if (nodes.get(i).getSpecificVal(j) < smallestVal) {
					smallestVal = nodes.get(i).getSpecificVal(j);
				} else if (nodes.get(i).getSpecificVal(j) > largestVal)  {
					largestVal = nodes.get(i).getSpecificVal(j);
				}
			}
			
			// snap to nearest integer
			if (move - Math.floor(move) > 0.5){
				position[i] = (int)Math.ceil(move);
			} else {
				position[i] = (int)Math.floor(move);
			}
			
			// snap position back to smallest or largest value, when gets too big
			if (position[i] < smallestVal) {
				position[i] = smallestVal;
			} else if (position[i] > largestVal) {
				position[i] = largestVal;
			}
			
		}
	}
	
	/**
	 * Builds a sample using the current particle position
	 * @return
	 */
	public Sample buildSampleFromPosition(){
		Sample s = new Sample();
		
		for (int i = 0; i < nodes.size(); i++){
			s.setSampledValue(nodes.get(i), position[i]);
		}
		
		return s;
	}
	
	
	/**
	 * Calculates particle's fitness
	 * @return
	 */
	public double calcFitness(){
		// build a sample
		Sample s = buildSampleFromPosition();
		double fit = f.calcFitness(s);
		
		
		// check constraints
		if (!checkConstraints(s)){
			// penalize if doesn't pass constraints
			fit -= 100;
			s.setFitness(fit);
		} 
		
		// set pBest
		// TODO: refactor, this only works for a max problem
		if (pBest == null || fit > pBest_fitness){
			pBest_fitness = fit;
			pBest = new int[position.length];
			// copy this position
			for (int i = 0; i < pBest.length; i++){
				pBest[i] = position[i];
			}
		}

		return fit;
	}
	
	/**
	 * TODO: move this into the fitness function class or something
	 * Returns TRUE if all constraints are passed, FALSE otherwise
	 * @return
	 */
	public boolean checkConstraints(Sample s){
		// only graph coloring constraints, for now
		for (Edge e: edgesArray){
			// invalid if any neighboring nodes have the same color
			if (s.getTable().get(e.getEndpoints().getFirst()) == s.getTable().get(e.getEndpoints().getLast())){
				return false;
			}
		}		
		return true;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}
	
	public void print(){
		System.out.println("Fitness: " + calcFitness());
	}
}
