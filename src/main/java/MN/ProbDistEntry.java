package MN;

public class ProbDistEntry {
	
	private double value;
	private double prob;
	
	public ProbDistEntry(double value){
		this.value = value;
		this.prob = 1; // because everything will be multiplied by it
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}
	
	public ProbDistEntry copy(){
		ProbDistEntry cp = new ProbDistEntry(value);
		cp.setProb(prob);
		return cp;
	}

}
