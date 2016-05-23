package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.Sample;
import PSO.ICPSO;
import PSO.IPSO;

public class PSOTest {

	// @Test
	// public void testICPSO() throws FileNotFoundException {
	// String filename = ("src/main/resources/graphColor05Node_30Values.txt");
	//
	// // String fileName, boolean Markov, int numParticles, int numSamples,
	// double epsilon, double omega,
	// //double phi1, double phi2
	//
	// ICPSO p = new ICPSO(filename, false, 5, 3, 0.9, 0.4, 0.6, 0.6);
	// Sample s = p.run();
	// s.print();
	// }

	@Test
	public void testIPSO() throws FileNotFoundException {
//		String filename = ("src/main/resources/graphColor05Node2_30Values.txt");
		String filename = ("src/main/resources/maxSatTestFile2.txt");

		// String fileName, boolean Markov, int numParticles, int numSamples,
		// double epsilon, double omega,
		// double phi1, double phi2

		//for (int i = 0; i < 100; i++) {
			IPSO p = new IPSO(filename, 10, 1.0, 1.4, 1.4);
			Sample s = p.run();
			System.out.println("\n");
			s.print();
			System.out.println("Fitness: " + s.getFitness());
			
			System.out.println("Stored fitness: " + s.getFitness());
			System.out.println("Calculated fitness: " + p.getProblem().getFitnessFunction().calcFitness(s));
			
			//assertEquals(true, s.getFitness() <= 0);
		//}
	}

}
