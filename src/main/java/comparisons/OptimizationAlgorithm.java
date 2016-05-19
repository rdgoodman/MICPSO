package comparisons;

import java.io.File;
import java.io.FileNotFoundException;

import MN.Sample;
import applicationProblems.ApplicationProblem;

public interface OptimizationAlgorithm {

	public Sample run();
	public ApplicationProblem constructProblemFromFile(File file) throws FileNotFoundException;
	
}
