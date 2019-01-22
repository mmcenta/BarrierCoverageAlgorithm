package models;

import java.util.ArrayList;

public class Graph {
	// A class that represents a directed graph with adjacency lists
	// Nodes are stored in an ArrayList and referenced by their number on the array
	public int nNodes, nEdges;
	ArrayList<Node> nodes;
	
	public Graph(int numberNodes) {
		this.nNodes = numberNodes;
		this.nEdges = 0;
		this.nodes = new ArrayList<Node>();
		
		for(int k=0; k < this.nNodes; k++)
			nodes.add(new Node(k));
	}
	
	public void addEdge(int from, int to) {
		// Adds a edge to this graph
		Node fromNode = nodes.get(from);
		Edge newEdge = new Edge(from, to);
		
		fromNode.out.add(newEdge);
		nEdges++;
	}
	
	public void addUndirectedEdge(int node1, int node2) {
		addEdge(node1, node2);
		addEdge(node2, node1);
	}
	
	public Node getNode(int node) {
		return nodes.get(node);
	}
}
