package edu.msu.MICPSO;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import MN.Edge;
import MN.MarkovNetwork;
import MN.Node;

public class RunModels {

	public static void main(String[] args) throws FileNotFoundException {	
		// creates a Markov network (read in from file)
        MarkovNetwork MN = new MarkovNetwork();
        // samples the above network using Gibbs sampling
        MN.sample();

    }
}
