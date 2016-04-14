package edu.msu.MICPSO;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.MarkovNetwork;
import MN.Node;
import PSO.GCFitnessFunction;
import PSO.ICParticle;

public class RunModels {

	public static void main(String[] args) throws FileNotFoundException {	
		// The filePath and fileName where the Markov net file is located
		String filePath = "src/main/resources/";
		String fileName = "markovNet.txt";
		
		// for passing in to MarkovNetwork or ICParticle
		String inputFile = filePath + fileName;
				
		// creates a Markov network (read in from file)
        MarkovNetwork MN = new MarkovNetwork(inputFile);
        
        // samples the above network using Gibbs sampling
        MN.sample();
        

//		ICParticle p = new ICParticle(inputFile, new GCFitnessFunction(3), 5, .005);
//		p.print();
//		p.updatePosition();
//		System.out.println("Update 1:");
//		p.print();
//		System.out.println("Update 2:");
//		p.updatePosition();
//		p.print();
    }
}
