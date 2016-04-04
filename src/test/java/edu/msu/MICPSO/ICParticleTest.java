package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import PSO.GCFitnessFunction;
import PSO.ICParticle;

public class ICParticleTest {

	@Test
	public void testInitialization() throws FileNotFoundException {		
		
		String filename = ("src/main/resources/markovNet.txt");
		ICParticle p = new ICParticle(filename, new GCFitnessFunction(3), 5, .005);
		p.print();
	}

}
