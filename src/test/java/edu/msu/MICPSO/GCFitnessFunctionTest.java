package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.PairwiseMarkovNetwork;
import MN.Sample;
import PSO.GCFitnessFunction;

public class GCFitnessFunctionTest {
	
	// The filePath and fileName where the Markov net file is located
	String filePath = "src/main/resources/";
	String fileName = "markovNet.txt";
	
	// for passing in to MarkovNetwork or ICParticle
	String inputFile = filePath + fileName;

	@Test
	public void test() throws FileNotFoundException {
		PairwiseMarkovNetwork m = new PairwiseMarkovNetwork(inputFile);
		GCFitnessFunction f = new GCFitnessFunction(3);
        
        // samples the above network using Gibbs sampling
        Sample s = m.sample();
        double fit = f.calcFitness(s);
        System.out.println("Fitness: " + fit);
        
        
	}

}
