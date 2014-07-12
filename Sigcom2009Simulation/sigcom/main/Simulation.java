package sigcom.main;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;

import sigcom.network.Common;
import sigcom.network.CommunicateGraph;
import sigcom.network.Edge;
import sigcom.network.Vertex;


public class Simulation {

	public static void comparation() {
		EdgeFactory<Vertex, Edge> ef = 
				new ClassBasedEdgeFactory<Vertex, Edge>(null);		
		CommunicateGraph socialG = new CommunicateGraph(ef);	
		
		socialG = Common.createInitialGraph(socialG);
		
	}

}
