package applicationProblems;

import java.util.ArrayList;

import MN.Edge;
import MN.Sample;


public interface Constraint {

	public boolean satisfiesConstraint(Sample s, ArrayList<Edge> edgesArray);
	
}
