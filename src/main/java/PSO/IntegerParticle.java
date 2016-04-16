package PSO;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import MN.Edge;
import MN.Node;
import MN.Sample;

public class IntegerParticle {

	private int[] position; // build sample out of position for fitness eval
	private double[] velocity;
	private Node[] nodes;
	private ArrayList<Edge> edgesArray;
	private FitnessFunction f;
	
	private int[] pBest;
	private double pBest_fitness;
	
	
	public IntegerParticle(String fileName, FitnessFunction f) throws FileNotFoundException {
		this.f = f;
		
		// TODO: fill out global set of Nodes that can hold values for building samples
		// TODO: read in edges to get the dependencies between nodes for constraint-handling business
		
		initializeVelocityAndPosition();
	}
	
	/**
	 * Updates particle velocity
	 * @param omega
	 * @param phi1
	 * @param phi2
	 * @param gBest
	 */
	public void updateVelocity(double omega, double phi1, double phi2, int[] gBest) {
		// decide on multipliers
		double cognitive = Math.random() * phi1;
		double social = Math.random() * phi2;
		
		for (int i = 0; i < velocity.length; i++){
			velocity[i] = (omega * velocity[i]) + (cognitive * pBest[i]) + (social * gBest[i]);
		}
	}

	
	/**
	 * Randomly initializes velocity and position vectors
	 */
	private void initializeVelocityAndPosition(){
		for (int i = 0; i < velocity.length; i++){
			position[i] = (int)(Math.random()*10);
			velocity[i] = Math.random();
		}
	}
	
	/**
	 * Updates particle position
	 */
	public void updatePosition(){
		for (int i = 0; i < position.length; i++){
			double move = position[i] + velocity[i];
			// snap to nearest integer
			if (move - Math.floor(move) > 0.5){
				position[i] = (int)Math.ceil(move);
			} else {
				position[i] = (int)Math.floor(move);
			}
		}
	}
	
	/**
	 * Builds a sample using the current particle position
	 * @return
	 */
	public Sample buildSampleFromPosition(){
		Sample s = new Sample();
		
		for (int i = 0; i < nodes.length; i++){
			s.setSampledValue(nodes[i], position[i]);
		}
		
		return s;
	}
	
	
	/**
	 * Calculates particle's fitness
	 * @return
	 */
	public double calcFitness(){
		// build a sample
		Sample s = buildSampleFromPosition();
		
		double fit = f.calcFitness(s);
		
		// check constraints
		if (!checkConstraints(s)){
			// penalize if doesn't pass constraints
			fit -= 100;
			s.setFitness(fit);
		} 
		
		// set pBest
		// TODO: refactor, this only works for a max problem
		if (fit > pBest_fitness || pBest == null){
			pBest_fitness = fit;
			pBest = new int[position.length];
			// copy this position
			for (int i = 0; i < pBest.length; i++){
				pBest[i] = position[i];
			}
		}
		
		return fit;
	}
	
	/**
	 * TODO: move this into the fitness function class or something
	 * Returns TRUE if all constraints are passed, FALSE otherwise
	 * @return
	 */
	public boolean checkConstraints(Sample s){
		// only graph coloring constraints, for now
		for (Edge e: edgesArray){
			// invalid if any neighboring nodes have the same color
			if (s.getTable().get(e.getEndpoints().getFirst()) == s.getTable().get(e.getEndpoints().getLast())){
				return false;
			}
		}		
		return true;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}
	
	
	
}
