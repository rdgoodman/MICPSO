package applicationProblems;

import java.util.ArrayList;
import java.util.Collections;

import MN.Edge;
import MN.PairwiseFactorEntry;
import MN.Node;
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
				// for a max problem, compared is better if it's greater than
				// current
				return 1;
			} else if (current > compared) {
				return -1;
			}
		} else {
			if (current < compared) {
				return -1;
			} else if (current > compared) {
				// for a min problem, compared is better if it's less than
				// current
				return 1;
			}
		}
		return 0;
	}

	@Override
	public Sample generateNeighbor(Sample s) {
		// TODO Define a neighbor as a graph with one vertex color changed

		Sample r = new Sample();
		ArrayList<Node> a = Collections.list(s.getTable().keys());

		// copies current sample
		for (Node k : a) {
			r.setSampledValue(k, s.getTable().get(k));
		}

		// picks node to change color of
		int r1 = (int) Math.floor(Math.random() * a.size());
		// System.out.println("element: " + r1);
		Node c = a.get(r1);

		// picks color to change it to
		int r2 = (int) Math.floor(Math.random() * c.getVals().length);

		// ensure we didn't pick same color as it already was
		while (c.getVals()[r2] == r.getTable().get(c)) {
			r2 = (int) Math.floor(Math.random() * c.getVals().length);
		}
		// System.out.println("color: " + r2);
		r.setSampledValue(c, c.getVals()[r2]);

		return r;
	}

	@Override
	public double getInvalidSolutionPenalty() {
		return -100;
	}

	@Override
	public void checkEdgeConstraints(Edge e) {
		PairwiseFactorEntry[] factors = e.getFactors();
		for (int i = 0; i < factors.length; i++) {
			// for graph-coloring problems, adjacent vertices
			// cannot have the same color
			if ((factors[i].getValA() == factors[i].getValB()) && (factors[i].getPotential() > 0.0)) {
//				System.out.println(e.getEndpoints().getFirst().getName() + " has the same color as " + e.getEndpoints().getLast().getName());
//				System.out.println(factors[i].getA().getName() + " has the same color as " + factors[i].getB().getName());
//				System.out.println(factors[i].getValA() + " == " + factors[i].getValB());
				throw new RuntimeException("ERROR: graph-color constraints violated");
			}
		}
	}

	@Override
	public void handleEdgeConstraints(Edge e) {
		PairwiseFactorEntry[] factors = e.getFactors();
		for (int i = 0; i < factors.length; i++) {
			// for graph-coloring problems, adjacent vertices
			// cannot have the same color
			if (factors[i].getValA() == factors[i].getValB()) {
				factors[i].setPotential(0.0);
			}
		}
	}

}
