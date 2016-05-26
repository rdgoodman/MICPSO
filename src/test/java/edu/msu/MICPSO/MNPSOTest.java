package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.Sample;
import PSO.ICPSO;

public class MNPSOTest {

	@Test
	public void test() throws FileNotFoundException {
		double average = 0;
		for (int i = 0; i < 10; i++) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> " + i);
			String filename = ("src/main/resources/BigMaxSatTestFile3.txt");
			// String filename =
			// ("src/main/resources/graphColor10Node_30Values.txt");
			ICPSO p = new ICPSO(filename, true, 10, 3, 0.99, 0.4, 0.6, 0.6);
			Sample s = p.run();
			assertEquals(true, p.getProblem().satisfiesConstraints(s, s.getEdges()));

			// TODO: fix this issue for ICPSO
			System.out.println("Stored fitness: " + s.getFitness());
			System.out.println("Calculated fitness: " + p.getProblem().getFitnessFunction().calcFitness(s));
			average += s.getFitness();

		}
		System.out.println("Average: " + (average / 10));
	}

}
