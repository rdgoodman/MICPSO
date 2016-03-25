package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import PSO.FitnessFunction;
import PSO.GCFitnessFunction;
import PSO.ICParticle;

public class ICParticleTest {

	@Test
	public void testInitialization() throws FileNotFoundException {		
		
		String filename = ("src/main/resources/markovNet.txt");
		// TODO; this fitness function stuff is probably all kinds of broken
		ICParticle p = new ICParticle(filename, (FitnessFunction) new GCFitnessFunction());
		p.print();
	}

}
