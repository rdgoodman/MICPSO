package edu.msu.MICPSO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import PSO.ICParticle;

public class ICParticleTest {

	@Test
	public void testInitialization() throws FileNotFoundException {
		String filename = ("src/main/resources/markovNet.txt");
		ICParticle p = new ICParticle(filename);
		p.print();
	}

}
