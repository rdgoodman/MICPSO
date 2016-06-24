package comparisons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import MN.PairwiseMarkovNetwork;
import MN.Node;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;

public class GreedyVC extends Optimizer {

	private PairwiseMarkovNetwork mn; // just to store the nodes/edges of graph
	ApplicationProblem problem;

	public GreedyVC(String fileName) throws FileNotFoundException {
		mn = new PairwiseMarkovNetwork(fileName);

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// construct the optimization problem itself
		problem = constructProblemFromFile(file);
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
		
		
		System.out.println("Returned solution " + s.getFitness());
		return s;
	}

	public PairwiseMarkovNetwork getMN() {
		return mn;
	}

	public ApplicationProblem getProblem() {
		return problem;
	}

}
