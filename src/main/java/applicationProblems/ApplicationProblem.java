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

}
