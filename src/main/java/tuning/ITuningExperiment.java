package tuning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import PSO.IPSO;

public class ITuningExperiment {

	String filePath1;
	String filePath2;
	String filePath3;

	public ITuningExperiment(String file1, String file2, String file3, ArrayList<Integer> pNums,
			ArrayList<Double> omegas, ArrayList<Double> phi1s, ArrayList<Double> phi2s) throws FileNotFoundException {

		filePath1 = file1;
		filePath2 = file2;
		filePath3 = file3;

		tuneGeneral(pNums);
		tuneMultipliers(omegas, phi1s, phi2s);
	}

	/**
	 * Tunes number of particles/samples, and epsilon
	 * 
	 * @throws FileNotFoundException
	 */
	private void tuneGeneral(ArrayList<Integer> pNums) throws FileNotFoundException {
		// reports results of combination of params (average)
		String averageFilePath = "src/main/resources/TuningExperiments/I_GenAverages.txt";

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

			FileWriter avgWriter = null;

			// run each 10 times
			double averageFitnessAcrossRuns = 0;

			for (int i = 0; i < 10; i++) {

				IPSO i1 = new IPSO(filePath1, numParticles, 0.7, 1.4, 1.4);
				IPSO i2 = new IPSO(filePath2, numParticles, 0.7, 1.4, 1.4);
				IPSO i3 = new IPSO(filePath3, numParticles, 0.7, 1.4, 1.4);

				i1.run();
				double fit1 = i1.getBestFitness();
				System.out.println("i1 ran");
				i2.run();
				double fit2 = i2.getBestFitness();
				System.out.println("i2 ran");
				i3.run();
				double fit3 = i3.getBestFitness();
				System.out.println("i3 ran");

				double averageFitness = (fit1 + fit2 + fit3) / 3;
				averageFitnessAcrossRuns += averageFitness;

			}

			averageFitnessAcrossRuns /= 10;

			try {
				avgWriter = new FileWriter(avgOutput, true);

				// notes, in avg file, what parameter combo led to the
				// results
				avgWriter.write("\n" + "#P = " + numParticles + " o = 0.7" + " p1 = 1.4" + " p2 = 1.4");
				avgWriter.write("\n" + averageFitnessAcrossRuns);
			} catch (IOException f) {
				f.printStackTrace();
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
		String averageFilePath = "src/main/resources/TuningExperiments/I_MultAverages.txt";

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

						IPSO i1 = new IPSO(filePath1, 10, omega, phi1, phi2);
						IPSO i2 = new IPSO(filePath2, 10, omega, phi1, phi2);
						IPSO i3 = new IPSO(filePath3, 10, omega, phi1, phi2);

						i1.run();
						double fit1 = i1.getBestFitness();
						System.out.println("i1 ran");
						i2.run();
						double fit2 = i2.getBestFitness();
						System.out.println("i2 ran");
						i3.run();
						double fit3 = i3.getBestFitness();
						System.out.println("i3 ran");

						double averageFitness = (fit1 + fit2 + fit3) / 3;
						averageFitnessAcrossRuns += averageFitness;
					}

					averageFitnessAcrossRuns /= 10;

					try {
						avgWriter = new FileWriter(avgOutput, true);

						// notes, in avg file, what parameter combo led to the
						// results
						avgWriter.write("\n" + "#P = 10 " + " o = " + omega + " p1 = " + phi1 + " p2 = " + phi2);
						avgWriter.write("\n" + averageFitnessAcrossRuns);
						avgWriter.close();

					} catch (IOException f) {
						f.printStackTrace();
					}

				}
			}
		}
	}

}
