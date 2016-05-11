package edu.msu.MICPSO;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.MarkovNetwork;
import MN.Node;
import PSO.GCFitnessFunction;
import PSO.ICPSO;
import PSO.ICParticle;
import PSO.IPSO;
import experiments.ICExperiment;
import experiments.MNExperiment;
import experiments.IExperiment;
import tuning.ICTuningExperiment;
import tuning.ITuningExperiment;
import tuning.MTuningExperiment;

public class RunModels {

	public static void main(String[] args) throws FileNotFoundException {	
		// The filePath and fileName where the Markov net file is located
//		String filePath = "src/main/resources/";
//		String fileName = "graphColor05Node_30Values.txt";
		
		// for passing in to MarkovNetwork or ICParticle
//		String inputFile = filePath + fileName;
				
//		IPSO ipso = new IPSO(inputFile, 10, 0.4, 0.6, 0.6);
//		ipso.run();
		
//		ICPSO icpso = new ICPSO(inputFile, false, 10, 3, 0.99, 0.4, 0.6, 0.6);
//		icpso.run();
//
//		ICPSO micpso = new ICPSO(inputFile, true, 10, 3, 0.99, 0.4, 0.6, 0.6);
//		micpso.run();
		
//		tuneExperiments();
		testExperiments();
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
		
		//MTuningExperiment exp1 = new MTuningExperiment(pNums, sNums, epsilons, omegas, phi1s, phi2s);
		//ICTuningExperiment exp2 = new ICTuningExperiment(pNums, sNums, epsilons, omegas, phi1s, phi2s);		
		//ITuningExperiment exp3 = new ITuningExperiment(pNums, omegas, phi1s, phi2s);		
	}
	
	private static void testExperiments() throws FileNotFoundException{
		
		ArrayList<String> experimentGraphs = new ArrayList<String>();

		//experimentGraphs.add("src/main/resources/graphColor05Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor05Node2_30Values.txt");
		//experimentGraphs.add("src/main/resources/graphColor05Node3_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor08Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor08Node2_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor10Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor10Node2_30Values_NP.txt");
		experimentGraphs.add("src/main/resources/graphColor12Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor16Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor20Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor24Node_30Values.txt");
		experimentGraphs.add("src/main/resources/graphColor38Node_30Values.txt");
			
		MNExperiment m = new MNExperiment(20, experimentGraphs);
		ICExperiment ic = new ICExperiment(10, experimentGraphs);
		IExperiment i = new IExperiment(10, experimentGraphs);

	}
}
