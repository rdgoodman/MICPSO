package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.Sample;
import comparisons.Hillclimb;

public class HillclimbTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor08Node_30Values.txt");
		Hillclimb h = new Hillclimb(filename);
		
		Sample s = h.run();
		System.out.println("\n");
		s.print();
//		double sFitness = -100;
//		
//		if (h.getProblem().satisfiesConstraints(s, h.getMN().getEdges())){
//			sFitness = h.getProblem().getFitnessFunction().calcFitness(s);
//		}
		
		System.out.println("Fitness: " + s.getFitness());

	}

}
