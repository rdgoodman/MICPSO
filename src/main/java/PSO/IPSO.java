package PSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.Node;
import MN.Sample;
import applicationProblems.ApplicationProblem;
import applicationProblems.GraphColoringProblem;

public class IPSO {

	public ArrayList<IntegerParticle> pop;
	int[] gBest;
	double gBest_fitness;
	// private FitnessFunction f;
	Sample s; // constructed from gBest

	// tunables
	private int numParticles;
	private double omega;
	private double phi1;
	private double phi2;

	// determines convergence
	private double threshold = 0.01;
	private double numToConsiderConverged = 20;

	// TODO: solution reporting
	int numFitnessEvals = 0;
	ArrayList<Double> fitnesses = new ArrayList<Double>();

	// type of problem
	ApplicationProblem problem;

	public IPSO(String fileName, int numParticles, double omega, double phi1, double phi2)
			throws FileNotFoundException {
		this.numParticles = numParticles;
		this.omega = omega;
		this.phi1 = phi1;
		this.phi2 = phi2;

		// based on info about problem type from file,
		// create a fitness function

		Scanner scanner = null;

		// The entire file name, for retrieving the Markov net file
		File file = new File(fileName);

		// Reads in the nodes, edges and values in from a specifically formatted
		// file

		String problemType;
		int optimal;

		try {
			scanner = new Scanner(new BufferedReader(new FileReader(file)));

			String tempVal;

			/*
			 * Reads the file, ignoring lines with % (which are comment lines).
			 * File is structured so that the nodes are first (comma separated),
			 * followed by the edges (in form A, B semi-colon separated) and
			 * then the values for the variables (in form A: 0, 1). The values
			 * need to be in the same order as the node variables. At this
			 * stage, the variables are read in as strings, and after the file
			 * is closed they are converted to the appropriate object type
			 * (i.e., Node or Edge objects).
			 * 
			 */
			// Read the first line in the file
			tempVal = scanner.nextLine();

			// gets the node info first
			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = scanner.nextLine();
			}

			// only getting type of problem
			problemType = tempVal;

			// keep scanning for the next non-empty line
			if (scanner.nextLine().equals("")) {
				tempVal = scanner.nextLine();
			}

			// checks for comments, when present, discards them
			while (tempVal.startsWith("%")) {
				tempVal = scanner.nextLine();
			}

			// gets optimal solution size
			optimal = Integer.valueOf(tempVal);
			System.out.println("Size: " + optimal);

			// TODO: create problem type
			if (problemType.equals("GC")) {
				problem = new GraphColoringProblem(optimal);
			}

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

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
			pop.add(new IntegerParticle(fileName, problem));
		}
	}

	/**
	 * Actually carries out optimization
	 * 
	 * @return
	 */
	public Sample run() {

		// termination criterion
		int runsUnchanged = 0;
		boolean terminated = false;
		double prevBestSampleFit = 0; // remember to set this
		int runs = 1; // TODO: testing, remove

		System.out.println("Evaluating Particles");
		// 1) evaluate all particles
		// 2) set gBest
		double maxFit = problem.getWorstValue();

		for (IntegerParticle p : pop) {
			p.calcFitness();

			// for solution reporting
			numFitnessEvals++;

			double fit = p.calcFitness();
			// for solution reporting
			numFitnessEvals++;

			// find initial gBest
			if (problem.compare(maxFit, fit) == 1) {
				maxFit = fit;
				int[] b = p.getPosition();
				gBest = new int[b.length];

				for (int i = 0; i < b.length; i++) {
					gBest[i] = b[i];
				}
				gBest_fitness = fit;
				s = p.buildSampleFromPosition();

				System.out.println("Global Best " + gBest_fitness);
			}
		}

		while (!terminated) {
			System.out.println("\n \n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println(" %%%%%%%%%%%%% RUN " + runs);
			System.out.println(" %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

			int particleNumber = 0;

			// iterate through all particles
			for (IntegerParticle p : pop) {
				System.out.println("\n >>>> Particle " + particleNumber);
				particleNumber++;

				// 1) update velocity
				System.out.println(">> Velocity Update");
				p.updateVelocity(omega, phi1, phi2, gBest);

				// 2) update position
				System.out.println(">> Position Update ");
				p.updatePosition();
				// print for this particle
				p.print();

				// 2.5) change the previous best sample fitness
				prevBestSampleFit = gBest_fitness;

				// 3) evaluate fitness
				double fit = p.calcFitness();
				numFitnessEvals++;

				// compare and update gBest
				if (problem.compare(gBest_fitness, fit) == 1) {
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

			// TODO: updating fitness for evaluation
			fitnesses.add(gBest_fitness);

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

		// System.out.println("Final fitness: ");
		// System.out.println(gBest_fitness);
		// System.out.println();
		System.out.println("Returning best sample:");
		s.print();
		// System.out.println("Number of fitness evaluations: " +
		// numFitnessEvals);
		return s;
	}

	public double getBestFitness() {
		return gBest_fitness;
	}

	public int getNumFitnessEvals() {
		return numFitnessEvals;
	}

	public ArrayList<Double> getFitnesses() {
		return fitnesses;
	}

}
