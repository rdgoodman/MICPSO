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
		
		// update velocity for each prob dist
//		for (int i = 0; i < probs.length; i++){
//			// call update for each element
//			probs[i].updateVelocity(omega, cognitive, social, ((ICParticle) gBest).getProbs()[i], pBest_position.getProbs()[i]);
//		}	
		
		// TODO: update velocity for each FactorEntry...oy vay
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

	@Override
	public Object[] getProbs() {
		// TODO This is used to get the position of gbest & pbest in velocity update
		// frighteningly, I think this may involve extracting the FactorEntries from
		// all the edges in the MN...
		return null;
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
