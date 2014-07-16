package St_andrewssassySimulation.st_andrewssassy.network;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.SimpleGraph;

public class CommunicateGraph extends SimpleGraph<Vertex, Edge>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7494457115378005375L;

	public CommunicateGraph(EdgeFactory<Vertex, Edge> ef) {
		super(ef);
	}

}
