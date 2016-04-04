package PSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import MN.Node;
import MN.ProbDist;
import MN.Sample;

public class ICParticle implements Particle {

	private Sample pBest_sample;
	private ICParticle pBest_position;
	private Node[] variables;
	private ProbDist[] probs;

	// TODO; need to add in velocity term, for fucks sake
	
	private FitnessFunction f;
	private int numSamples;
	private double epsilon;

	public ICParticle(String fileName, FitnessFunction f, int numSamples, double epsilon) throws FileNotFoundException {
		this.f = f;
		this.numSamples = numSamples;
		this.epsilon = epsilon;
		
		// TODO: read stuff from file. It's hard.

		// procedure:

		// 1) create Nodes
		// 2) foreach Node, create a ProbDist with its list of values and itself
		
		///////////////////////////////////////////////////////////

		// Arrays for storing the string values read in from file
		String[] stringNodes = null;
		String[] stringValues = null;
		// the largest number of values associated with any node
		int numValues = 0;

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
					int tempNumValues = 0;
					startOfValues = stringValues[i].lastIndexOf(":");
					stringValues[i] = stringValues[i].substring(startOfValues + 1, stringValues[i].length());
					// trims extra whitespace from edge objects
					if (stringValues[i].startsWith(" ")) {
						stringValues[i] = stringValues[i].trim();
					}

					tempNumValues = stringValues[i].split(",").length;
					// finds the largest number of values for any nodes (for use
					// in
					// initializing the Node objects)
					if (tempNumValues > numValues) {
						numValues = tempNumValues;
					}
				}
			}

		} finally {
			if (s != null) {
				s.close();
			}
		}
	}
	
	
	/**
	 * This constructor is intended for use with the copy() method
	 * @param f
	 */
	public ICParticle(FitnessFunction f, Sample pBest_sample){
		this.f = f;
		this.pBest_sample = pBest_sample;
	}

	@Override
	public Sample sample() {
		Sample s = new Sample();
		
		// builds a sample by independently sampling every distribution
		for (int i = 0; i < probs.length; i++){
			double val = probs[i].sample();
			s.setSampledValue(probs[i].getNode(), val);
		}
		
		return s;
	}

	@Override
	public double calcFitness() {		
		double particleFit = 0;
		// average over a certain number of samples
		for (int i = 0; i < numSamples; i++){
			// 1) generate a sample and calculate its fitness
			Sample s = sample();
			double fit = f.calcFitness(s);
			particleFit += fit;
			
			// 2) Save this sample if it's the new pBest
			if (pBest_sample == null | fit > pBest_sample.getFitness()){ 
				// TODO: recall that this assumes a max problem, refactor later
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
		
		// update each prob dist
		for (int i = 0; i < probs.length; i++){
			// call update for each element
			probs[i].updateVelocity(omega, cognitive, social, ((ICParticle) gBest).getProbs()[i], pBest_position.getProbs()[i]);
		}

		// update each CPD
//		for (int i = 0; i < numYears; i++) {
//			for (int j = 0; j < numObjects; j++) {
//				// 1) extract the relevant part of gBest
//				Dist g = gBest[i][j];
//				// 2) extract relevant part of pBest
//				Dist p = pBest[i][j];
//				// 3) send to CPD updater
//				position[i][j].updateVelocity(omega, cognitive, social, g, p);
//			}
//		}
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPBest(Sample s) {
		pBest_sample = s;
		adjustPBest();
		// TODO: set pBest dist!
		pBest_position = this.copy();
	}

	@Override
	public void adjustPBest() {
		// call bias() method for each variable
		for (int i = 0; i < probs.length; i++){
			// retrieves value associated with this dist's node in sample
			double k = pBest_sample.getTable().get(probs[i].getNode());
			// calls bias() with that value
			probs[i].bias(k, epsilon);
		}
	}


	@Override
	public void bias(double epsilon) {
		// TODO Auto-generated method stub

	}

	@Override
	public  ICParticle copy() {
		ICParticle cp = new ICParticle(f, pBest_sample);
		
		ProbDist[] cpProbs = new ProbDist[probs.length];
		
		for (int i = 0; i < cpProbs.length; i++){
			cpProbs[i] = probs[i].copy();
		}
		
		cp.setDist(cpProbs);
		
		return cp;
	}

	@Override
	public void print() {
		for (int i = 0; i < probs.length; i++){
			probs[i].print();
		}
	}
	
	public void setDist(ProbDist[] probs){
		this.probs = probs;
	}


	public ProbDist[] getProbs() {
		return probs;
	}


}
