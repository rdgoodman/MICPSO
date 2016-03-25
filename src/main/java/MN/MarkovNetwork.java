package MN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MarkovNetwork {
	// Array list for storing nodes and edges
	private ArrayList<Node> nodesArray = new ArrayList<Node>();
	private ArrayList<Edge> edgesArray = new ArrayList<Edge>();
	
   	// Arrays for storing the string values read in from file
    String[] stringNodes = null;
    String[] stringEdges = null;
    String[] stringValues = null;
	
    // the largest number of nodes associated with any node
    int numValues = 0;

	public MarkovNetwork() throws FileNotFoundException {		
		// The scanner for reading in the Markov net file
		Scanner s = null;
		
		// The filePath and fileName where the Markov net file is located
		String filePath = "src/main/resources/";
		String fileName = "markovNet.txt";
				              
		// The entire file name, for retrieving the Markov net file
		File file = new File(filePath + fileName);
		
		// Reads in the nodes, edges and values in from a specifically formatted file
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
            // creates the Markov network
            createNetworkStructure();
        } finally {
            if (s != null) {
                s.close();
            }
        }
	}
	
	/**
	 * Use this to create the network structure (known) for the network
	 */
	public void createNetworkStructure() {
		
		// step 1: create nodes
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

		// step 2: create edges
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
    		// each time an edge A-B is created, do: A.addNeighbor(B) and B.addNeighbor(A)
        	startingNode.addNeighbor(endingNode);
        	endingNode.addNeighbor(startingNode);        	
        }
        // for testing, can remove when finished
        print();
	}

	/**
	 * Returns the result of Gibbs sampling this distribution
	 * 
	 * @return
	 */
	public Sample sample() {
		
		Sample sample = new Sample(this);

		// 1) generate an initial sample (probably randomly from vals(Vars)
		for (Node n : nodesArray) {
			// chosen uniformly - does that work?
			double[] vals = n.getVals();
			double r = Math.random();
			double interval = (double) 1 / vals.length;
			
			int counter = 0;
			for (double i = interval; i <= 1; i += interval) {
				if (r < i) {
					sample.setSampledValue(n, vals[counter]);
					break;
				}
				counter++;
			}
		}
		
		System.out.println(" Initial sample: ");
		sample.print();
		
		// 2) For each non-evidence variable (so, all of them) [order
		// doesn't
		// matter]...
		for (Node N : nodesArray) {
			
			System.out.println(">>>>> Resampling Node " + N.getName());

			// 4) Calculate P(X|MB(X)) using current values for MB(X)
			ArrayList<Node> MB = N.getMB();			
			ArrayList<Edge> E = new ArrayList<Edge>();

			// Get all edges adjacent to N (there must be a better way to do
			// this)
			// TODO: refactor (if time)
			// (gives a more limited list to search later on)
			for (Edge e : edgesArray) {
				if (e.getEndpoints().contains(N)) {
					E.add(e);
				}
			}

			// stores the distribution generated by calculations
			ProbDist probs = new ProbDist(N.getVals(), N);

			// for each value of N
			double[] nVals = N.getVals();
			for (int n = 0; n < nVals.length; n++) {
				//System.out.println();
				//System.out.print("P~ (" + N.getName() + " == " + nVals[n] + ") \n");

				for (Node M : MB) {					
					Edge edge = null;
					// pull the edge between N and M
					for (Edge e : E) {
						if (e.getEndpoints().contains(M)) {
							edge = e;
						}
					}

					// get sample value of M
					double mVal = sample.getValue(M);

					// get potential where M=mVal and N=nVals[n]
					double p = edge.getPotential(N, nVals[n], M, mVal);
					//System.out.print(p + "[" +  M.getName() + " == " + mVal + "] *");

					// multiply current P(N==n) by p
					probs.setProb(nVals[n], probs.getProb(nVals[n]) * p);
				}
				
				//probs.print();

				// re-normalize
				probs.normalize();
				
				//probs.print();

				// re-sample resulting distribution for N
				sample.setSampledValue(N, probs.sample());
			}

		}
		
		// TODO: testing, remove
		System.out.println("\n Final sample: ");
		sample.print();
		
		return sample;
	}

	public ArrayList<Node> getNodes() {
		return nodesArray;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodesArray = nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edgesArray;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edgesArray = edges;
	}
	
	public void print(){
		System.out.println("NODES");
		for (int i = 0; i < nodesArray.size(); i++){
			System.out.println("> " + nodesArray.get(i).getName());
		}
		System.out.println("EDGES");
		for (int i = 0; i < edgesArray.size(); i++){
			edgesArray.get(i).printFactors();
		}
	}
}
