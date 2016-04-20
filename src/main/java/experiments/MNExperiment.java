package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import PSO.ICPSO;

public class MNExperiment {

	public MNExperiment(double numRuns, ArrayList<String> experimentGraphs) throws FileNotFoundException {

		for (String graphName : experimentGraphs) {
			
			// get graph name
			String graph = graphName.split("/")[graphName.split("/").length -1];

			// TODO: make a specialized MN folder
			String totalResults = "src/main/resources/OverallResults/MN_rawResults_" + graph;
			String fitnessEvals = "src/main/resources/OverallResults/MN_fitEvals_" + graph;
			String fitnessOverIterations = "src/main/resources/OverallResults/MN_fitnesses_" + graph;
			
			ArrayList<Double> averageFitnessOverIterations = new ArrayList<Double>();

			// create our files and writers
			File rawResults = new File(totalResults);
			if (!rawResults.exists()) {
				try {
					rawResults.createNewFile();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}

			FileWriter rawWriter = null;

			File evals = new File(fitnessEvals);
			if (!evals.exists()) {
				try {
					evals.createNewFile();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}

			FileWriter evalWriter = null;

			// TODO: this might have to move
			File fitnessCurve = new File(fitnessOverIterations);
			if (!fitnessCurve.exists()) {
				try {
					fitnessCurve.createNewFile();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}

			FileWriter fitnessWriter = null;

			//
			// runs a certain number of experiments on this graph
			//

			for (int i = 0; i < numRuns; i++) {
				// chosen params:
				// #P = 10 #S = 3 e = 0.75 o = 0.7 p1 = 1.4 p2 = 1.4
				// -0.3

				// runs on a single graph instance
				ICPSO m = new ICPSO(graphName, true, 10, 3, 0.75, 0.7, 1.4, 1.4);
				double fitness = m.run().getFitness();
				double fitEvals = m.getNumFitnessEvals();

				// write fitness results
				try {
					rawWriter = new FileWriter(rawResults, true);
					rawWriter.write("\n" + fitness);
					rawWriter.flush();
				} catch (IOException f) {
					f.printStackTrace();
				}

				// write number of fitness evaluation results
				try {
					evalWriter = new FileWriter(evals, true);
					evalWriter.write("\n" + fitEvals);
					evalWriter.flush();
				} catch (IOException f) {
					f.printStackTrace();
				}

				// update average fitness over iterations
				// TODO this is confusing
				ArrayList<Double> eachIterationFitness = m.getFitnesses();
				if (averageFitnessOverIterations.size() == 0) {
					averageFitnessOverIterations = eachIterationFitness;
				} else {
					
					System.out.println("Aggregate: " + averageFitnessOverIterations.size());
					System.out.println("This run: " + eachIterationFitness.size());
					
					double smallest = 0;
					if (averageFitnessOverIterations.size() < eachIterationFitness.size()) {
						smallest = averageFitnessOverIterations.size();
					} else {
						smallest = eachIterationFitness.size();
					}
					
					for (int j = 0; j < smallest; j++) {
						averageFitnessOverIterations.set(j,
								averageFitnessOverIterations.get(j) + eachIterationFitness.get(j));
					}
				}
			}

			// average fitness evaluations at each iteration
			for (int i = 0; i < averageFitnessOverIterations.size(); i++) {
				averageFitnessOverIterations.set(i, averageFitnessOverIterations.get(i) / numRuns);
			}

			// write average fitness evaluations at each iteration to file
			try {
				fitnessWriter = new FileWriter(fitnessCurve, true);

				for (double d : averageFitnessOverIterations) {
					fitnessWriter.write("\n" + d);
				}

				fitnessWriter.flush();
			} catch (IOException f) {
				f.printStackTrace();
			}

		}
	}

}
