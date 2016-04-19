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
				
		IPSO ipso = new IPSO(inputFile, 10, 0.4, 0.6, 0.6);
		ipso.run();
		
		ICPSO icpso = new ICPSO(inputFile, false, 10, 3, 0.99, 0.4, 0.6, 0.6);
		icpso.run();

		ICPSO micpso = new ICPSO(inputFile, true, 10, 3, 0.99, 0.4, 0.6, 0.6);
		micpso.run();
	        
    }
}
