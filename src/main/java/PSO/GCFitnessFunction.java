package PSO;

import MN.Sample;

public class GCFitnessFunction implements FitnessFunction{
	
	// TODO: this needs to be read in from file as well
	private int optimal;
	
	public GCFitnessFunction(int optimal){
		this.optimal = optimal;
	}

	@Override
	public double calcFitness(Sample s) {
		// TODO Auto-generated method stub
		// TODO count up number of colors/labels used and compare with optimal
		return 0;
	}

}
