package edu.msu.MICPSO;

import static org.junit.Assert.*;

import org.junit.Test;

import MN.Edge;
import MN.Node;

public class EdgeTest {

	@Test
	public void testInitialization() {
		double[] aVals = {0,1};
		double[] bVals = {2,3};
		double[] cVals = {4,5};
		double[] dVals = {6,7};		

		Node A = new Node(aVals, "A");
		Node B = new Node(bVals, "B");
		Node C = new Node(cVals, "C");
		Node D = new Node(dVals, "D");		
		
		Edge AB = new Edge(A, B);
		Edge BC = new Edge(B, C);
		Edge CD = new Edge(C, D);
		Edge DA = new Edge(D, A);
		
		AB.printFactors();		
		BC.printFactors();
		CD.printFactors();
		DA.printFactors();
	}

}
