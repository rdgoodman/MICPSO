package comparisons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import MN.PairwiseMarkovNetwork;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;
import applicationProblems.MaxSatProblem;

public abstract class Optimizer implements OptimizationAlgorithm {

	/**
	 * Handles creating the optimization problem instance. Reads the file,
	 * ignoring lines with % (which are comment lines). File is structured so
	 * that the nodes are first (comma separated), followed by the edges (in
	 * form A, B semi-colon separated) and then the values for the variables (in
	 * form A: 0, 1). The values need to be in the same order as the node
	 * variables.
	 * 
	 * @param file
	 *            the file where the problem info can be found
	 * @throws FileNotFoundException
	 */
	public ApplicationProblem constructProblemFromFile(File file) throws FileNotFoundException {
		ApplicationProblem problem = null;

		// based on info about problem type from file,
		// create a fitness function
		Scanner s = null;

		// Reads in the nodes, edges and values in from a specifically formatted
		// file
		// as well as problem type
		int optimal;
		String probType;
		try {
			s = new Scanner(new BufferedReader(new FileReader(file)));

			String potential;

			// Read the first line in the file
			potential = s.nextLine();

			// gets the node info first
			// checks for comments, when present, discards them
			while (potential.startsWith("%")) {
				potential = s.nextLine();
			}

			// only getting type of problem and optimal size
			probType = potential;

			if (probType.equals("GC")) {

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

				// create problem
				if (probType.equals("GC")) {
					problem = new GraphColoringProblem(optimal);
				}
			} else {
				// TODO: this is not good
				// we need this to be the same set of predicates used 
				// in each MN particle somehow...FUCK.
				// or, store predicates
				// and make sure we only have one set...
				PairwiseMarkovNetwork mn = new PairwiseMarkovNetwork(file.getPath());
				problem = mn.getProblem();
			}

		} finally {
			if (s != null) {
				s.close();
			}
		}

		return problem;
	}

}
