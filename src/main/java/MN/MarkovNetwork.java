package MN;

import java.util.ArrayList;

public interface MarkovNetwork {
	
	public Sample sample();
	public MarkovNetwork copy();
	public ArrayList<Edge> getEdges();
	public double[][] getAllPotentials();
	public double[][] getAllVelocities();
	public void adjustAllVelocities(double[][] v);
	public void updatePotentials();
	public void adjustPotentials(Sample pBest_sample, double epsilon);
	public void print();
	

}
