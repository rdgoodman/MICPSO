package edu.msu.MICPSO;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import org.junit.Test;
import MN.CliqueMarkovNetwork;

public class CliqueMNTest {

	@Test
	public void test() throws FileNotFoundException {
		String filename = "src/main/resources/maxSatTestFile2.txt";
		CliqueMarkovNetwork net = new CliqueMarkovNetwork(filename);
		
		net.bronKerbosch();
	}

}
