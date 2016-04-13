package PSO;

import java.io.FileNotFoundException;

import MN.MarkovNetwork;
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
		return net.sample();
	}

	@Override
	public double calcFitness() {
		double particleFit = 0;
		// average over a certain number of samples
		for (int i = 0; i < numSamples; i++){
			// 1) generate a sample and calculate its fitness
			Sample s = sample();
			double fit = f.calcFitness(s);
			particleFit += fit;
			System.out.println("Sample fitness: " + fit);
			
			// 2) Save this sample if it's the new pBest
			if (pBest_sample == null || fit > pBest_sample.getFitness()){ 
				// TODO: recall that this assumes a max problem, refactor later
				setPBest(s);
			}
		}	
		return particleFit / numSamples;
	}



	@Override
	public void updateVelocity(double omega, double phi1, double phi2, Particle gBest) {
		// decide on multipliers
		double cognitive = Math.random() * phi1;
		double social = Math.random() * phi2;
		
		// 1) call getAllPotentials (P) and getAllVelocities (V)
		double[][] P = net.getAllPotentials();
		double[][] globalBest = ((MNParticle) gBest).getAllPotentials();
		double[][] personalBest = pBest_position.getAllPotentials();
		double[][] V = net.getAllVelocities();
		
		// 2) update velocity [][] component-wise using V and P
		for (int e = 0; e < V.length; e++){
			for (int f = 0; f < V[e].length; f++){
				V[e][f] = (omega * P[e][f]) + (cognitive * personalBest[e][f]) + (social * globalBest[e][f]);
			}
		}
		// 3) call net.adjustAllVelocities(new velocity [][])
		net.adjustAllVelocities(V); // TODO this might be unnecessary, test		
	}
	
	@Override
	public void updatePosition() {
		net.updatePotentials();
	}


	@Override
	public MNParticle copy() {
		// TODO: I think we'll need a copy() method for Markov Networks in order to
		// make a copy constructor for the particle. Yikes.
		return null;
	}


	@Override
	public void setPBest(Sample s) {
		System.out.println(" ^ new ^ personal ^ best");
		pBest_sample = s;
		adjustPBest();
		// sets pBest dist!
		pBest_position = this.copy();		
	}

	@Override
	public void adjustPBest() {
		// TODO Auto-generated method stub
		
	}

	private double[][] getAllPotentials(){
		return net.getAllPotentials();
	}
	
	@Override
	public Sample getBestSample() {
		return pBest_sample;
	}

	@Override
	public void print() {
		net.print();
	}

}
