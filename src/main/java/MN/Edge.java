package MN;

import java.text.DecimalFormat;

public class Edge {

	private Node start;
	private Node end;
	private double[] startVals;
	private double[] endVals;
	private FactorEntry[] factors; // potentials
	
	/**
	 * Creates an edge between two nodes (order doesn't actually matter)
	 * @param start
	 * @param end
	 */
	public Edge(Node start, Node end){
		this.start = start;
		this.end = end;
		this.startVals = start.getVals();
		this.endVals = end.getVals();
		initializeFactors(startVals.length * endVals.length);
	}

	/**
	 * Initializes the factor potential table. Potentials are randomly initialized!
	 * @param size the number of potentials in the table
	 */
	private void initializeFactors(int size) {
		factors = new FactorEntry[size];
		
		int counter = 0;
		
		// creates a new potential for each combination of start and end node vals
		for (int s = 0; s < startVals.length; s++){
			for (int e = 0; e < endVals.length; e++){
				factors[counter] = new FactorEntry(start, end, startVals[s], endVals[e]);
				counter++;
			}
		}
	}

	/**
	 * Prints the factor potentials
	 */
	public void printFactors(){
		System.out.println("Start \t End \t Factor");
		DecimalFormat fourd = new DecimalFormat("#.####");
		DecimalFormat oned = new DecimalFormat("#.####");

		
		int counter = 0;
		
		// creates a new potential for each combination of start and end node vals
		for (int s = 0; s < startVals.length; s++){
			for (int e = 0; e < endVals.length; e++){
				System.out.println(oned.format(startVals[s]) + "\t " + oned.format(endVals[e]) + "\t " + fourd.format(factors[counter].getPotential()));
				counter++;
			}
		}
		
	}

}
