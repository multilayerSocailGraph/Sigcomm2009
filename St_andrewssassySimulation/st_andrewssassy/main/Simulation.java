package St_andrewssassySimulation.st_andrewssassy.main;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;

import St_andrewssassySimulation.st_andrewssassy.network.Common;
import St_andrewssassySimulation.st_andrewssassy.network.CommunicateGraph;
import St_andrewssassySimulation.st_andrewssassy.network.Edge;
import St_andrewssassySimulation.st_andrewssassy.network.Vertex;


public class Simulation {

	public static void comparation() {
		EdgeFactory<Vertex, Edge> ef = 
				new ClassBasedEdgeFactory<Vertex, Edge>(null);		
		CommunicateGraph socialG = new CommunicateGraph(ef);	
		
		socialG = Common.createInitialGraph(socialG);
		
	}

}
