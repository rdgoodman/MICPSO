package applicationProblems;

import java.util.ArrayList;

import MN.Edge;
import MN.Sample;

public class GraphColoringConstraint implements Constraint {
	
	public GraphColoringConstraint(){
		// TODO
	}

	@Override
	public boolean satisfiesConstraint(Sample s, ArrayList<Edge> edgesArray) {
		for (Edge e: edgesArray){
			// invalid if any neighboring nodes have the same color
			if (s.getTable().get(e.getEndpoints().getFirst()) == s.getTable().get(e.getEndpoints().getLast())){
				//System.out.println(e.getEndpoints().getFirst().getName() + " has the same color as " + e.getEndpoints().getLast().getName());
				return false;
			}
		}
		return true;
	}
	


}
