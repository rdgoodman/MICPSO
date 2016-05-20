package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.MarkovNetwork;

public class MNTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = "src/main/resources/maxSatTestFile2.txt";
		
		MarkovNetwork net = new MarkovNetwork(filename);
	}

}
