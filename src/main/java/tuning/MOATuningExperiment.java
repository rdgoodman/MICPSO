package tuning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import comparisons.MOA;

public class MOATuningExperiment {
	
	String filePath1;
	String filePath2;
	String filePath3;


	public MOATuningExperiment(String file1, String file2, String file3, ArrayList<Double> coolingRates, ArrayList<Integer> popSizes, ArrayList<Double> percentagesToSelect) throws FileNotFoundException{
		filePath1 = file1;
		filePath2 = file2;
		filePath3 = file3;
		
		tuneAll(coolingRates, popSizes, percentagesToSelect);
	}

	/**
	 * Carries out and reports results of all tuning (unlike other algorithms, only one method for this)
	 * @param coolingRates
	 * @param popSizes
	 * @param percentagesToSelect
	 * @throws FileNotFoundException 
	 */
	private void tuneAll(ArrayList<Double> coolingRates, ArrayList<Integer> popSizes,
			ArrayList<Double> percentagesToSelect) throws FileNotFoundException {
		
		// reports results of combination of params (average)
		String averageFilePath = "src/main/resources/TuningExperiments/MOA_Averages.txt";	
		
		File avgOutput = new File(averageFilePath);
		if (!avgOutput.exists()) {
			try {
				avgOutput.createNewFile();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Double cr : coolingRates){
			for (Integer pop : popSizes){
				for (Double perc : percentagesToSelect){
					FileWriter avgWriter = null;

					double averageFitnessAcrossRuns = 0;
					// run each 10 times
					for (int i = 0; i < 5; i++) {
						// creates a MOA instance for each file
						MOA m1 = new MOA(filePath1, cr, pop, perc);
						MOA m2 = new MOA(filePath2, cr, pop, perc);
						MOA m3 = new MOA(filePath3, cr, pop, perc);
						
						double fit1 = m1.run().getFitness();
						System.out.println("moa1 ran");
						double fit2 = m2.run().getFitness();
						System.out.println("moa2 ran");
						double fit3 = m3.run().getFitness();
						System.out.println("moa3 ran");

						
						double avgFit = (fit1 + fit2 + fit3)/3;
						averageFitnessAcrossRuns += avgFit;
					}
					
					averageFitnessAcrossRuns /= 5;
					
					try {
						avgWriter = new FileWriter(avgOutput, true);

						// notes, in avg file, what parameter combo led to the
						// results
						avgWriter.write("\n" + "CR = " + cr + " Pop = " + pop + " % = " + perc);
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

}
