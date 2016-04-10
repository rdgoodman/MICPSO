package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import PSO.ICPSO;

public class PSOTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = ("src/main/resources/markovNet.txt");
		
		// String fileName, boolean Markov, int numParticles, int numSamples, double epsilon, double omega,
		//double phi1, double phi2
		
		ICPSO p = new ICPSO(filename, false, 2, 3, 0.05, 0.4, 0.7, 0.7);
		p.run();
	}

}
