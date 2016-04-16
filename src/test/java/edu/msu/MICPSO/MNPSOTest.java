package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import PSO.ICPSO;

public class MNPSOTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor08Node.txt");
		ICPSO p = new ICPSO(filename, true, 10, 3, 0.99, 0.4, 0.6, 0.6);
		p.run();
	}

}
