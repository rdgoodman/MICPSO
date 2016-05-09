package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.Sample;
import comparisons.GreedyVC;

public class GreedyVCTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor10Node2_30Values_NP.txt");
		GreedyVC g = new GreedyVC(filename);

		double avg = 0;

		for (int i = 0; i < 50; i++) {
			Sample s = g.run();
			//s.print();

			assertEquals(true, g.getProblem().satisfiesConstraints(s, g.getMN().getEdges()));
			System.out.println("Fitness: " + g.getProblem().getFitnessFunction().calcFitness(s));
			avg += g.getProblem().getFitnessFunction().calcFitness(s);
		}
		
		avg /= 50;
		System.out.println("\n *** Average: " + avg);
	}

}
