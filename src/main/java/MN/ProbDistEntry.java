package MN;

public class ProbDistEntry {
	
	private int value;
	private double prob;
	
	public ProbDistEntry(int value){
		this.value = value;
		this.prob = 1; // because everything will be multiplied by it
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
		if (this.prob > 1){
			this.prob = 1;
		} else if (this.prob < 0){
			this.prob = 0;
		}
	}
	
	public ProbDistEntry copy(){
		ProbDistEntry cp = new ProbDistEntry(value);
		cp.setProb(prob);
		return cp;
	}

}
