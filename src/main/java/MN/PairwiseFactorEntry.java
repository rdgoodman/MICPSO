package MN;

public class PairwiseFactorEntry {
	
	private Node A;
	private Node B;
	private int valA;
	private int valB;
	private Double potential;
	
	/**
	 * Creates a single entry in a factor table between A and B
	 * @param A one variable of the factor
	 * @param B the other variable
	 * @param valA the value of A for this entry
	 * @param valB the value of B for this entry
	 */
	public PairwiseFactorEntry(Node A, Node B, int valA, int valB) {
		this.A = A;
		this.B = B;
		this.valA = valA;
		this.valB = valB;
		// TODO: might wanna rethink this random initialization, I guess
		this.potential = Math.random();
	}
	
	/**
	 * For use only with copy method
	 */
	public PairwiseFactorEntry(Node A, Node B){
		this.A = A;
		this.B = B;
	}
	
	
	/**
	 * Basically a copy constructor with same potentials/vals but different nodes
	 * @param A
	 * @param B
	 */
	public PairwiseFactorEntry copy(Node A, Node B){
		PairwiseFactorEntry fe = new PairwiseFactorEntry(A, B);
		fe.setValA(valA);
		fe.setValB(valB);
		fe.setPotential(potential);		
		return fe;
	}	

	public double getValue(Node n){
		if (n.equals(A)){
			return valA;
		} else if (n.equals(B)){
			return valB;
		}
		// TODO: throw exception
		return Double.MIN_VALUE;
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

	public int getValA() {
		return valA;
	}
	

	public void setValA(int valA) {
		this.valA = valA;
	}

	public int getValB() {
		return valB;
	}

	public void setValB(int valB) {
		this.valB = valB;
	}	

}
