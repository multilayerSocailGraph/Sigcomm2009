package simulation_1.sigcom.main;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;

import simulation_1.sigcom.network.Common;
import simulation_1.sigcom.network.CommunicateGraph;
import simulation_1.sigcom.network.Edge;
import simulation_1.sigcom.network.Vertex;


public class Simulation {

	public static void comparation() {
		EdgeFactory<Vertex, Edge> ef = 
				new ClassBasedEdgeFactory<Vertex, Edge>(null);		
		CommunicateGraph socialG = new CommunicateGraph(ef);	
		
		socialG = Common.createInitialGraph(socialG);
		
	}

}
