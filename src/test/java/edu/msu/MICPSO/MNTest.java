package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.PairwiseMarkovNetwork;
import MN.Sample;

public class MNTest {

	// @Test
	// public void test1() throws FileNotFoundException {
	// String filename = "src/main/resources/maxSatTestFile2.txt";
	// PairwiseMarkovNetwork net = new PairwiseMarkovNetwork(filename);
	// }

	@Test
	public void test2() throws FileNotFoundException {
		String filename = "src/main/resources/myciel4_converted.txt";
//		 String filename = "src/main/resources/myciel3_converted.txt";

		PairwiseMarkovNetwork net = new PairwiseMarkovNetwork(filename);		
		

		// Sample s = net.sample();
		// s.print();

		// System.out.println("\n +++++++++++++++++++++++++++++");
		// System.out.println("++++++++ Valid Sample ++++++++");
		// System.out.println(" +++++++++++++++++++++++++++++ \n");
		//
		// Sample s2 = net.createRandomValidSample();
		// s.print();
	}

}
