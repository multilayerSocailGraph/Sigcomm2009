package st_andrewssassy.network;

import java.util.ArrayList;

import sigcom.datamining.Initial_Phase;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Common {
	
	public static CommunicateGraph createInitialGraph(CommunicateGraph socialG) {
						
		Initial_Phase initial = new Initial_Phase();
		ArrayList<Instances> datasets = initial.getdata();
		for(Instance i : datasets.get(0)){
			Attribute a = i.attribute(0);
			int a_val = Integer.parseInt(a.value(0));
			Vertex newVertex = new Vertex(a_val);
			if(!socialG.containsVertex(newVertex)){
				socialG.addVertex(newVertex);
			}			
			
		}
		
		return null;		
	}

	

}
