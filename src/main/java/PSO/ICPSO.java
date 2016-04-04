package PSO;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import MN.Sample;

public class ICPSO {

	private ArrayList<Particle> pop; // population
	private FitnessFunction f;
	// global best position stored in the position of this particle
	private Particle gBest;
	// stores the best sample AND its fitness 
	private Sample bestSample;
	//private double bestSampleFit; // probably better to have this as a
									// property of the sample

	// parameters:
	//private int iterations;
	private int numParticles;
	private int numSamples;
	private double epsilon;
	private double omega;
	private double phi1;
	private double phi2;
	// determines convergence
	private double threshold = 0.01;
	private double numToConsiderConverged = 50;

	public ICPSO(String fileName, boolean Markov, int iterations, int numParticles, int numSamples, double epsilon,
			double omega, double phi1, double phi2) throws FileNotFoundException {
		//this.iterations = iterations;
		this.numParticles = numParticles;
		this.numSamples = numSamples;
		this.epsilon = epsilon;
		this.omega = omega;
		this.phi1 = phi1;
		this.phi2 = phi2;
						
		
		// loop to create population:
		// Particle constructor takes fileName
		
		
		// TODO: based on info about problem type from file,
		// create a fitness function
		
		boolean graphColoring = true;
		int optimal = 3; // TODO: read from file, obviously
		
		if (graphColoring){
			f = new GCFitnessFunction(optimal);

		} else {
			// TODO
		}
		
		initializePop(fileName, Markov);		
	}

	/**
	 * Initializes the population
	 * @param markov True if we want MICPSO, false if we want ICPSO
	 * @throws FileNotFoundException 
	 */
	private void initializePop(String fileName, boolean markov) throws FileNotFoundException {		
		// creates the correct number of the correct type of particle
		for (int i = 0; i < numParticles; i++){
			if (markov){
				pop.add(new MNParticle());
			} else {
				pop.add(new ICParticle(fileName, f, numSamples, epsilon));
			}
		}				
	}

	/**
	 * Returns the global best sample from the swarm
	 */
	public Sample run() {		
		// termination criterion
		int runsUnchanged = 0;
		boolean terminated = false;
		double prevBestSampleFit = 0; // TODO: remember to set this at some point
		
		while (!terminated){
			
			// 1) evaluate all particles			
			// 2) set gBest (maybe pull a collections.sort?)
			double maxFit = -Double.MAX_VALUE; // TODO: this is dangerous
			for (Particle p: pop){
				p.calcFitness();
				double fit = p.getBestSample().getFitness();
				if (fit > maxFit){ // TODO: again, assuming max
					maxFit = fit;
					bestSample = p.getBestSample();
					gBest = p.copy();
				}
			}
			
			// iterate through all particles
			for (Particle p : pop){
							
				// 1) update velocity
				p.updateVelocity(omega, phi1, phi2, gBest);
				
				// 2) update position
				p.updatePosition();
				
				// 2.5) change the previous best sample fitness
				prevBestSampleFit = bestSample.getFitness();
				
				// 3) evaluate fitness
				double fit = p.calcFitness(); // this is never used
				double sampleFit = p.getBestSample().getFitness();
				
				// TODO: recall this is a max problem, refactor that later
				if (sampleFit > bestSample.getFitness()){
					setBestSample(p.getBestSample());
					// set gBest and bias
					setGBest(p);
				}			
			}
			
			
			// Next half-dozen or so lines used to determine convergence
			if (Math.abs(prevBestSampleFit - bestSample.getFitness()) < threshold){
				runsUnchanged++;
			} else {
				runsUnchanged = 0;
			}			
			if (runsUnchanged >= numToConsiderConverged){
				// return if the solution hasn't significantly changed in a certain
				// number of iterations
				
				terminated = true;
			}
		}				
		
		return bestSample;
	}

	/**
	 * Creates a copy of the best sample
	 */
	private void setBestSample(Sample s) {
		bestSample = s;
	}

	/**
	 * Adjusts probabilities to bias gBest toward best sample
	 */
	public void adjustGBest(Particle p) {
		// TODO stub. Need to figure out how to make this work with both types of particles...
		// maybe call their adjustPBest method, overload it somehow?
	}

	/**
	 * Sets the global best
	 */
	private void setGBest(Particle p) {
		adjustGBest(p);
		gBest = p.copy();
	}

}
