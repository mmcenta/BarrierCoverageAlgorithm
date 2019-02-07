package models;
import java.util.LinkedList;

public class Node {
	// A simple class representing a node.
	public int number;           // the number of the node on the array of nodes in the graph
	public LinkedList<Edge> out; // list containing the outbound edges
	
	public Node(int n) {
		this.number = n;
		this.out = new LinkedList<Edge>();
	}
	
	public void addOutbound(int adj) {
		out.add(new Edge(number, adj));
	}
}
