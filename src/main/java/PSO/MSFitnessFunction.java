package PSO;

import MN.Sample;
import applicationProblems.MaxSatProblem;
import applicationProblems.Predicate;

public class MSFitnessFunction implements FitnessFunction {

	private MaxSatProblem maxSat;

	public MSFitnessFunction(MaxSatProblem maxSat) {
		this.maxSat = maxSat;
	}

	@Override
	public double calcFitness(Sample s) {
		int numSatisfied = 0;
		
		// counts number of satisfied predicates in the instance
		for (Predicate p : maxSat.getPredicates()){
			if (p.isSatisfied(s)){
				numSatisfied++;
			}
		}
		
		return numSatisfied;
	}

}
