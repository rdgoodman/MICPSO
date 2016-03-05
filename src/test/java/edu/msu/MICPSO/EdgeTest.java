package edu.msu.MICPSO;

import static org.junit.Assert.*;

import org.junit.Test;

import MN.Edge;
import MN.Node;

public class EdgeTest {

	@Test
	public void testInitialization() {
		double[] aVals = {1, 2, 3};
		double[] bVals = {4, 5, 6};

		Node A = new Node(aVals);
		Node B = new Node(bVals);
		
		Edge AB = new Edge(A, B);
		AB.printFactors();

		
	}

}
