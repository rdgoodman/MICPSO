package comparisons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import MN.MarkovNetwork;
import MN.Node;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;

public class GreedyVC {

	private MarkovNetwork mn; // just to store the nodes/edges of graph
	ApplicationProblem problem;

	public GreedyVC(String fileName) throws FileNotFoundException {
		mn = new MarkovNetwork(fileName);

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// construct the optimization problem itself
		constructProblemFromFile(file);
	}
	
	/**
	 * Handles for creating the optimization problem instance. Reads the file,
	 * ignoring lines with % (which are comment lines). File is structured so
	 * that the nodes are first (comma separated), followed by the edges (in
	 * form A, B semi-colon separated) and then the values for the variables (in
	 * form A: 0, 1). The values need to be in the same order as the node
	 * variables.
	 * 
	 * @param file the file where the problem info can be found
	 * @throws FileNotFoundException
	 */
	private void constructProblemFromFile(File file) throws FileNotFoundException {
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

		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	public Sample run() {
		Sample s = new Sample();

		// create random ordering of vertices
		ArrayList<Node> vertices = mn.getNodes();
		Collections.shuffle(vertices);

		// color each one with a free color (can just check Vals bc all same)
		for (Node v : vertices) {
			// colors of the MB
			ArrayList<Integer> takenColors = new ArrayList<Integer>();
			ArrayList<Node> MB = v.getMB();

			for (Node m : MB) {
				if (s.getTable().get(m) != null) {
					takenColors.add(s.getTable().get(m));
				}
			}

			// choose a free color
			int[] vals = v.getVals();
			for (int i = 0; i < vals.length; i++) {
				if (!takenColors.contains(vals[i])) {
					s.setSampledValue(v, vals[i]);
					break;
				}
			}
			// check this
			if (s.getTable().get(v) == null) {
				throw new RuntimeException("Ran out of colors");
			}
		}

		// calculates fitness of returned solution
		s.setFitness(problem.getFitnessFunction().calcFitness(s));
		if (!problem.satisfiesConstraints(s, mn.getEdges())){
			s.setFitness(s.getFitness() + problem.getInvalidSolutionPenalty());
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
