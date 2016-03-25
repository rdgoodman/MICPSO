package MN;

import java.util.ArrayList;

public class ProbDist {

	ProbDistEntry[] probs;
	Node N;

	public ProbDist(double[] possibleVals, Node N) {
		probs = new ProbDistEntry[possibleVals.length];
		this.N = N;

		// create an empty ( == 1) entry for every possible value
		for (int i = 0; i < probs.length; i++) {
			probs[i] = new ProbDistEntry(possibleVals[i]);
		}
	}

	/**
	 * Turns this into an actual-factual valid probability distribution. Wow!
	 */
	protected void normalize() {
		double denominator = 0;
		for (int i = 0; i < probs.length; i++) {
			denominator += probs[i].getProb();
		}

		for (int i = 0; i < probs.length; i++) {
			probs[i].setProb(probs[i].getProb() / denominator);
		}
	}

	/**
	 * Samples from this distribution, returns sampled value
	 */
	protected double sample() {
		double p = Math.random();

		// create upper and lower bounds
		double[] LB = new double[probs.length];
		double[] UB = new double[probs.length];
		double low = 0;

		for (int i = 0; i < probs.length; i++) {
			UB[i] = low + probs[i].getProb();
			low = UB[i];
			// TODO: check upper and lower bounds here
			// TODO: elsewhere, make sure we don't end up with negative values
			// or anything
		}

		for (int i = 0; i < probs.length; i++) {
			if (p >= LB[i] && p < UB[i]) {
				return probs[i].getValue();
			}
		}

		// TODO: throw exception
		return -1;
	}

	/**
	 * Returns the probability associated with a certain value
	 * 
	 * @param value
	 * @return
	 */
	public double getProb(double value) {
		double p = 0;

		for (int i = 0; i < probs.length; i++) {
			if (probs[i].getValue() == value) {
				p = probs[i].getProb();
			}
		}
		return p;
	}

	/**
	 * Adjusts the probability for a certain value
	 */
	public void setProb(double value, double newprob) {
		for (int i = 0; i < probs.length; i++) {
			if (probs[i].getValue() == value) {
				probs[i].setProb(newprob);
			}
		}
	}

	public void print() {
		for (int i = 0; i < probs.length; i++) {
			System.out.println("Pr(" + probs[i].getValue() + ") =" + probs[i].getProb());
		}
	}

}
