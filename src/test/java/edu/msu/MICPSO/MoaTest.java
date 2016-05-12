package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import MN.Sample;
import comparisons.MOA;

public class MoaTest {
	
	@Test
	public void testValidInit() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor05Node_10Values.txt");	
		MOA m = new MOA(filename, 0.5, 100, 50, 0.5);
		
		ArrayList<Sample> samples = m.getPop();
		
		for (Sample s : samples){
			assertEquals(true, m.getProblem().satisfiesConstraints(s, m.getMN().getEdges()));
		}
		
	}

	@Test
	public void testSample() throws FileNotFoundException {
		String filename = ("src/main/resources/graphColor38Node_30Values.txt");
		
		// TODO: set according to paper
		MOA m = new MOA(filename, 0.5, 100, 50, 0.5);
		
		ArrayList<Sample> samples = m.getPop();	
		
		Sample s = m.sample(10, m.getPop());
		System.out.println("\n Final sample:");
		s.print();
				
		assertEquals(true, m.getProblem().satisfiesConstraints(s, m.getMN().getEdges()));
	}

}
