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
	 * Returns -1 if current fitness is better than compared, 1 if current fitness is worse than compared, 0 otherwise
	 * @param current
	 * @param compared
	 * @return
	 */
	public int compare(double current, double compared);

}