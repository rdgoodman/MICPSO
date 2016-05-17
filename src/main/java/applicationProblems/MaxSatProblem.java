package applicationProblems;

import java.util.ArrayList;
import java.util.Collections;

import MN.Edge;
import MN.Node;
import MN.Sample;
import PSO.FitnessFunction;
import PSO.MSFitnessFunction;

public class MaxSatProblem implements ApplicationProblem {

	ArrayList<Predicate> predicates;
	MSFitnessFunction f;

	public MaxSatProblem(ArrayList<Predicate> predicates) {
		// TODO: doesn't currently take an optimal, but could...
		this.predicates = predicates;
	}

	@Override
	public boolean satisfiesConstraints(Sample s, ArrayList<Edge> edgesArray) {
		// TODO: technically, no constraints to violate...right?
		return true;
	}

	@Override
	public void createFitnessFunction(int optimal) {
		f = new MSFitnessFunction(this);
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
		// changes assignment to one variable		
		Sample r = new Sample();
		
		ArrayList<Node> a = Collections.list(s.getTable().keys());

		// copies current sample
		for (Node k : a) {
			r.setSampledValue(k, s.getTable().get(k));
		}
		
		// picks node to change assignment to
		int r1 = (int) Math.floor(Math.random() * a.size());
		// System.out.println("element: " + r1);
		Node c = a.get(r1);
		
		// flips value
		if (s.getTable().get(c) == 0){
			r.setSampledValue(c, 1);
		} else {
			r.setSampledValue(c, 0);
		}
		
		return r;
	}

	@Override
	public double getInvalidSolutionPenalty() {
		// not possible to have an invalid solution, I think
		return 0;
	}

	@Override
	public void checkEdgeConstraints(Edge e) {
		// no edge constraints

	}

	@Override
	public void handleEdgeConstraints(Edge e) {
		// again, no constraints, so...
	}

	public ArrayList<Predicate> getPredicates() {
		return predicates;
	}
	
//	/**
//	 * Flips the truth assignment to this Node's variable for all predicates in instance
//	 * @param n
//	 */
//	public void flipValue(Node n){
//		for (Predicate p : predicates){
//			p.flipValue(n);
//		}
//	}

}
