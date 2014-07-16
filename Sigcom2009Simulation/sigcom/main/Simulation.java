package Sigcom2009Simulation.sigcom.main;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;

import Sigcom2009Simulation.sigcom.network.Common;
import Sigcom2009Simulation.sigcom.network.CommunicateGraph;
import Sigcom2009Simulation.sigcom.network.Edge;
import Sigcom2009Simulation.sigcom.network.Vertex;


public class Simulation {

	public static void comparation() {
		EdgeFactory<Vertex, Edge> ef = 
				new ClassBasedEdgeFactory<Vertex, Edge>(null);		
		CommunicateGraph socialG = new CommunicateGraph(ef);	
		
		socialG = Common.createInitialGraph(socialG);
		
	}

}
