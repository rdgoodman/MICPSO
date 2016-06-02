package edu.msu.MICPSO;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import MN.Clique;
import MN.CliqueMarkovNetwork;
import MN.Node;
import MN.Sample;

public class CliqueMNTest {

//	@Test
//	public void testBronKerbosch() throws FileNotFoundException {
//		String filename = "src/main/resources/maxSatTestFile2.txt";
//		//String filename = "src/main/resources/graphColor08Node2_10Values.txt";
//		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
//		
//		ArrayList<Node> R = new ArrayList<Node>();
//		ArrayList<Node> X = new ArrayList<Node>();
//		ArrayList<Node> P = net.getNodes();
//		
//		net.bronKerbosch(R, P, X);		
//	}
//	
//	@Test
//	public void testFactorInitialization() throws FileNotFoundException{
//		String filename = "src/main/resources/maxSatTestFile2.txt";
//		//String filename = "src/main/resources/graphColor08Node2_10Values.txt";
//		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
//		
//		ArrayList<Clique> c = net.getMaxCliques();
//		System.out.println("\n -----------------------");
//		System.out.println("# cliques: " + c.size());
//		for (Clique l: c){
//			l.print();
//		}
//	}
	
	@Test
	public void testSample() throws FileNotFoundException{
		String filename = "src/main/resources/maxSatTestFile2.txt";
		//String filename = "src/main/resources/graphColor08Node2_10Values.txt";
		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
		
		Sample s = net.createRandomValidSample();
		System.out.println("Random valid sample: ");
		s.print();
	}

}