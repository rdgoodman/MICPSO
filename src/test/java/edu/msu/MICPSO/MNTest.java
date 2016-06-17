package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.PairwiseMarkovNetwork;

public class MNTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = "src/main/resources/maxSatTestFile2.txt";
		
		PairwiseMarkovNetwork net = new PairwiseMarkovNetwork(filename);
	}
	


}
