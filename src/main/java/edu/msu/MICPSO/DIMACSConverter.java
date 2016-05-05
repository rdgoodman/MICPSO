package edu.msu.MICPSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// converts from DIMACS file to the format we are using for our parser
// output file lists nodes and edges
public class DIMACSConverter {
	// Variables for storing the string values read in from file
	private ArrayList<String> stringNodes = new ArrayList<String>();
	private ArrayList<String> stringEdges = new ArrayList<String>();

	/**
	 * Constructor when read in from file
	 * 
	 * @param inputFile
	 * @throws FileNotFoundException
	 */
	public DIMACSConverter(String inputFile) throws FileNotFoundException {
		// The scanner for reading in the DIMACS file
		Scanner s = null;

		// The entire file name (passed in from RunModels),
		// for retrieving the DIMACS file
		File file = new File(inputFile);

		// gets the file name, provided it meets our naming specs
		String filePart = inputFile.substring(38, inputFile.length());
		filePart = filePart.substring(0, filePart.lastIndexOf('.'));

		// We will write the output files to 2 files, node and edges
		String outN = "src/main/resources/DIMACSFiles/" + filePart + "_Nodes";
		String outE = "src/main/resources/DIMACSFiles/" + filePart + "_Edges";

		// create our files and writers
		File outNodes = new File(outN);
		if (!outNodes.exists()) {
			try {
				outNodes.createNewFile();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileWriter outNodesWriter = null;

		// create our files and writers
		File outEdges = new File(outE);
		if (!outEdges.exists()) {
			try {
				outEdges.createNewFile();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileWriter outEdgesWriter = null;

		// Reads in the nodes, edges and values in from a specifically formatted
		// file
		try {
			s = new Scanner(new BufferedReader(new FileReader(file)));
			/*
			 * Reads the file, ignoring lines with c and p. Gets the nodes from
			 * the list of edges, and also stores the edges.
			 * 
			 */
			while (s.hasNextLine()) {
				String tempVal = s.nextLine();

				// skips lines with c or p prefix
				while (tempVal.startsWith("c")) {
					// System.out.println("skip " + tempVal);
					tempVal = s.nextLine();
				}

				if (tempVal.startsWith("p")) {
					// System.out.println("skip " + tempVal);
					// always one value starting with p, move on to edges
					tempVal = s.nextLine();
				}

				// gets the number associated with the optimal solution
				// checks for comments, when present, discards them
				while (tempVal.startsWith("e")) {
					// removes the p, removes the leading space
					tempVal = tempVal.substring(1, tempVal.length()).trim();
					// System.out.println(tempVal);
					// add to edges - format is A,B;
					// add commas
					tempVal = tempVal.replace(" ", ",");
					// System.out.println(tempVal);
					// add trailing semi-colon and space
					stringEdges.add(tempVal + "; ");

					// add each to node list (if not already there)
					String[] nodeCheck = tempVal.split(",");
					
					String check = nodeCheck[0];
					if (!stringNodes.contains(check + ", ")) {
						// 0 is not in, add it
						stringNodes.add(check + ", ");
					}

					check = nodeCheck[1];
					if (!stringNodes.contains(nodeCheck[1]+ ", ")) {
						// 1 is not in, add it
						stringNodes.add(nodeCheck[1] + ", ");
					}

					// makes sure we have not moved beyond end of file
					if (s.hasNextLine()) {
						tempVal = s.nextLine();
					} else {
						break;
					}
				}

				//System.out.println("Printing edges");
				try {
					outEdgesWriter = new FileWriter(outEdges, true);
					for (int i = 0; i < stringEdges.size(); i++) {
						outEdgesWriter.write(stringEdges.get(i));
					}
					outEdgesWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//System.out.println("Printing nodes");
				try {
					outNodesWriter = new FileWriter(outNodes, true);
					for (int i = 0; i < stringNodes.size(); i++) {
						outNodesWriter.write(stringNodes.get(i));
					}
					outNodesWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String forPrint = "";
				String nodeNoComma = "";
				for (int i = 0; i < stringNodes.size(); i++) {
					nodeNoComma = stringNodes.get(i).substring(0, stringNodes.get(i).lastIndexOf(','));
					forPrint = forPrint + nodeNoComma + ": 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29; ";
				}
				System.out.println(forPrint);
			}
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}
}
