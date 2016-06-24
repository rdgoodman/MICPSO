package tuning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import PSO.ICPSO;

public class MTuningExperiment {

	String filePath1;
	String filePath2;
	String filePath3;

	public MTuningExperiment(String file1, String file2, String file3, ArrayList<Integer> pNums,
			ArrayList<Integer> sNums, ArrayList<Double> epsilons, ArrayList<Double> omegas, ArrayList<Double> phi1s,
			ArrayList<Double> phi2s) throws FileNotFoundException {

		filePath1 = file1;
		filePath2 = file2;
		filePath3 = file3;

		tuneGeneral(pNums, sNums, epsilons);
		tuneMultipliers(omegas, phi1s, phi2s);
	}

	/**
	 * Tunes number of particles/samples, and epsilon
	 * 
	 * @throws FileNotFoundException
	 */
	private void tuneGeneral(ArrayList<Integer> pNums, ArrayList<Integer> sNums, ArrayList<Double> epsilons)
			throws FileNotFoundException {

		// reports results of combination of params (average)
		String averageFilePath = "src/main/resources/TuningExperiments/MIC_GenAverages.txt";

		File avgOutput = new File(averageFilePath);
		if (!avgOutput.exists()) {
			try {
				avgOutput.createNewFile();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Integer numParticles : pNums) {
			for (Integer numSamples : sNums) {
				for (Double epsilon : epsilons) {
					FileWriter avgWriter = null;

					// run each 10 times
					double averageFitnessAcrossRuns = 0;

					for (int i = 0; i < 10; i++) {

						ICPSO m1 = new ICPSO(filePath1, true, numParticles, numSamples, epsilon, 0.7, 1.4, 1.4);
						ICPSO m2 = new ICPSO(filePath2, true, numParticles, numSamples, epsilon, 0.7, 1.4, 1.4);
						ICPSO m3 = new ICPSO(filePath3, true, numParticles, numSamples, epsilon, 0.7, 1.4, 1.4);

						double fit1 = m1.run().getFitness();
						System.out.println("m1 ran");
						double fit2 = m2.run().getFitness();
						System.out.println("m1 ran");
						double fit3 = m3.run().getFitness();
						System.out.println("m1 ran");

						double averageFitness = (fit1 + fit2 + fit3) / 3;
						averageFitnessAcrossRuns += averageFitness;

					}

					averageFitnessAcrossRuns /= 10;

					try {
						avgWriter = new FileWriter(avgOutput, true);

						// notes, in avg file, what parameter combo led to the
						// results
						avgWriter.write("\n" + "#P = " + numParticles + " #S = " + numSamples + " e = " + epsilon
								+ " o = 0.7" + " p1 = 1.4" + " p2 = 1.4");
						avgWriter.write("\n" + averageFitnessAcrossRuns);
						avgWriter.flush();
						// TODO: close/flush
					} catch (IOException f) {
						f.printStackTrace();
					}

				}
			}
		}

	}

	/**
	 * Tunes omega, phi1, phi2
	 * 
	 * @throws FileNotFoundException
	 */
	private void tuneMultipliers(ArrayList<Double> omegas, ArrayList<Double> phi1s, ArrayList<Double> phi2s)
			throws FileNotFoundException {
		// reports results of combination of params (average)
		String averageFilePath = "src/main/resources/TuningExperiments/MIC_MultAverages.txt";

		File avgOutput = new File(averageFilePath);
		if (!avgOutput.exists()) {
			try {
				avgOutput.createNewFile();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Double omega : omegas) {
			for (Double phi1 : phi1s) {
				for (Double phi2 : phi2s) {
					FileWriter avgWriter = null;

					// run each 10 times
					double averageFitnessAcrossRuns = 0;

					for (int i = 0; i < 10; i++) {

						ICPSO m1 = new ICPSO(filePath1, true, 10, 3, 0.75, omega, phi1, phi2);
						ICPSO m2 = new ICPSO(filePath2, true, 10, 3, 0.75, omega, phi1, phi2);
						ICPSO m3 = new ICPSO(filePath3, true, 10, 3, 0.75, omega, phi1, phi2);

						double fit1 = m1.run().getFitness();
						System.out.println("m1 ran");
						double fit2 = m2.run().getFitness();
						System.out.println("m1 ran");
						double fit3 = m3.run().getFitness();
						System.out.println("m1 ran");

						double averageFitness = (fit1 + fit2 + fit3) / 3;
						averageFitnessAcrossRuns += averageFitness;

					}

					averageFitnessAcrossRuns /= 10;

					try {
						avgWriter = new FileWriter(avgOutput, true);

						// notes, in avg file, what parameter combo led to the
						// results
						avgWriter.write("\n" + "#P = 10 " + " #S = 3 " + " e = 0.75 " + " o = " + omega + " p1 = "
								+ phi1 + " p2 = " + phi2);
						avgWriter.write("\n" + averageFitnessAcrossRuns);
						avgWriter.flush();

					} catch (IOException f) {
						f.printStackTrace();
					}

				}
			}
		}
	}

}
