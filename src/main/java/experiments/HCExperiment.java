package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import comparisons.Hillclimb;

public class HCExperiment {

	public HCExperiment(double numRuns, ArrayList<String> experimentGraphs) throws FileNotFoundException {

		for (String graphName : experimentGraphs) {
			
			ArrayList<ArrayList<Double>> fitnessesPerIteration = new ArrayList<ArrayList<Double>>();
			
			// get graph name
			String graph = graphName.split("/")[graphName.split("/").length -1];

			// TODO: make a specialized MN folder
			String totalResults = "src/main/resources/PaperExperiments/HC/rawResults_" + graph;
			String fitnessEvals = "src/main/resources/PaperExperiments/HC/fitEvals_" + graph;
			String fitnessOverIterations = "src/main/resources/PaperExperiments/HC/fitnesses_" + graph;
			
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
				Hillclimb h = new Hillclimb(graphName);
				double fitness = h.run().getFitness();
				double fitEvals = h.getNumFitnessEvals();

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

				// add datum to average fitness over iterations
				fitnessesPerIteration.add(h.getFitnesses());
 			    System.out.println("Size: " + h.getFitnesses().size());
				System.out.println("Size it should be: " + h.getNumFitnessEvals());
				
			}
			
			// find longest ArrayList
			int longest = 0;
			for (int i = 0; i < fitnessesPerIteration.size(); i++){
				if (fitnessesPerIteration.get(i).size() > longest){
					longest = fitnessesPerIteration.get(i).size();
				}
			}
			
			// add previous number when arrays are uneven lengths
			for (int i = 0; i < fitnessesPerIteration.size(); i++){
				int numToAdd = longest - fitnessesPerIteration.get(i).size();
				for (int j = 0; j < numToAdd; j++){
					fitnessesPerIteration.get(i).add(fitnessesPerIteration.get(i).get(fitnessesPerIteration.get(i).size()-1));
				}
			}

			// add all 
			ArrayList<Double> average = new ArrayList<Double>();
			for (int i = 0; i < longest; i++){
				double e = 0;
				for (int j = 0; j < numRuns; j++){
					// get ith element of jth array and sum
					e += fitnessesPerIteration.get(j).get(i);
				}
				average.add(e);
			}		
			
			// divide to get average fitness evaluations at each iteration
			for (int i = 0; i < average.size(); i++){
				average.set(i, average.get(i)/numRuns);
			}
			

			// write average fitness evaluations at each iteration to file
			try {
				fitnessWriter = new FileWriter(fitnessCurve, true);

				for (double d : average) {
					fitnessWriter.write("\n" + d);
				}

				fitnessWriter.flush();
			} catch (IOException f) {
				f.printStackTrace();
			}

		}
	}
}
