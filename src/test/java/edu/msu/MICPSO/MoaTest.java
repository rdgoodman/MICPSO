package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import MN.Sample;
import comparisons.MOA;

public class MoaTest {

	@Test
	public void testSample() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor05Node_10Values.txt");
		
		// TODO: set according to paper
		MOA m = new MOA(filename, 0.5, 3, 100, 0.5);
		
		Sample s = m.sample(1);
		s.print();
	}

}
