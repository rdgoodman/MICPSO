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

	private Sample pBest;
	private Node[] variables;
	private ProbDist[] probs;
	private FitnessFunction f;

	public ICParticle(String fileName, FitnessFunction f) throws FileNotFoundException {
		this.f = f;
		
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateVelocity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPBest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustPBest() {
		// TODO Auto-generated method stub

	}

	@Override
	public Particle copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bias(double epsilon) {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		for (int i = 0; i < probs.length; i++){
			probs[i].print();
		}
	}

}
