package models;

import java.util.HashMap;
import java.util.List;

public class FlowNetwork {
	//	A class that represents a flow network
	public final int n;            // the number of vertices
	public int m;                  // the number of edges 
	public Graph graph;  	 	   // the graph representing the structure of the network
	public final int source, sink; // the nodes that represent the source and the sink  
	public HashMap<Edge, Double> capacities;  // a hash map that stores edge capacities
	public HashMap<Edge, Double> demands;     // a hash map that stores edge demands
	private boolean hasDemands;                // boolean that stores wheter this flow network has edge demands
	
	public FlowNetwork(Graph directedGraph) {
		this.n = directedGraph.n;
		this.m = directedGraph.m;
		this.graph = directedGraph;
		this.capacities = new HashMap<Edge, Double>();
		this.demands = new HashMap<Edge, Double>();
		this.hasDemands = false;

		// For simplicity, the source is the first node and the sink is the last node 
		this.source = 0;
		this.sink = n-1;
	}
	
	public FlowNetwork(int numberNodes) {
		this.n = numberNodes;
		this.m = 0;
		this.graph = new Graph(numberNodes);
		this.capacities = new HashMap<Edge, Double>();
		this.demands = new HashMap<Edge, Double>();
		this.hasDemands = false;
		
		// For simplicity, the source is the first node and the sink is the last node 
		this.source = 0;
		this.sink = n-1;
	}
	
	public double getCapacity(Edge e) {
		if(!capacities.containsKey(e))
			return 0;		
		return capacities.get(e);
	}

	public void setCapacity(Edge e, double capacity) {
		capacities.put(e, capacity);
	}
	
	public double getDemand(Edge e) {
		if(!demands.containsKey(e))
			return 0;	
		return demands.get(e);
	}
	
	public void setDemand(Edge e, double demand) {
		if(demand > 0 && !hasDemands)
			hasDemands = true;
		demands.put(e, demand);
	}
			
	public boolean hasDemands() {
		return hasDemands;
	}
	
	public void addEdge(Edge e, double capacity) {
		graph.addEdge(e);
		capacities.put(e, capacity);
		m++;
	}
	
	public Node getNode(int node) {
		// Wraps the method getNode from the graph
		return graph.getNode(node);
	}
	
	public List<Edge> edgesOut(int node) {
		return graph.edgesOut(node);
	}
}
