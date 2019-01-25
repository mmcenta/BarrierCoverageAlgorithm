package models;

public class Graph {
	// A class that represents a directed graph with adjacency lists
	// Nodes are stored in an ArrayList and referenced by their number on the array
	public int nNodes, nEdges;
	Node[] nodes;
	
	public Graph(int numberNodes) {
		this.nNodes = numberNodes;
		this.nEdges = 0;
		this.nodes = new Node[numberNodes];
		
		for(int k=0; k < this.nNodes; k++)
			nodes[k] = new Node(k);
	}
	
	public void addEdge(int from, int to) {
		// Adds a edge to this graph
		nodes[from].out.add(to);
		nEdges++;
	}
	
	public void addUndirectedEdge(int node1, int node2) {
		addEdge(node1, node2);
		addEdge(node2, node1);
	}
	
	public Node getNode(int node) {
		return nodes[node];
	}
}
