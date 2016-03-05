package MN;

public class FactorEntry {
	
	private Node A;
	private Node B;
	private Double valA;
	private Double valB;
	private Double potential;
	
	/**
	 * Creates a single entry in a factor table between A and B
	 * @param A one variable of the factor
	 * @param B the other variable
	 * @param valA the value of A for this entry
	 * @param valB the value of B for this entry
	 */
	public FactorEntry(Node A, Node B, Double valA, Double valB) {
		this.A = A;
		this.B = B;
		this.valA = valA;
		this.valB = valB;
		// TODO: might wanna rethink this random initialization, I guess
		this.potential = Math.random();
	}

	public Double getPotential() {
		return potential;
	}

	public void setPotential(Double potential) {
		this.potential = potential;
	}

	public Node getA() {
		return A;
	}

	public void setA(Node a) {
		A = a;
	}

	public Node getB() {
		return B;
	}

	public void setB(Node b) {
		B = b;
	}

	public Double getValA() {
		return valA;
	}

	public void setValA(Double valA) {
		this.valA = valA;
	}

	public Double getValB() {
		return valB;
	}

	public void setValB(Double valB) {
		this.valB = valB;
	}
	
	

}
