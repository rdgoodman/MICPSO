package PSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;
import comparisons.Optimizer;

public class ICPSO extends Optimizer {

	private ArrayList<Particle> pop; // population
	// private FitnessFunction f;
	// global best position stored in the position of this particle
	private Particle gBest;
	// stores the best sample AND its fitness
	private Sample bestSample;

	// parameters:
	// private int iterations;
	private int numParticles;
	private int numSamples;
	private double epsilon;
	private double omega;
	private double phi1;
	private double phi2;
	// determines convergence
	private double threshold = 0.01;
	private double numToConsiderConverged = 20;

	// type of problem
	ApplicationProblem problem;

	// TODO: solution reporting
	int numFitnessEvals = 0;
	ArrayList<Double> fitnesses = new ArrayList<Double>();

	/**
	 * Creates an instance of either ICPSO or MICPSO depending on the Markov
	 * boolean argument
	 * 
	 * @param fileName
	 *            the name of the file encoding the optimization problem
	 * @param Markov
	 *            true if MICPSO, false if normal ICPSO
	 * @param numParticles
	 *            the number of particles in the swarm
	 * @param numSamples
	 *            the number of samples to use to evaluate particle fitness
	 * @param epsilon
	 *            scaling factor
	 * @param omega
	 *            multiplier on momentum
	 * @param phi1
	 *            multiplier on cognitive component
	 * @param phi2
	 *            multipier on social component
	 * @throws FileNotFoundException
	 */
	public ICPSO(String fileName, boolean Markov, int numParticles, int numSamples, double epsilon, double omega,
			double phi1, double phi2) throws FileNotFoundException {
		// this.iterations = iterations;
		this.numParticles = numParticles;
		this.numSamples = numSamples;
		this.epsilon = epsilon;
		this.omega = omega;
		this.phi1 = phi1;
		this.phi2 = phi2;

		pop = new ArrayList<Particle>();

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// construct the optimization problem itself
		problem = constructProblemFromFile(file);
		// TODO: for maxsat we need to revisit this
		// maybe pull out the predicates here...

		// loops to create population
		initializePop(fileName, Markov);
	}

	/**
	 * Initializes the population
	 * 
	 * @param markov
	 *            True if we want MICPSO, false if we want ICPSO
	 * @throws FileNotFoundException
	 */
	private void initializePop(String fileName, boolean markov) throws FileNotFoundException {
		// System.out.println("Creating population of size " + numParticles);

		// creates the correct number of the correct type of particle
		for (int i = 0; i < numParticles; i++) {
			// System.out.println(">> Particle " + i);

			if (markov) {
				pop.add(new MNParticle(fileName, problem, numSamples, epsilon));
			} else {
				pop.add(new ICParticle(fileName, problem, numSamples, epsilon));
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
		double prevBestSampleFit = 0; // remember to set this at some
										// point
		int runs = 0; // testing, remove

		// System.out.println("Evaluating Particles");
		// 1) evaluate all particles
		// 2) set gBest
		double maxFit = problem.getWorstValue();
		for (Particle p : pop) {
			p.calcFitness();

			// number of evals done is incremented by numSamples
			numFitnessEvals += numSamples;

			double fit = p.getBestSample().getFitness();
			// find initial gBest
			if (problem.compare(maxFit, fit) == 1) {
				maxFit = fit;
				bestSample = p.getBestSample();
				gBest = p.copy();
				// System.out.println("Global best:");
				// bestSample.print();
			}
		}

		while (!terminated) {
			// System.out.println("\n \n
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			// System.out.println(" %%%%%%%%%%%%% RUN " + runs);
			// System.out.println(" %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

			// iterate through all particles
			for (Particle p : pop) {
				// System.out.println("\n >>>> Particle ");
				// p.print();

				// 1) update velocity
//				System.out.println(">> Velocity Update");
				p.updateVelocity(omega, phi1, phi2, gBest);

				// 2) update position
//				System.out.println(">> Position Update ");
				p.updatePosition();
				// p.print();

				// 2.5) change the previous best sample fitness
				prevBestSampleFit = bestSample.getFitness();

				// 3) evaluate fitness
//				System.out.println(">> Fitness Calc");
				double fit = p.calcFitness(); // this is never used
				// number of evals done is incremented by numSamples
				numFitnessEvals += numSamples;

				double sampleFit = p.getBestSample().getFitness();

				// System.out.println("Sample fitness: " + sampleFit + " >? Best
				// fitness: " + bestSample.getFitness());

				// compares and adjusts gBest
				if (problem.compare(bestSample.getFitness(), sampleFit) == 1) {
					setBestSample(p.getBestSample());
					// set gBest and bias
					setGBest(p);
				}
			}

			// updating fitness for evaluation
			fitnesses.add(bestSample.getFitness());

			// Next half-dozen or so lines used to determine convergence
			if (Math.abs(prevBestSampleFit - bestSample.getFitness()) < threshold) {
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
			// System.out.println(">>>>>> RUNS: " + runs);
		}

		System.out.println("Returning best sample:");
		bestSample.print();
		System.out.println("Fitness: " + bestSample.getFitness());

		System.out.println("Fitness Aross Evals:");
		for (Double d : fitnesses) {
			System.out.print(d + " ");
		}
		System.out.println();

		return bestSample;
	}

	/**
	 * Creates a copy of the best sample
	 */
	private void setBestSample(Sample s) {
		// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// System.out.println("!!! New best sample with fitness " +
		// s.getFitness() + "!!!");
		bestSample = s;
	}

	/**
	 * Adjusts probabilities to bias gBest toward best sample
	 */
	public void adjustGBest(Particle p) {
		// adjust p's distribution based on its best sample
		// (which is now also the global best sample)
		p.adjustPBest();
	}

	/**
	 * Sets the global best
	 */
	private void setGBest(Particle p) {
		adjustGBest(p);
		gBest = p.copy();
	}

	public int getNumFitnessEvals() {
		return numFitnessEvals;
	}

	public void setNumFitnessEvals(int numFitnessEvals) {
		this.numFitnessEvals = numFitnessEvals;
	}

	public ArrayList<Double> getFitnesses() {
		return fitnesses;
	}

	public void setFitnesses(ArrayList<Double> fitnesses) {
		this.fitnesses = fitnesses;
	}

	public ApplicationProblem getProblem() {
		return problem;
	}

}
