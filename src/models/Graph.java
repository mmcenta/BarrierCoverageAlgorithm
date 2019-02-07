package models;

import java.util.List;

public class Graph {
	// A class that represents a directed graph with adjacency lists
	// Nodes are stored in an ArrayList and referenced by their number on the array
	public final int n; // number of nodes in the graph
	public int m;       // number of edges in the graph
	Node[] nodes;       // all the nodes in the graph
	
	public Graph(int numberNodes) {
		this.n = numberNodes;
		this.m = 0;
		this.nodes = new Node[numberNodes];
		
		for(int k=0; k < n; k++)
			nodes[k] = new Node(k);
	}
	
	public void addEdge(int from, int to) {
		// Adds a edge to this graph
		nodes[from].addOutbound(to);;
		m++;
	}
	
	public void addEdge(Edge e) {
		nodes[e.from].out.add(e);
		m++;
	}
	
	public Node getNode(int node) {
		return nodes[node];
	}
	
	public List<Edge> edgesOut(int node) {
		return nodes[node].out;
	}
}
