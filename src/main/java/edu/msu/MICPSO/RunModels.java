package edu.msu.MICPSO;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.MarkovNetwork;
import MN.Node;

public class RunModels {

	public static void main(String[] args) throws FileNotFoundException {	
		// The scanner for reading in the Markov net file
		Scanner s = null;
		
		// The filePath and fileName where the Markov net file is located
		String filePath = "src/main/resources/";
		String fileName = "markovNet.txt";
		
		// Array list for storing nodes and edges
        ArrayList<Node> nodesArray = new ArrayList<Node>();
        ArrayList<Edge> edgesArray = new ArrayList<Edge>();
        
    	// Arrays for storing the string values read in from file
        String[] stringNodes = null;
        String[] stringEdges = null;
        String[] stringValues = null;
		      
        // the largest number of nodes associated with any node
        int numValues = 0;
        
		// The entire file name, for retrieving the Markov net file
		File file = new File(filePath + fileName);
		
        try {
        	s = new Scanner(new BufferedReader(new FileReader(file)));
                        
            String potential;
            
            /*
             * Reads the file, ignoring lines with % (which are comment lines).
             * File is structured so that the nodes are first (comma separated), 
             * followed by the edges (in form A, B semi-colon separated) and then the 
             * values for the variables (in form A: 0, 1). The values need to 
             * be in the same order as the node variables. At this stage, the variables 
             * are read in as strings, and after the file is closed they are converted 
             * to the appropriate object type (i.e., Node or Edge objects).
             * 
             */
            while (s.hasNext()) {
            	 // Read the first line in the file
                potential = s.nextLine();
                	
                // gets the node info first
                // checks for comments, when present, discards them
                while (potential.startsWith("%")) {
                	potential = s.nextLine();
                } 
                               
                // splits the string into an array of separate node objects
                if (!potential.startsWith("%")){
        			stringNodes = potential.split(",");
                }
                	
                // trims extra whitespace from node objects
                for (int i = 0; i < stringNodes.length; i++) {
                	if (stringNodes[i].startsWith(" ")) {
                		stringNodes[i] = stringNodes[i].trim();
                	}
                }
                
                //keep scanning for the next non-empty line
                if (s.nextLine().equals("")) {
                	potential = s.nextLine();
                }
                      
                // gets the edge info first
                // checks for comments, when present, discards them
                while (potential.startsWith("%")) {
                	potential = s.nextLine();
                } 
                
               // if the line is not a comment, per the file structure is the edges 
                if(!potential.startsWith("%")) {
                	stringEdges = potential.split(";");            
                }
                
                // trims extra whitespace from edge objects
                for (int i = 0; i < stringEdges.length; i++) {
                	if (stringEdges[i].startsWith(" ")) {
                		stringEdges[i] = stringEdges[i].trim();
                	}
                }            

                //keep scanning for the next non-empty line
                if (s.nextLine().equals("")) {
                	potential = s.nextLine();
                }
                
                // get variable info
                // checks for comments, when present, discards them
                while (potential.startsWith("%")) {
                	potential = s.nextLine();
                } 
                                           	
                // if the line is not a comment, per file structure is values
                if(!potential.startsWith("%")) {
                	stringValues = potential.split(";");            
                }
                
                // trims the extra information and gets the (in node order) values
                for (int i = 0; i < stringValues.length; i++) {
                	// gets the information in format NODE: X, X 
                	int startOfValues;
                	// assists in getting the largest number of values found for any node
                	int tempNumValues = 0;
                	startOfValues = stringValues[i].lastIndexOf(":");
                	stringValues[i] = stringValues[i].substring(startOfValues + 1, stringValues[i].length());
                    // trims extra whitespace from edge objects                	
                	if (stringValues[i].startsWith(" ")) {
                		stringValues[i] = stringValues[i].trim();
                	}               
                	
                	tempNumValues = stringValues[i].split(",").length;
                	// finds the largest number of values for any nodes (for use in  
                	// initializing the Node objects)
                	if (tempNumValues > numValues) {
                		numValues = tempNumValues;
                	}
                	
                }	
            }
            
        } finally {
            if (s != null) {
                s.close();
            }
        }
//		  for testing to make sure file is being read correctly	        
//        System.out.println("Nodes");
//        for (int i = 0; i < stringNodes.length; i++) {
//        	System.out.println(stringNodes[i]);
//        }
//        
//        System.out.println("Edges");        
//        for (int i = 0; i < stringEdges.length; i++) {
//        	System.out.println(stringEdges[i]);
//        }        
//        
//        
//        System.out.println("Values");
//        for (int i = 0; i < stringValues.length; i++) {
//        	System.out.println(stringValues[i]);
//        }
        
        // initialize node and value objects from the string arrays
        for(int i = 0; i < stringNodes.length; i++) {
        	String nodeName = stringNodes[i];
     	    double[] values = new double[numValues]; 
     	    
    		int startIndex = 0;
    		int stopIndex = 1;
    		
        	// gets values from the value array
        	for (int n = 0; n < numValues; n++) {
        		// reminder: values are comma separated            	
        		double thisVal = Double.parseDouble(stringValues[i].substring(startIndex, stopIndex));
            	values[n] = thisVal;            	
            	startIndex = startIndex + 2;
            	stopIndex = startIndex + 1;
        	}

        	// creates a node with the appropriate values and node name 
        	Node thisNode = new Node (values, nodeName);          	
        	nodesArray.add(thisNode);
        }

        // makes temporary node objects to store the start/end of an edge 
        Node startingNode = null;
        Node endingNode = null;
    
        // goes through each edge in the array of edges (from the file read in earlier)
        for(int e = 0; e < stringEdges.length; e++) {
        	// for each node in the array of Nodes gets the starting and ednging nodes
        	for (int n = 0; n < nodesArray.size(); n++) {        	
        		// gets the first node from the string array of edges
        		if (stringEdges[e].startsWith(nodesArray.get(n).getName())) {
        			startingNode = nodesArray.get(n);
        		}
        	}

        	for (int n = 0; n < nodesArray.size(); n++) {
        		// get the last node from the string array of edges
        		if (stringEdges[e].endsWith(nodesArray.get(n).getName())) {
        			endingNode = nodesArray.get(n);
        		}	
        	}
        	
        	Edge E = new Edge (startingNode, endingNode);
        	edgesArray.add(E);
        	startingNode.addNeighbor(endingNode);
        	endingNode.addNeighbor(startingNode);        	
        }  
        
        MarkovNetwork MN = new MarkovNetwork(nodesArray, edgesArray);
        MN.sample();

// 		  for testing - remove when finished        
//        System.out.println("Nodes");
//        for (int i = 0; i < nodesArray.size(); i++) {
//			System.out.println("> " + nodesArray.get(i).getName());;
//        }
//
//        System.out.println("Edges");
//        for (int i = 0; i < edgesArray.size(); i++) {;
//        	edgesArray.get(i).printFactors();
//        }
        
    }
}
