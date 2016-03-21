package PSO;

import java.util.ArrayList;

import MN.Sample;

public class ICPSO {

	private ArrayList<Particle> pop; // population
	private FitnessFunction f;
	// global best position will be stored in the position element(?) of this particle, along with fitness
	private Particle gBest; 
	// TODO: we need to store the best sample AND its fitness as well as what generated it...
	private Sample bestSample;
	private double bestSamplefit; // TODO: probably better to have this as a property of the sample
	
	// parameters:
	private int iterations;
	private int numParticles;
	private int numSamples;
	private double epsilon;
	private double omega;
	private double phi1;
	private double phi2;
	
	/**
	 * Returns the global best sample from the swarm
	 * @return
	 */
	public Sample run(){
		// TODO stub
		return null;
	}
	
	/**
	 * Creates a copy of the best sample
	 */
	private void setBestSample(){
		// TODO stub
	}
	
	/**
	 * Adjusts probabilities to bias gBest toward best sample
	 */
	public void adjustGBest(Particle p) {
		// TODO stub
	}
	
	/**
	 * Sets the global best
	 */
	private void setGBest(Particle p){
		// TODO stub
	}

}
