package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import PSO.GCFitnessFunction;
import PSO.MNParticle;

public class MNParticleTest {

	@Test
	public void test() throws FileNotFoundException {		
		String filename = ("src/main/resources/graphColor05Node.txt");
		MNParticle p = new MNParticle(filename, null, 5, .9); 
		p.updatePosition();
		System.out.println("\n POSITION UPDATE \n");
		p.print();
		System.out.println("\n FITNESS EVAL \n");
		p.calcFitness();

	}
	
	

}
