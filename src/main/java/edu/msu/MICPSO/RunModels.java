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

public class RunModels {

	public static void main(String[] args) throws FileNotFoundException {	
		// The filePath and fileName where the Markov net file is located
		String filePath = "src/main/resources/";
		String fileName = "graphColor38Node.txt";
		
		// for passing in to MarkovNetwork or ICParticle
		String inputFile = filePath + fileName;
				
//		// creates a Markov network (read in from file)
//        MarkovNetwork MN = new MarkovNetwork(inputFile);
//              
//        // samples the above network using Gibbs sampling
//        MN.sample();

//		ICPSO p = new ICPSO(inputFile, true, 10, 3, 0.99, 0.4, 0.6, 0.6);
//		p.run();

		IPSO p2 = new IPSO(inputFile, 10, 0.4, 0.6, 0.6);
		p2.run();
		
		
        
    }
}
