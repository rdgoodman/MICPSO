package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import comparisons.MOA;

public class MoaTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor05Node_30Values.txt");
		
		// TODO: set according to paper
		MOA m = new MOA(filename, 0.5, 100, 50, 0.5);
	}

}
