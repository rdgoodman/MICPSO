package MN;

import java.util.ArrayList;

public class ProbDist {
	
	ProbDistEntry[] probs;
	
	public ProbDist(double[] possibleVals){
		probs = new ProbDistEntry[possibleVals.length];
		
		// create an empty ( == 1) entry for every possible value
		for (int i = 0; i < probs.length; i++){
			probs[i] = new ProbDistEntry(possibleVals[i]);
		}
	}
	
	
	/**
	 * Returns the probability associated with a certain value
	 * @param value
	 * @return
	 */
	public double getProb(double value){
		double p = 0;
		
		for (int i = 0; i < probs.length; i++){
			if (probs[i].getValue() == value){
				p = probs[i].getProb();
			}
		}		
		return p;
	}
	
	/**
	 * Adjusts the probability for a certain value
	 */
	public void setProb(double value, double newprob){
		for (int i = 0; i < probs.length; i++){
			if (probs[i].getValue() == value){
				probs[i].setProb(newprob);
			}
		}
	}
	
	

}
