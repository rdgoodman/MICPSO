package PSO;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import MN.Sample;

public class IPSO {

	private ArrayList<IntegerParticle> pop;
	int[] gBest;
	double gBest_fitness;
	private FitnessFunction f;
	Sample s; // constructed from gBest

	// tunables
	private int numParticles;
	private double omega;
	private double phi1;
	private double phi2;

	// determines convergence
	private double threshold = 0.01;
	private double numToConsiderConverged = 20;

	// type of problem
	boolean graphColoring = false;
	int optimalSolution;

	public IPSO(String fileName, int numParticles, double omega, double phi1, double phi2)
			throws FileNotFoundException {
		this.numParticles = numParticles;
		this.omega = omega;
		this.phi1 = phi1;
		this.phi2 = phi2;

		// TODO: i/o stuff
		initializePop(fileName);
	}

	/**
	 * Randomly initializes population
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	private void initializePop(String fileName) throws FileNotFoundException {
		pop = new ArrayList<IntegerParticle>();
		for (int i = 0; i < numParticles; i++) {
			pop.add(new IntegerParticle(fileName, f));
		}
	}

	/**
	 * Actually carries out optimization
	 * 
	 * @return
	 */
	public Sample run() {
		// TODO: make a method to build samples from double[]

		// termination criterion
		int runsUnchanged = 0;
		boolean terminated = false;
		double prevBestSampleFit = 0; // TODO: remember to set this at some
										// point
		int runs = 0; // TODO: testing, remove

		System.out.println("Evaluating Particles");
		// 1) evaluate all particles
		// 2) set gBest
		double maxFit = -Double.MAX_VALUE; // TODO: this is dangerous
		for (IntegerParticle p : pop) {
			p.calcFitness();
			double fit = p.calcFitness();
			if (fit > maxFit) { // TODO: again, assuming max
				maxFit = fit;
				int[] b = p.getPosition();
				gBest = new int[b.length];

				for (int i = 0; i < b.length; i++) {
					gBest[i] = b[i];
				}
				gBest_fitness = fit;
				s = p.buildSampleFromPosition();

				System.out.println("Global best:");
				// TODO: print method for IntegerParticle
			}
		}

		while (!terminated) {
			System.out.println("\n \n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println(" %%%%%%%%%%%%% RUN " + runs);
			System.out.println(" %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

			// iterate through all particles
			for (IntegerParticle p : pop) {
				System.out.println("\n >>>> Particle ");
				// TODO: print method
				//p.print();

				// 1) update velocity
				System.out.println(">> Velocity Update");
				p.updateVelocity(omega, phi1, phi2, gBest);

				// 2) update position
				System.out.println(">> Position Update ");
				// TODO: print method
				//p.print();

				// 2.5) change the previous best sample fitness
				prevBestSampleFit = gBest_fitness;

				// 3) evaluate fitness
				double fit = p.calcFitness(); 

				// TODO: recall this is a max problem, refactor that later
				if (fit > gBest_fitness) {
					// set gBest
					int[] b = p.getPosition();
					gBest = new int[b.length];

					for (int i = 0; i < b.length; i++) {
						gBest[i] = b[i];
					}
					gBest_fitness = fit;
					s = p.buildSampleFromPosition();
				}
			}

			// Next half-dozen or so lines used to determine convergence
			if (Math.abs(prevBestSampleFit - gBest_fitness) < threshold) {
				runsUnchanged++;
			} else {
				runsUnchanged = 0;
			}
			if (runsUnchanged >= numToConsiderConverged) {
				// return if the solution hasn't significantly changed in a
				// certain
				// number of iterations
				terminated = true;
			}
			runs++;
		}

		System.out.println("Returning best sample:");
		s.print();
		return s;
	}

}
