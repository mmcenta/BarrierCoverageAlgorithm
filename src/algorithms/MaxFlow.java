package algorithms;

import java.util.LinkedList;

import models.Node;
import models.FlowNetwork;
import models.Graph;

public class MaxFlow {
	private FlowNetwork nw;
	private FlowNetwork residual;
	private int V;

	float maxFlowValue;
	float[][] maxFlow;
	
	public MaxFlow() {
		this.V = 0;
		this.maxFlowValue = Float.NEGATIVE_INFINITY;
	}
	
	public MaxFlow(FlowNetwork network) {
		this.nw = network;
		this.V = network.graph.nNodes;
		this.maxFlow = new float[V][V];
		
		this.maxFlowValue = Float.NEGATIVE_INFINITY;
	}
	
	public void setFlowNetwork(FlowNetwork newNetwork) {
		nw = newNetwork;
		
		if(V != newNetwork.graph.nNodes) {
			V = newNetwork.graph.nNodes;
			maxFlow = new float[V][V];
		}
	}
	
	private void initializeFlow() {
		// Initialize the flow on edges on the network to 0
		// flow[u][v] if the edge from node u to node v doesn't exist on the original network
		for(int i=0; i < V; i++)
			for(int j=0; j < V; j++)
				maxFlow[i][j] = Float.NEGATIVE_INFINITY;
		
		for(int from=0; from < V; from++) {
			Node fromNode = nw.getNode(from);
			for(int to: fromNode.out)
				maxFlow[from][to] = 0;
		}
	}
	
	private void initializeResidualNetwork() {
		// This function initializes the residual network
		// The matrix flow must be initialized before running this method
		residual = new FlowNetwork(new Graph(V));
		
		for(int from=0; from<V; from++) {
			Node fromNode = nw.getNode(from); // get the node from the original graph
			
			for(int to : fromNode.out) {
				// Create the forward edge on the residual graph
				residual.addEdge(from, to, nw.getCapacity(from, to));
				
				// Create the backwards edge if it doens't exist on the original graph
				if(maxFlow[to][from] == Float.NEGATIVE_INFINITY && from != nw.source && to != nw.sink)
					// flow[i][j] is - /infty if the edge from i to j doesn't exist on the original graph
					residual.addEdge(to, from, 0);
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
			int curr = queue.pop();
			Node currNode = residual.getNode(curr);

			for(int adj: currNode.out)
				if(!visited[adj] && residual.getCapacity(curr, adj) != 0) {
					queue.push(adj);
					paths[adj] = curr;
					visited[adj] = true;
				}
		}
		
		return visited[residual.sink];
	}
	
	private float augmentPath(int[] paths) {
		float bottleneck = Integer.MAX_VALUE; // stores the minimum residual capacity along the path
		
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
			if(maxFlow[prev][curr] == Float.NEGATIVE_INFINITY) 
				// If this is a backwards edge
				maxFlow[curr][prev] -= bottleneck;
			else
				// If this is a forwards edge
				maxFlow[prev][curr] += bottleneck;
			
			// Update the residual graph
			residual.updateCapacity(prev, curr, -bottleneck);
			residual.updateCapacity(curr, prev, bottleneck);

			curr = prev; 
		}
		
		return bottleneck;
	}
	
	public void solveMaxFlow() {
		// Shortest Augmenting Path implementation of the Ford-Fulkerson Algorithm
		// Also called the Dinitz-Edmonds-Karp Algorithm
		int[] paths = new int[V]; // stores the search paths of the last BFS
		maxFlowValue = 0;
		
		initializeFlow();
		initializeResidualNetwork();
		
		// While there is a path in the residual graph between the source and the sink,
		// pick the shortest and augment the path.
		while(shortestPath(paths))
			maxFlowValue += augmentPath(paths);
	}
	
	public float getMaxFlowValue() {
		if(maxFlowValue == Float.NEGATIVE_INFINITY)
			solveMaxFlow();
		
		return maxFlowValue;
	}
	
	public float[][] getMaxFlow() {
		if(maxFlowValue == Float.NEGATIVE_INFINITY)
			solveMaxFlow();
		
		return maxFlow;
	}
}
