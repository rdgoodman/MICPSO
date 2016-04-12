package PSO;

import java.io.FileNotFoundException;

import MN.MarkovNetwork;
import MN.ProbDist;
import MN.Sample;

public class MNParticle implements Particle {
	
	private Sample pBest_sample;
	private MNParticle pBest_position;
	private FitnessFunction f;
	private int numSamples;
	private double epsilon;
	
	private MarkovNetwork net;
	
	/**
	 * Creates a new particle for MICPSO (dependency handling)
	 * @param fileName the file containing the problem encoding
	 * @param f the fitness function for the problem
	 * @param numSamples the number of samples to create during each fitness evaluation
	 * @param epsilon scaling factor
	 * @throws FileNotFoundException
	 */
	public MNParticle(String fileName, FitnessFunction f, int numSamples, double epsilon) throws FileNotFoundException {
		this.f = f;
		this.numSamples = numSamples;
		this.epsilon = epsilon;
		
		// TODO: I/O stuff? I think a lot of this is actually taken care of for us already, come to think of it
		net = new MarkovNetwork(fileName);
		
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
	public void updateVelocity(double omega, double phi1, double phi2, Particle gBest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}


	@Override
	public Particle copy() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPBest(Sample s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adjustPBest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getProbs() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Sample getBestSample() {
		// TODO;
		return null;
	}

}
