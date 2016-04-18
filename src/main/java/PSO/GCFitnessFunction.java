package PSO;

import java.util.ArrayList;
import java.util.Set;

import MN.Sample;
import MN.Node;

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
		// step 1: get list of keys
		Set<Node> keys = s.getTable().keySet();
		
		// step 2: iterate with counter
		ArrayList<Integer> usedColors = new ArrayList<Integer>();;
		
		for (Node k : keys){
			if (!usedColors.contains(s.getTable().get(k))){
				// adds this key's value to the list if it isn't already there
				usedColors.add(s.getTable().get(k));
			}
		}
				
		// TODO: maximization problem
		// TODO: throw an exception if this is > 0
		double fit = optimal - usedColors.size();
		s.setFitness(fit);
		return fit;
	}

}
