package edu.msu.MICPSO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import MN.Edge;
import MN.PairwiseMarkovNetwork;
import MN.Node;
import PSO.GCFitnessFunction;
import PSO.ICPSO;
import PSO.ICParticle;
import PSO.IPSO;
import comparisons.GreedyVC;
import experiments.ICExperiment;
import experiments.MNExperiment;
import experiments.IExperiment;
import tuning.ICTuningExperiment;
import tuning.ITuningExperiment;
import tuning.MTuningExperiment;

public class RunModels {

	public static void main(String[] args) throws FileNotFoundException {
		// The filePath and fileName where the Markov net file is located
		// String filePath = "src/main/resources/";
		// String fileName = "graphColor05Node_30Values.txt";

		// for passing in to MarkovNetwork or ICParticle
		// String inputFile = filePath + fileName;

		// IPSO ipso = new IPSO(inputFile, 10, 0.4, 0.6, 0.6);
		// ipso.run();

		// ICPSO icpso = new ICPSO(inputFile, false, 10, 3, 0.99, 0.4, 0.6,
		// 0.6);
		// icpso.run();
		//
		// ICPSO micpso = new ICPSO(inputFile, true, 10, 3, 0.99, 0.4, 0.6,
		// 0.6);
		// micpso.run();

		// tuneExperiments();
		 testExperiments();

//		try {
//			//createRandomCSMaxSat(4, 50, 600);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private static void tuneExperiments() throws FileNotFoundException {
		ArrayList<Integer> pNums = new ArrayList<Integer>();
		pNums.add(5);
		pNums.add(10);
		pNums.add(15);

		ArrayList<Integer> sNums = new ArrayList<Integer>();
		sNums.add(1);
		sNums.add(3);

		ArrayList<Double> epsilons = new ArrayList<Double>();
		epsilons.add(0.6);
		epsilons.add(0.75);
		epsilons.add(0.9);
		epsilons.add(0.95);

		ArrayList<Double> omegas = new ArrayList<Double>();
		omegas.add(0.7);
		omegas.add(0.2);
		omegas.add(0.5);
		omegas.add(0.8);
		omegas.add(1.0);

		ArrayList<Double> phi1s = new ArrayList<Double>();
		phi1s.add(1.4);
		phi1s.add(0.2);
		phi1s.add(0.6);
		phi1s.add(1.0);

		ArrayList<Double> phi2s = new ArrayList<Double>();
		phi2s.add(1.4);
		phi2s.add(0.2);
		phi2s.add(0.6);
		phi2s.add(1.0);

		// MTuningExperiment exp1 = new MTuningExperiment(pNums, sNums,
		// epsilons, omegas, phi1s, phi2s);
		// ICTuningExperiment exp2 = new ICTuningExperiment(pNums, sNums,
		// epsilons, omegas, phi1s, phi2s);
		ITuningExperiment exp3 = new ITuningExperiment(pNums, omegas, phi1s, phi2s);
	}

	private static void testExperiments() throws FileNotFoundException {

		ArrayList<String> experimentGraphs = new ArrayList<String>();
		String filePath = "src/main/resources/";
		
//		experimentGraphs.add(filePath + "huck_converted.txt");		
//		experimentGraphs.add(filePath + "myciel3_converted.txt");
//		experimentGraphs.add(filePath + "myciel4_converted.txt");
//		experimentGraphs.add(filePath + "myciel5_converted.txt");
//		experimentGraphs.add(filePath + "queen5_5color_converted.txt");		

		//MT to do: check these on other computer.
//		experimentGraphs.add(filePath + "queen6_6color_converted.txt");
//		experimentGraphs.add(filePath + "queen7_7color_converted.txt");
//		experimentGraphs.add(filePath + "queen8_8color_converted.txt");	
//		experimentGraphs.add(filePath + "queen8_12color_converted.txt");		
		
     	experimentGraphs.add("src/main/resources/graphColor05Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor05Node2_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor05Node3_30Values_NP.txt");
		experimentGraphs.add("src/main/resources/graphColor08Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor08Node2_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor10Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor10Node2_30Values_NP.txt");
		experimentGraphs.add("src/main/resources/graphColor12Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor16Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor20Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor24Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor38Node_30Values.txt");

		MNExperiment m = new MNExperiment(1, experimentGraphs);
//		ICExperiment ic = new ICExperiment(30, experimentGraphs);
//		IExperiment i = new IExperiment(30, experimentGraphs);
//		GreedyVC g = new GreedyVC(filePath + "queen8_8color_converted.txt");
//		g.run();
	}

	// TODO: this
	private static void createRandomCSMaxSat(int index, int numVars, int numPreds) throws IOException {
		// cs = controlled size
		// create 3 groups of variables
		ArrayList<Integer> layer1 = new ArrayList<Integer>();
		ArrayList<Integer> layer2 = new ArrayList<Integer>();
//		ArrayList<Integer> layer3 = new ArrayList<Integer>();

		ArrayList<Integer> allNums = new ArrayList<Integer>();
//		ArrayList<ArrayList<Integer>> layers = new ArrayList<ArrayList<Integer>>();
		
		// (recall: 1-based indexing, 0 is a special character)
		for (int i = 1; i < numVars + 1; i++) {
			allNums.add(i);
		}
		System.out.println(Arrays.toString(allNums.toArray()));
		
		while (!allNums.isEmpty()) {
			layer1.add(allNums.get(0));
			allNums.remove(0);

			if (!allNums.isEmpty()) {
				layer2.add(allNums.get(0));
				allNums.remove(0);
			}
//
//			if (!allNums.isEmpty()) {
//				layer3.add(allNums.get(0));
//				allNums.remove(0);
//			}
		}
		
		System.out.println(Arrays.toString(layer1.toArray()));
		System.out.println(Arrays.toString(layer2.toArray()));
		//System.out.println(Arrays.toString(layer3.toArray()));


		// create output file
		String s = "src/main/resources/CSMaxSatTestFile" + index + ".txt";

		File ms = new File(s);

		FileWriter writer = new FileWriter(ms);

		// write preamble
		writer.write("MS");
		writer.write("\n\n");
		writer.write("c Randomly-generated maxsat whose corresponding MN is tripartite \n");
		writer.write("p cnf " + numVars + " " + numPreds + "\n");

		// create predicates
		for (int i = 0; i < numPreds; i++) {
			// pick a random variable from each layer
			ArrayList<Integer> predicate = new ArrayList<Integer>();
			predicate.add(layer1.get(ThreadLocalRandom.current().nextInt(0, layer1.size())));
			predicate.add(layer2.get(ThreadLocalRandom.current().nextInt(0, layer2.size())));
//			predicate.add(layer3.get(ThreadLocalRandom.current().nextInt(1, layer3.size())));
//			
//			// pick 3 more
//			int fourth = layer1.get(ThreadLocalRandom.current().nextInt(0, layer1.size()));
//			while (predicate.contains(fourth)){
//				fourth = layer1.get(ThreadLocalRandom.current().nextInt(0, layer1.size()));
//			}
//			predicate.add(fourth);
//			int fifth = layer2.get(ThreadLocalRandom.current().nextInt(0, layer2.size()));
//			while (predicate.contains(fifth)){
//				fifth = layer2.get(ThreadLocalRandom.current().nextInt(0, layer2.size()));
//			}
//			predicate.add(fifth);
//			int sixth = layer3.get(ThreadLocalRandom.current().nextInt(0, layer3.size()));
//			while (predicate.contains(sixth)){
//				sixth = layer3.get(ThreadLocalRandom.current().nextInt(0, layer3.size()));
//			}
//			predicate.add(sixth);

			for (Integer p : predicate) {
				boolean positive = Math.random() > 0.5;
				if (!positive) {
					writer.write("-");
				}
				writer.write(p + " ");
			}

			writer.write("0" + "\n");
		}

		writer.close();

	}
}
