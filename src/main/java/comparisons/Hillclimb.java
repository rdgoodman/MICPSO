package comparisons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import MN.MarkovNetwork;
import MN.Sample;
import PSO.FitnessFunction;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;

public class Hillclimb {

	private MarkovNetwork mn; // just to store the nodes/edges of graph
	ApplicationProblem problem;
	// TODO: will need a termination criterion, same as anything else
	private double numToConsiderConverged = 20;
	private double threshold = 0.01;

	FitnessFunction f;

	public Hillclimb(String fileName) throws FileNotFoundException {
		mn = new MarkovNetwork(fileName);

		// TODO: create problem
		// based on info about problem type from file,
		// create a fitness function
		Scanner s = null;

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// Reads in the nodes, edges and values in from a specifically formatted
		// file
		// as well as problem type
		int optimal;
		String probType;
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
			// Read the first line in the file
			potential = s.nextLine();

			// gets the node info first
			// checks for comments, when present, discards them
			while (potential.startsWith("%")) {
				potential = s.nextLine();
			}

			// only getting type of problem and optimal size
			probType = potential;

			// keep scanning for the next non-empty line
			if (s.nextLine().equals("")) {
				potential = s.nextLine();
			}

			// checks for comments, when present, discards them
			while (potential.startsWith("%")) {
				potential = s.nextLine();
			}

			// gets optimal solution size
			optimal = Integer.valueOf(potential);
			System.out.println("Size: " + optimal);

			// TODO: create problem
			if (probType.equals("GC")) {
				problem = new GraphColoringProblem(optimal);
			}

		} finally {
			if (s != null) {
				s.close();
			}
		}

		f = problem.getFitnessFunction();
	}

	public Sample run() {
		// termination criterion
		int runsUnchanged = 0;
		boolean terminated = false;

		// start with a randomly generated sample
		Sample s = mn.createRandomSample();

		f.calcFitness(s);
		if (!problem.satisfiesConstraints(s, mn.getEdges())) {
			s.setFitness(-100.0);
		}
		double prevBestSampleFit = s.getFitness();

		// if (!problem.satisfiesConstraints(s, mn.getEdges())){
		// throw new RuntimeException("Started with an invalid solution");
		// // TODO: can probably get rid of this, might not make sense
		// // for all applications
		// }

		while (!terminated) {
			Sample n = problem.generateNeighbor(s);

			double currentFit = f.calcFitness(s);
			double neighborFit = f.calcFitness(n);

			// penalizes
			// TODO: ensure this is consistent across applications
			// maybe move into problem class, honestly
			if (!problem.satisfiesConstraints(s, mn.getEdges())) {
				currentFit = -100;
				s.setFitness(currentFit);
			}

			if (!problem.satisfiesConstraints(n, mn.getEdges())) {
				neighborFit = -100;
				n.setFitness(neighborFit);
			}
			
			prevBestSampleFit = currentFit;

			//System.out.println("Current: " + currentFit + ", Neighbor: " + neighborFit);
			if (problem.compare(currentFit, neighborFit) == 1) {
				// accept neighbor if better than current
				s = n;
			}
			
			// Next half-dozen or so lines used to determine convergence
			if (Math.abs(prevBestSampleFit - s.getFitness()) < threshold) {
				runsUnchanged++;
			} else {
				runsUnchanged = 0;
			}
			if (runsUnchanged >= numToConsiderConverged) {
				// return if the solution hasn't significantly changed in a
				// certain
				// number of iterations
				terminated = true;
			}
		}

		return s;
	}

	public MarkovNetwork getMN() {
		return mn;
	}

	public ApplicationProblem getProblem() {
		return problem;
	}
}
