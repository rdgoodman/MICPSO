package PSO;

import MN.Sample;

public interface Particle {
	
	public Sample sample();
	public double calcFitness();
	public void updateVelocity(double omega, double phi1, double phi2, Particle gBest); 
	public void updatePosition();
	public void setPBest(Sample s);
	public void adjustPBest();
	public Particle copy(); // returns a DEEP copy of this particle
	public void print();
	//public Object[] getProbs(); // this is kludgy, but seems to work
	public Sample getBestSample();

}
