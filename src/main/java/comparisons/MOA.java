package comparisons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;

import MN.MarkovNetwork;
import MN.Node;
import MN.ProbDist;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;

public class MOA extends Optimizer{

	private MarkovNetwork mn;
	private double cr; // cooling rate for gibbs sampling
	// per paper, cr = 0.5
	private ArrayList<Sample> pop;
	private int numIterations; // I think this is for Gibbs sampling? 
	private double percentToSelect;

	private ApplicationProblem problem;
	
	// termination criteria
	private double numToConsiderConverged = 20;
	private double threshold = 0.01;

	public MOA(String fileName, double cr, int popSize, double percentToSelect)
			throws FileNotFoundException {
		mn = new MarkovNetwork(fileName);
		pop = new ArrayList<Sample>();
		this.cr = cr;
		
		this.percentToSelect = percentToSelect;

		if (percentToSelect > 1.0) {
			throw new RuntimeException("Invalid selection percentage for MOA: must be in (0,1]");
		}

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// construct the optimization problem itself
		problem = constructProblemFromFile(file);

		createPopulation(popSize);
	}

	/**
	 * Creates a randomly initialized population to start
	 * 
	 * @param popSize
	 */
	private void createPopulation(int n) {
		// creates n randomly (uniformly) initialized samples
		for (int i = 0; i < n; i++) {
			Sample s = mn.createRandomValidSample();
			pop.add(s);
			// evaluates samples as they're created
			problem.getFitnessFunction().calcFitness(s);
		}

		// TODO: testing, remove
		//System.out.println(">>>>> Population size:" + pop.size());
		for (Sample s : pop) {
			if (!problem.satisfiesConstraints(s, mn.getEdges())) {
				throw new RuntimeException("Invalid individual in population");
			}
			// s.print();
		}
	}

	/**
	 * Runs optimization and returns best sample
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Sample run() {
		// termination criterion
		int runsUnchanged = 0;
		boolean terminated = false;
		double bestFitness = problem.getWorstValue(); // initial best
		double prevBestFitness = problem.getWorstValue();

		int iteration = 0;
		while (!terminated) {
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("%%%%%% Iteration " + iteration);
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%");


			// sort the population
			Collections.sort(pop);

			// update previous best fitness
			if (!problem.isMaxProblem()) {
				prevBestFitness = pop.get(0).getFitness();
			} else {
				prevBestFitness = pop.get(pop.size() - 1).getFitness();
			}
//			System.out.println("Previous best fitness: " + prevBestFitness);
//			System.out.println("Worst fitness: " + pop.get(0).getFitness());
					
			// select a set of solutions
			ArrayList<Sample> selected = truncationSelect();
			

			// generate the correct number of samples
			ArrayList<Sample> newSamples = new ArrayList<Sample>();
			for (int i = 0; i < selected.size(); i++) {
				newSamples.add(sample(iteration, selected));
				System.out.println("new samples: " + (i+1));
			}

			// evaluate childrens' fitness
			for (Sample s : newSamples){
				problem.getFitnessFunction().calcFitness(s);
			}
			
			// remove parents from population
			//pop.removeAll(selected);
			
			// TODO: remove worst instead of parents
			removeWorst(newSamples.size());
			
			// TODO: testing, remove
//			Collections.sort(newSamples);
//			System.out.println("New Samples: ");
//			String str = "";
//			for (Sample sa: newSamples){
//				str += sa.getFitness();
//				str += " ";
//			}
//			System.out.println(str);
			
			// replace with children
			pop.addAll(newSamples);
			
			// sort the population
			Collections.sort(pop);
			
			// TODO: testing, remove
//			System.out.println("Population: ");
//			String string = "";
//			for (Sample sa: pop){
//				string += sa.getFitness();
//				string += " ";
//			}
//			System.out.println(string);
			System.out.println("Best sample: " + pop.get(pop.size() - 1).getFitness());
//			pop.get(pop.size() - 1).print();
			
			// update best
			if (!problem.isMaxProblem()) {
				bestFitness = problem.getFitnessFunction().calcFitness(pop.get(0));
			} else {
				bestFitness = problem.getFitnessFunction().calcFitness(pop.get(pop.size() - 1));
			}
		//	System.out.println("Best fitness: " + bestFitness);

			// checks termination criterion
			if (Math.abs(bestFitness - prevBestFitness) < threshold){
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
		//	System.out.println("Unchanged: " + runsUnchanged);
				
			iteration++;
		}

		// sort population by fitness
		Collections.sort(pop);
		// note: ascending
		if (!problem.isMaxProblem()) {
			return pop.get(0);
		} else {
			return pop.get(pop.size() - 1);
		}
	}

	/**
	 * Uses truncation selection to select a proportion of the population
	 * 
	 * @return
	 */
	private ArrayList<Sample> truncationSelect() {
		// the original authors used truncation selection, so...
		int numToSelect = (int) (pop.size() * percentToSelect);
		//System.out.println("Selecting " + numToSelect);

		ArrayList<Sample> selected = new ArrayList<Sample>();

		if (problem.isMaxProblem()) {
			// recall: sorted in ascending order
			for (int i = 0; i < numToSelect; i++) {
				// does not remove parents, as this could mess with Gibbs
				// sampling
				selected.add(pop.get((pop.size() - 1) - i));
			}
		} else {
			// recall: sorted in ascending order
			for (int i = 0; i < numToSelect; i++) {
				// does not remove parents, as this could mess with Gibbs
				// sampling
				selected.add(pop.get(i));
			}
		}
		return selected;
	}
	
	/**
	 * Removes the worst solutions in the population
	 */
	private void removeWorst(int numToRemove){
		
		if (!problem.isMaxProblem()) {
			// recall: sorted in ascending order
			for (int i = 0; i < numToRemove; i++) {
				pop.remove(pop.get((pop.size() - 1) - i));
			}
		} else {
			// recall: sorted in ascending order
			for (int i = 0; i < numToRemove; i++) {
				pop.remove(pop.get(i));
			}
		}
	}

	/**
	 * Returns the result of MOA's version of Gibbs sampling
	 * 
	 * @return
	 */
	public Sample sample(int g, ArrayList<Sample> selected) {

		int numAttempted = 0;

		// calculate temperature
		double T = 1 / ((g + 1) * cr);

		// generate random initial solution
		Sample s = mn.createRandomSample();
		// System.out.println("- - - - - - - - - - ");
		// System.out.println("Initial sample:");
		// s.print();
		// System.out.println("- - - - - - - - - - ");

		// calcualte number of iterations
		numIterations = (int) (selected.size() * Math.log(selected.size()) * 4);
		
		for (int i = 0; i < numIterations; i++) {
			// System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			// System.out.println("%%%%%%% Iteration " + i);
			// System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

			// chooses nodes at random
			ArrayList<Node> nodes = mn.getNodes();
			Collections.shuffle(nodes);

			// resample each node
			for (Node N : nodes) {
				// System.out.println("\n Resampling Node " + N.getName());
				// calculate p ( n | MB(n) )
				ArrayList<Node> MB = N.getMB();

				// stores the distribution generated by calculations
				ProbDist probs = new ProbDist(N.getVals(), N);

				// for each value of N
				int[] nVals = N.getVals();
				// each entry is count where N=nVals[n] and MB is from sample
				double[] counts = new double[nVals.length];

				for (int n = 0; n < nVals.length; n++) {
					for (Sample a : selected) {
						Hashtable<Node, Integer> t = a.getTable();

						if (t.get(N) == nVals[n]) { // nCount
							boolean increment = true;
							for (Node m : MB) {
								if (t.get(m) != s.getTable().get(m)) {
									increment = false;
								}
							}
							// increment count if all MB vars
							// have the same values as the sample
							// and N = nVals[n]
							if (increment) {
								counts[n]++;
							}
						}
					}
				}

				// System.out.println("Counts: (given MB)");
				// for (int q = 0; q < counts.length; q++) {
				// System.out.println(nVals[q] + ": " + counts[q]);
				// }

				// for each value of N
				for (int n = 0; n < nVals.length; n++) {
					// calc p (n | MB(N))
					double numerator = 0;
					double denominator = 0;

					numerator = Math.exp((counts[n] / selected.size()) / T);

					// getting the denominator. ugh.
					for (int prime = 0; prime < nVals.length; prime++) {
						// if (prime != n) { // don't double-count
						denominator += Math.exp((counts[prime] / selected.size()) / T);
						// }
					}

					if (denominator == 0.0) {
						denominator = 1.0; // avoid divide-by-zero
					}

					// update Probs
					double prob = numerator / denominator;
					// System.out.println(prob);
					probs.setProb(nVals[n], probs.getProb(nVals[n]) * prob);
					// System.out.println(probs.getProb(nVals[n]));
					// System.out.println("Prob == " + nVals[n] + ": " +
					// probs.getProb(nVals[n]));

				}

				// renormalize (should already be handled by the sampler,
				// but...check?)
				probs.normalize();
				// probs.print();

				// TODO: set value of factor?

				// resample
				s.setSampledValue(N, probs.sample());

				// System.out.println("Final prob dist:");
				// probs.print();
			}

		}

		// deals with the fact that this can't handle constraints
		if (!problem.satisfiesConstraints(s, mn.getEdges())) {
			s = sample(g, selected);
			numAttempted++;
		}

		if (numAttempted > 1000) {
			throw new RuntimeException("Tried too many");
			// TODO: restart
		}

		return s;
	}

	public ArrayList<Sample> getPop() {
		return pop;
	}

	public ApplicationProblem getProblem() {
		return problem;
	}

	public MarkovNetwork getMN() {
		return mn;
	}

}
