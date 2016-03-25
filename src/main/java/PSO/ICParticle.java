package PSO;

import MN.ProbDist;
import MN.Sample;

public class ICParticle implements Particle {
	
	private Sample pBest;
	private ProbDist[] probs; 
	// TODO: will have to create Nodes for our variables as well, even though this isn't an MN
	// so we can create samples

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
