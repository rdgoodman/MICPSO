package comparisons;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import MN.MarkovNetwork;
import MN.Sample;
import applicationProblems.ApplicationProblem;

public class MOA {
	
	// pop: group of samples? or constructed from samples?
	// single MN object, no need to rebuild BC no structure learning aspect
	
	private MarkovNetwork mn;
	private double cr; // cooling rate for gibbs sampling
	private ArrayList<Sample> pop;
	private int numIterations; // I think this is for Gibbs sampling? (their version)
	private int numToSelect;
	
	private ApplicationProblem problem;
	
	public MOA(String filename, double cr, int numIterations, int popSize, int numToSelect) throws FileNotFoundException{
		// TODO: new MN constructor?		
		mn = new MarkovNetwork(filename);
		pop = new ArrayList<Sample>();
		this.cr = cr;
		this.numIterations = numIterations;
		this.numToSelect = numToSelect;
		
		createPopulation(popSize);
	}

	/** 
	 * Creates a randomly initialized population to start
	 * @param popSize
	 */
	private void createPopulation(int n) {
		// creates n randomly (uniformly) initialized samples
		for (int i = 0; i < n; i++){
			pop.add(mn.createRandomSample());
		}
	}
	
	/**
	 * Runs optimization and returns best sample
	 * @return
	 */
	public Sample run(){
		// termination criterion
		// TODO: set this up
		int runsUnchanged = 0;
		boolean terminated = false;
		
		while (!terminated){
			
			// select a set of solutions
			ArrayList<Sample> selected = new ArrayList<Sample>();
			
		}
		
		// TODO: sort population by fitness
		Collections.sort(pop);
		// note: ascending
		
		if (problem.isMaxProblem()){
			return pop.get(0);
		} else {
			return pop.get(pop.size()-1);
		}
	}

}
