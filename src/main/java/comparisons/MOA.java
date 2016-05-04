package comparisons;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import MN.MarkovNetwork;
import MN.Sample;

public class MOA {
	
	// pop: group of samples? or constructed from samples?
	// single MN object, no need to rebuild BC no structure learning aspect
	
	private MarkovNetwork mn;
	private double cr; // cooling rate for gibbs sampling
	private ArrayList<Sample> pop;
	private int numIterations;
	
	public MOA(String filename, double cr, int numIterations, int popSize) throws FileNotFoundException{
		// TODO: new MN constructor?		
		mn = new MarkovNetwork(filename);
		pop = new ArrayList<Sample>();
		createPopulation(popSize);
		this.cr = cr;
		this.numIterations = numIterations;
	}

	/** 
	 * Creates a randomly initialized population to start
	 * @param popSize
	 */
	private void createPopulation(int popSize) {
		// TODO Auto-generated method stub
		
	}

}
