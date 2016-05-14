package applicationProblems;

import java.util.ArrayList;

import MN.Edge;
import MN.Sample;
import PSO.FitnessFunction;

public interface ApplicationProblem {
	
	public boolean satisfiesConstraints(Sample s, ArrayList<Edge> edgesArray); 
	public void createFitnessFunction(int optimal);
	public boolean isMaxProblem();
	public FitnessFunction getFitnessFunction();
	public double getWorstValue();
	
	/**
	 * Returns -1 if compared fitness is worse than current, 1 if compared fitness is better than current, 0 otherwise
	 * @param current
	 * @param compared
	 * @return
	 */
	public int compare(double current, double compared);
	public Sample generateNeighbor(Sample s);
	/**
	 * Returns the penalty to be added to a solution that violates the constraints of the problem
	 * @return
	 */
	public double getInvalidSolutionPenalty();
	/**
	 * Throws an exception if edge potentials violate any constraints
	 * @param e
	 */
	public void checkEdgeConstraints(Edge e); 
	/**
	 * Enforces any special constraints on a single edge
	 * @param e
	 */
	public void handleEdgeConstraints(Edge e);

}
