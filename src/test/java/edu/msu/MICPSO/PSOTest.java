package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import PSO.ICPSO;
import PSO.IPSO;

public class PSOTest {

//	@Test
//	public void testICPSO() throws FileNotFoundException {
//		String filename = ("src/main/resources/graphColor08Node_30Values.txt");
//		
//		// String fileName, boolean Markov, int numParticles, int numSamples, double epsilon, double omega,
//		//double phi1, double phi2
//		
//		ICPSO p = new ICPSO(filename, false, 5, 3, 0.9, 0.4, 0.6, 0.6);
//		p.run();
//	}
	
	@Test
	public void testIPSO() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor08Node_30Values.txt");
		
		// String fileName, boolean Markov, int numParticles, int numSamples, double epsilon, double omega,
		//double phi1, double phi2
		
		IPSO p = new IPSO(filename, 5, 0.4, 0.6, 0.6);
		p.run();
	}

}
