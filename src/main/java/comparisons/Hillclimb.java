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

public class Hillclimb extends Optimizer{

	private MarkovNetwork mn; // just to store the nodes/edges of graph
	ApplicationProblem problem;
	// needs a termination criterion, same as anything else
	private double numToConsiderConverged = 20;
	private double threshold = 0.01;

	FitnessFunction f;

	public Hillclimb(String fileName) throws FileNotFoundException {
		mn = new MarkovNetwork(fileName);

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);
		
		// construct the optimization problem itself
		problem = constructProblemFromFile(file);

		f = problem.getFitnessFunction();
	}
	
	public Sample run() {
		// termination criterion
		int runsUnchanged = 0;
		boolean terminated = false;

		// start with a randomly generated sample
		Sample s = mn.createRandomValidSample();

		f.calcFitness(s);
		if (!problem.satisfiesConstraints(s, mn.getEdges())) {
			s.setFitness(-100.0);
		}
		double prevBestSampleFit = s.getFitness();

		while (!terminated) {
			Sample n = problem.generateNeighbor(s);

			double currentFit = f.calcFitness(s);
			double neighborFit = f.calcFitness(n);

			
			// penalizes
			if (!problem.satisfiesConstraints(s, mn.getEdges())) {
				s.setFitness(currentFit + problem.getInvalidSolutionPenalty());
			}

			if (!problem.satisfiesConstraints(n, mn.getEdges())) {
				n.setFitness(neighborFit + problem.getInvalidSolutionPenalty());
			}
			
			prevBestSampleFit = s.getFitness();
			
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
