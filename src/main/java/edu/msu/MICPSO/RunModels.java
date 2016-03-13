package edu.msu.MICPSO;

import java.io.*;
import java.util.Scanner;

import MN.Edge;
import MN.Node;

public class RunModels {

	private static double[] vals;

	public static void main(String[] args) throws FileNotFoundException {
		//TODO: Clean this up, make more universal
		
		// The scanner for reading in the Markov net file
		Scanner s = null;
		
		// The filePath and fileName where the Markov net file is located
		String filePath = "C:/Users/Monica/Desktop/";
		String fileName = "markovNet.txt";
		
		// array for storing node names
        String[] nodes = null;
        String[] edges = null;
        String[] stringValues = null;
        Node[] nodeArray = new Node[4];
		      
		// The entire file name, for retrieving the Markov net file
		File file = new File(filePath + fileName);
		
        try {
            s = new Scanner(new BufferedReader(new FileReader(file)));
            
            String potential;
            String discard;
            
            // a potential piece of information in Markov net file
            potential = s.nextLine();
            	
            // gets the node info first
            // checks for comments, when present, discards them
            while (potential.startsWith("%")) {
            	discard = potential;
            	potential = s.nextLine();
            } 
            	
            // splits the string into an array of separate node objects
            if (!potential.startsWith("%")){
    			nodes = potential.split(",");
            }
            	
            // trims extra whitespace from node objects
            for (int i = 0; i < nodes.length; i++) {
            	if (nodes[i].startsWith(" ")) {
            		nodes[i] = nodes[i].trim();
            	}
            }
            
            potential = s.nextLine();
            potential = s.nextLine();
                                    
            // gets the edge info first
            // checks for comments, when present, discards them
            while (potential.startsWith("%")) {
            	discard = potential;
            	potential = s.nextLine();
            } 
            
            
            if(!potential.startsWith("%")) {
            	edges = potential.split(";");            
            }
            
            // trims extra whitespace from edge objects
            for (int i = 0; i < edges.length; i++) {
            	if (edges[i].startsWith(" ")) {
            		edges[i] = edges[i].trim();
            	}
            }            
            
            // get variable info
            // checks for comments, when present, discards them
            while (potential.startsWith("%")) {
            	discard = potential;
            	potential = s.nextLine();
            } 

        	potential = s.nextLine();
        	potential = s.nextLine();
        	potential = s.nextLine();
        	        	
            if(!potential.startsWith("%")) {
            	stringValues = potential.split(";");            
            }
            
            // trims extra whitespace from edge objects
            for (int i = 0; i < stringValues.length; i++) {
            	if (stringValues[i].startsWith(" ")) {
            		stringValues[i] = stringValues[i].trim();
            	}         
            	stringValues[i] = stringValues[i].substring(3, 7);
            }
                       
        } finally {
            if (s != null) {
                s.close();
            }
        }

        
//    	System.out.println("Nodes:");
//        for (int i = 0; i < nodes.length; i++) {
//        	System.out.println(nodes[i]);
//        }
//        System.out.println();
//        
//    	System.out.println("Edges:");
//        for (int i = 0; i < edges.length; i++) {
//        	System.out.println(edges[i]);
//        }
//        System.out.println();        
//
//    	System.out.println("Values:");
//        for (int i = 0; i < stringValues.length; i++) {
//        	System.out.println(stringValues[i]);
//        }
//        System.out.println(); 
               
        // initialize nodes and values
        // TODO: This is a mess, fix it
        for(int i = 0; i < nodes.length; i++) {
        	String nodeName = nodes[i];
     	    double[] values = new double[2];
        	int n = 0;
        	double thisVal = Double.parseDouble(stringValues[i].substring(0, 1));
        	values[n] = thisVal;
        	n++;        	
        	thisVal = Double.parseDouble(stringValues[i].substring(3, 4));
        	values[n] = thisVal;
        	Node thisNode = new Node (values, nodeName);          	
        	nodeArray[i] = thisNode;
        }
        	
        // initialize edges
        Node aNode = null;
        Node bNode = null;
    
        // for each edge
        for(int i = 0; i < edges.length; i++) {
        	// for each node in the array
        	for (int n = 0; n < nodeArray.length; n++) {
        		// get the first node in the edge
        		if (edges[i].startsWith(nodeArray[n].getName())) {
        			aNode = nodeArray[n];
        		}
        	}

        	for (int n = 0; n < nodeArray.length; n++) {
        		// get the first node in the edge
        		if (edges[i].endsWith(nodeArray[n].getName())) {
        			bNode = nodeArray[n];
        		}
        		
        	}
        	
        	Edge E = new Edge (aNode, bNode);
        	E.printFactors();
        }  	

    }
}
