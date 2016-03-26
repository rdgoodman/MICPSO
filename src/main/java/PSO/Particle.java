package PSO;

import MN.Sample;

public interface Particle {
	
	public Sample sample();
	public double calcFitness();
	public void updateVelocity(); // TODO: will need arguments
	public void updatePosition();
	public void setPBest();
	public void adjustPBest();
	public void bias(double epsilon);
	public Particle copy(); // TODO: will return a deep copy of this particle
	public void print();

}
