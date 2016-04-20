package tuning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import PSO.IPSO;

public class ITuningExperiment {
	public ITuningExperiment(ArrayList<Integer> pNums, ArrayList<Double> omegas, ArrayList<Double> phi1s, ArrayList<Double> phi2s) throws FileNotFoundException {
		tuneGeneral(pNums);
		tuneMultipliers(omegas, phi1s, phi2s);
	}

	/**
	 * Tunes number of particles/samples, and epsilon
	 * 
	 * @throws FileNotFoundException
	 */
	private void tuneGeneral(ArrayList<Integer> pNums)
			throws FileNotFoundException {
		// reports results of individual params (raw numbers)
		String singleParamFilePath = "";
		// reports results of combination of params (average)
		String averageFilePath = "src/main/resources/Iresults/genAverage.txt";

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
					singleParamFilePath = "src/main/resources/Iresults/" + numParticles + ".txt";

					File tuneOutput = new File(singleParamFilePath);
					if (!tuneOutput.exists()) {
						try {
							tuneOutput.createNewFile();
						} catch (IOException e) {
							// Auto-generated catch block
							e.printStackTrace();
						}
					}

					FileWriter singleWriter = null;
					FileWriter avgWriter = null;

					// run each 10 times
					double averageFitnessAcrossRuns = 0;

					for (int i = 0; i < 10; i++) {

						// TODO: are we doing multiple files?
						IPSO i1 = new IPSO("src/main/resources/graphColor08Node_10Values.txt", numParticles, 0.7, 1.4, 1.4);
						IPSO i2 = new IPSO("src/main/resources/graphColor10Node_10Values.txt", numParticles, 0.7, 1.4, 1.4);
						IPSO i3 = new IPSO("src/main/resources/graphColor20Node_10Values.txt", numParticles, 0.7, 1.4, 1.4);

						double fit1 = i1.run().getFitness();
						System.out.println("i1 ran");
						double fit2 = i2.run().getFitness();
						System.out.println("i1 ran");
						double fit3 = i3.run().getFitness();
						System.out.println("i1 ran");

						double averageFitness = (fit1 + fit2 + fit3) / 3;
						averageFitnessAcrossRuns += averageFitness;

						try {
							singleWriter = new FileWriter(tuneOutput, true);

							// writes raw number to individual parameter file
							singleWriter.write("\n" + averageFitness);
							singleWriter.close();

						} catch (IOException f) {
							f.printStackTrace();
						}

					}

					averageFitnessAcrossRuns /= 10;

					try {
						avgWriter = new FileWriter(avgOutput, true);

						// notes, in avg file, what parameter combo led to the
						// results
						avgWriter.write("\n" + "#P = " + numParticles + " o = 0.7" + " p1 = 1.4" + " p2 = 1.4");
						avgWriter.write("\n" + averageFitnessAcrossRuns);
						
						avgWriter.close();
					} catch (IOException f) {
						f.printStackTrace();
					}

				}
			}
		

	

	/**
	 * Tunes omega, phi1, phi2
	 * @throws FileNotFoundException 
	 */
	private void tuneMultipliers(ArrayList<Double> omegas, ArrayList<Double> phi1s, ArrayList<Double> phi2s) throws FileNotFoundException {
		// reports results of individual params (raw numbers)
		String singleParamFilePath = "";
		// reports results of combination of params (average)
		String averageFilePath = "src/main/resources/ICresults/multAverage.txt";

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
					singleParamFilePath = "src/main/resources/Iresults/" + omega + "_" + phi1 + "_"
							+ phi2 + ".txt";

					File tuneOutput = new File(singleParamFilePath);
					if (!tuneOutput.exists()) {
						try {
							tuneOutput.createNewFile();
						} catch (IOException e) {
							// Auto-generated catch block
							e.printStackTrace();
						}
					}

					FileWriter singleWriter = null;
					FileWriter avgWriter = null;

					// run each 10 times
					double averageFitnessAcrossRuns = 0;

					for (int i = 0; i < 10; i++) {

						// TODO: are we doing multiple files?
						IPSO i1 = new IPSO("src/main/resources/graphColor08Node_10Values.txt", 10, omega, phi1, phi2);
						IPSO i2 = new IPSO("src/main/resources/graphColor10Node_10Values.txt", 10, omega, phi1, phi2);
						IPSO i3 = new IPSO("src/main/resources/graphColor20Node_10Values.txt", 10, omega, phi1, phi2);

						double fit1 = i1.run().getFitness();
						System.out.println("i1 ran");
						double fit2 = i2.run().getFitness();
						System.out.println("i2 ran");
						double fit3 = i3.run().getFitness();
						System.out.println("i3 ran");

						double averageFitness = (fit1 + fit2 + fit3) / 3;
						averageFitnessAcrossRuns += averageFitness;

						try {
							singleWriter = new FileWriter(tuneOutput, true);

							// writes raw number to individual parameter file
							singleWriter.write("\n" + averageFitness);
							singleWriter.close();

						} catch (IOException f) {
							f.printStackTrace();
						}

					}

					averageFitnessAcrossRuns /= 10;

					try {
						avgWriter = new FileWriter(avgOutput, true);

						// notes, in avg file, what parameter combo led to the
						// results
						avgWriter.write("\n" + "#P = 10 "  + " #S = 3 " + " e = 0.75 " + " o = " + omega + " p1 = " + phi1 + " p2 = " + phi2);
						avgWriter.write("\n" + averageFitnessAcrossRuns);
					} catch (IOException f) {
						f.printStackTrace();
					}

				}
			}
		}
	}

}
