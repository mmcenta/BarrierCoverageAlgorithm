package FordFulkerson;

import models.Graph;
import models.Node;

public class FlowNetwork {
	//	A class that represents a flow network
	Graph graph;  // the graph representing the network
	public int source, sink;  // the node that represents the source and the sink  
	private int V;  // the number of vertices
	private int[][] capacities;  // capacities[i][j] stores the capacity from edge i to j
	
	public FlowNetwork(Graph directedGraph) {
		this.graph = directedGraph;
		this.V = directedGraph.nNodes;
		this.capacities = new int[V][V];
		
		// For simplicity, the source is the first node and the sink is the last node 
		this.source = 0;
		this.sink = this.graph.nNodes-1;
	}
	
	public FlowNetwork(int numberNodes) {
		this.graph = new Graph(numberNodes);
		this.V = numberNodes;
		this.capacities = new int[V][V];
		
		// For simplicity, the source is the first node and the sink is the last node 
		this.source = 0;
		this.sink = this.graph.nNodes-1;
	}
	
	public int getCapacity(int from, int to) {
		return capacities[from][to];
	}
	
	public void setCapacity(int from, int to, int capacity) {
		capacities[from][to] = capacity;
	}
	
	public void updateCapacity(int from, int to, int change) {
		capacities[from][to] += change;
	}
	
	public void addEdge(int from, int to) {
		// Wraps the method addEdge from the graph
		graph.addEdge(from, to);
	}
	
	public Node getNode(int node) {
		// Wraps the method getNode from the graph
		return graph.getNode(node);
	}
}
