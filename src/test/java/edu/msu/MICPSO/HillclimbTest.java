package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.Sample;
import comparisons.Hillclimb;

public class HillclimbTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor38Node_30Values.txt");
		Hillclimb h = new Hillclimb(filename);

		double avg = 0;
		int validCount = 0;
		for (int i = 0; i < 10; i++) {
			Sample s = h.run();

			 System.out.println( i + ": fitness: " + s.getFitness());

			if (s.getFitness() > -100) {
				avg += s.getFitness();
				validCount++;
			}
		}

		System.out.println("\n *** Average: ");
		System.out.println(avg / validCount);
		System.out.println("\n *** Valid %: ");
		System.out.println((double)validCount/10);
	}

}
