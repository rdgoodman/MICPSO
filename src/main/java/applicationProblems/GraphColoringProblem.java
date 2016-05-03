package applicationProblems;

import java.util.ArrayList;

import MN.Edge;
import MN.Sample;
import PSO.FitnessFunction;
import PSO.GCFitnessFunction;

public class GraphColoringProblem implements ApplicationProblem {

	GraphColoringConstraint constraint;
	GCFitnessFunction f;

	public GraphColoringProblem(int optimal) {
		constraint = new GraphColoringConstraint();
		createFitnessFunction(optimal);
	}

	@Override
	public boolean satisfiesConstraints(Sample s, ArrayList<Edge> edgesArray) {
		return constraint.satisfiesConstraint(s, edgesArray);
	}

	@Override
	public void createFitnessFunction(int optimal) {
		f = new GCFitnessFunction(optimal);
	}

	@Override
	public boolean isMaxProblem() {
		return true;
	}

	@Override
	public FitnessFunction getFitnessFunction() {
		return f;
	}

	@Override
	public double getWorstValue() {
		// recall, this is a maximization problem
		return -Double.MAX_VALUE;
	}

	@Override
	public int compare(double current, double compared) {
		if (isMaxProblem()) {
			if (current < compared) {
				// for a max problem, compared is better if it's greater than current
				return 1;
			} else if (current > compared) {
				return -1;
			}
		} else {
			if (current < compared) {
				return -1;
			} else if (current > compared) {
				// for a min problem, compared is better if it's less than current
				return 1;
			}
		}
		return 0;
	}

}
