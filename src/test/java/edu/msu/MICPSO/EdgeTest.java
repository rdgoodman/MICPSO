package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Test;

import MN.Edge;
import MN.Node;

public class EdgeTest {

//	@Test
//	public void testInitialization() {
//		double[] aVals = {0,1};
//		double[] bVals = {2,3};
//		double[] cVals = {4,5};
//		double[] dVals = {6,7};		
//
//		Node A = new Node(aVals, "A");
//		Node B = new Node(bVals, "B");
//		Node C = new Node(cVals, "C");
//		Node D = new Node(dVals, "D");		
//		
//		Edge AB = new Edge(A, B);
//		Edge BC = new Edge(B, C);
//		Edge CD = new Edge(C, D);
//		Edge DA = new Edge(D, A);
//		
//		AB.printFactors();		
//		BC.printFactors();
//		CD.printFactors();
//		DA.printFactors();
//	}
	
	@Test
	public void testHashTable(){
		Hashtable<Node, Double> ht = new Hashtable<Node,Double>();
		double[] three = {1,2,3};
		Node A = new Node(three, "A");
		Node B = new Node(three, "B");
		ht.put(A, 4.0);
		ht.put(B, 4.0);
		System.out.println("A's value: " + ht.get(A));
		System.out.println("B's value: " + ht.get(B));
		ht.put(A, 2.0);
		System.out.println();
		System.out.println("A's value: " + ht.get(A));
		System.out.println("B's value: " + ht.get(B));
		ht.put(A, 4.0);
		System.out.println();
		System.out.println("A's value: " + ht.get(A));
		System.out.println("B's value: " + ht.get(B));

	}

}
