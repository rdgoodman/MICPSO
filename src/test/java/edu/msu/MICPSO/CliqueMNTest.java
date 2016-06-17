package edu.msu.MICPSO;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import MN.Clique;
import MN.CliqueMarkovNetwork;
import MN.Node;
import MN.Sample;
import PSO.MSFitnessFunction;
import applicationProblems.MaxSatProblem;

public class CliqueMNTest {

	// @Test
	// public void testBronKerbosch() throws FileNotFoundException {
	// String filename = "src/main/resources/maxSatTestFile2.txt";
	// //String filename = "src/main/resources/graphColor08Node2_10Values.txt";
	// CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
	//
	// ArrayList<Node> R = new ArrayList<Node>();
	// ArrayList<Node> X = new ArrayList<Node>();
	// ArrayList<Node> P = net.getNodes();
	//
	// net.bronKerbosch(R, P, X);
	// }
	//
	// @Test
	// public void testFactorInitialization() throws FileNotFoundException{
	// String filename = "src/main/resources/maxSatTestFile2.txt";
	// //String filename = "src/main/resources/graphColor08Node2_10Values.txt";
	// CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
	//
	// ArrayList<Clique> c = net.getMaxCliques();
	// System.out.println("\n -----------------------");
	// System.out.println("# cliques: " + c.size());
	// for (Clique l: c){
	// l.print();
	// }
	// }

//	@Test
//	public void testSample() throws FileNotFoundException {
//		String filename = "src/main/resources/maxSatTestFile2.txt";
//		// String filename =
//		// "src/main/resources/graphColor08Node2_10Values.txt";
//		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
//
//		Sample s1 = net.createRandomValidSample();
//		System.out.println("Random valid sample: ");
//		s1.print();
//
//		Sample s2 = net.createRandomValidSample();
//		System.out.println("Random sample: ");
//		s2.print();
//
//		ArrayList<Clique> c = net.getMaxCliques();
//		for (Clique l : c) {
//			l.print();
//		}
//
//		Sample s3 = net.sample();
//	}
	
//	@Test
//	public void testAdjustment() throws FileNotFoundException {
//		String filename = "src/main/resources/maxSatTestFile2.txt";
//		// String filename =
//		// "src/main/resources/graphColor08Node2_10Values.txt";
//		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
//
//		Sample s1 = net.createRandomValidSample();
//		System.out.println("Random valid sample: ");
//		s1.print();
//
//		net.adjustPotentials(s1, 0.9);
//	}
//	
//	@Test
//	public void testVelocityAdjustment() throws FileNotFoundException {
//		String filename = "src/main/resources/maxSatTestFile2.txt";
//		// String filename =
//		// "src/main/resources/graphColor08Node2_10Values.txt";
//		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
//		net.print();
//		System.out.println("\n\n UPDATING POTENTIALS \n\n");
//		net.updatePotentials();
//		net.print();
//	}
	
	@Test
	public void testInitValues() throws FileNotFoundException{
		String filename = "src/main/resources/BigMaxSatTestFile2.txt";
		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
		MSFitnessFunction f = new MSFitnessFunction((MaxSatProblem)net.getProblem());
		
		double avgFit = 0;
		double max = -1;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < 30; i++){
			Sample s = net.createRandomValidSample();
			avgFit += f.calcFitness(s);
			if (s.getFitness() > max){
				max = s.getFitness();
			} 
			if (s.getFitness() < min){
				min = s.getFitness();
			}
		}
		
		System.out.println(filename);
		System.out.println("Average: " + avgFit/30);
		System.out.println("Max: " + max);
		System.out.println("Min: " + min);
	}
	
	@Test
	public void testInitValues2() throws FileNotFoundException{
		String filename = "src/main/resources/BigMaxSatTestFile.txt";
		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
		MSFitnessFunction f = new MSFitnessFunction((MaxSatProblem)net.getProblem());
		
		double avgFit = 0;
		double max = -1;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < 30; i++){
			Sample s = net.createRandomValidSample();
			avgFit += f.calcFitness(s);
			if (s.getFitness() > max){
				max = s.getFitness();
			} 
			if (s.getFitness() < min){
				min = s.getFitness();
			}
		}
		
		System.out.println(filename);
		System.out.println("Average: " + avgFit/30);
		System.out.println("Max: " + max);
		System.out.println("Min: " + min);
	}

}
