package PSO;

import MN.Node;
import MN.ProbDist;
import MN.Sample;

public class ICParticle implements Particle {
	
	private Sample pBest;
	private Node[] variables;
	private ProbDist[] probs; 

	
	public ICParticle(String filename){
		// TODO: read stuff from file. It's hard.
		
		// procedure:
		
		// 1) create Nodes
		// 2) foreach Node, create a ProbDist with its list of values and itself
	}

	@Override
	public Sample sample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double calcFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateVelocity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPBest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustPBest() {
		// TODO Auto-generated method stub

	}

	@Override
	public Particle copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bias(double epsilon) {
		// TODO Auto-generated method stub
		
	}

}
