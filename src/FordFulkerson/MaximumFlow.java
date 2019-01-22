package FordFulkerson;

import java.util.LinkedList;

import models.Node;
import models.Edge;
import models.Graph;

public class MaximumFlow {
	FlowNetwork nw;
	FlowNetwork residual;
	int V;
	int[][] flow;
	
	public MaximumFlow(FlowNetwork network) {
		this.nw = network;
		this.V = network.graph.nNodes;
		this.flow = new int[V][V];
	}
	
	private void initializeFlow() {
		// Initialize the flow on edges on the network to 0
		// flow[u][v] if the edge from node u to node v doesn't exist on the original network
		for(int i=0; i < V; i++)
			for(int j=0; j < V; j++)
				flow[i][j] = -1;
		
		for(int k=0; k < V; k++) {
			Node curr = nw.getNode(k);
			for(Edge e: curr.out)
				flow[e.from][e.to] = 0;
		}
	}
	
	private void initializeResidualNetwork() {
		// This function initializes the residual network
		// The matrix flow must be initialized before running this method
		residual = new FlowNetwork(new Graph(V));
		
		for(int k=0; k < V; k++) {
			Node curr = nw.getNode(k); // get the node from the original graph
			
			for(Edge e : curr.out) {
				// Create the forward edge on the residual graph
				residual.addEdge(e.from, e.to);
				residual.setCapacity(e.from, e.to, nw.getCapacity(e.from, e.to));
				
				// Create the backwards edge if it doens't exist on the original graph
				if(flow[e.to][e.from] == -1) {
					// flow[i][j] is -1 if the edge from i to j doesn't exist on the original graph
					residual.addEdge(e.to, e.from);
					residual.setCapacity(e.to, e.from, 0);
				}
			}
		}
	}
	
	private boolean shortestPath(int[] paths) {
		// paths[i] stores the parent of the i-th node on this search tree
		boolean[] visited = new boolean[V];  // visited[i] is true iff the node was already seen during the search 
		
		// Initialize the BFS from the source
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.push(residual.source);
		paths[residual.source] = residual.source;
		visited[residual.source] = true;
		
		// Search for the sink remembering the paths taken
		while(!queue.isEmpty() && !visited[residual.sink]) {
			Node curr = residual.getNode(queue.pop());

			for(Edge e: curr.out)
				if(!visited[e.to] && residual.getCapacity(e.from, e.to) != 0) {
					queue.push(e.to);
					paths[e.to] = e.from;
					visited[e.to] = true;
				}
		}
		
		return visited[residual.sink];
	}
	
	private int augmentPath(int[] paths) {
		int bottleneck = Integer.MAX_VALUE; // stores the minimum residual capacity along the path
		
		// Calculate the bottleneck of the path (minimum residual capacity)
		int curr = residual.sink;
		while(paths[curr] != curr) {
			int prev =  paths[curr];
			if(bottleneck > residual.getCapacity(prev, curr))
				bottleneck = residual.getCapacity(prev, curr);
			curr = prev; 
		}
		
		// Traverse the path again augmenting the flow
		curr = residual.sink;
		while(paths[curr] != curr) {
			int prev =  paths[curr];
			if(flow[prev][curr] == -1) 
				// If this is a backwards edge
				flow[curr][prev] -= bottleneck;
			else
				// If this is a forwards edge
				flow[prev][curr] += bottleneck;
			
			// Update the residual graph
			residual.updateCapacity(prev, curr, -bottleneck);
			residual.updateCapacity(curr, prev, bottleneck);

			curr = prev; 
		}
		
		return bottleneck;
	}
	
	public int maximumFlowSAP() {
		// Shortest Augmenting Path implementation of the Ford-Fulkerson Algorithm
		// Also called the Dinitz-Edmonds-Karp Algorithm
		int[] paths = new int[V]; // stores the search paths of the last BFS
		int max_flow = 0;
		
		initializeFlow();
		initializeResidualNetwork();
		
		// While there is a path in the residual graph between the source and the sink,
		// pick the shortest and augment the path.
		while(shortestPath(paths))
			max_flow += augmentPath(paths);
		
		return max_flow;
	}
	
	public int[][] getFlow() {
		return flow;
	}
}
