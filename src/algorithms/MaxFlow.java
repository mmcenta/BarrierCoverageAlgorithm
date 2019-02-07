package algorithms;

import java.util.HashMap;
import java.util.LinkedList;

import models.Edge;
import models.Flow;
import models.FlowNetwork;

public class MaxFlow {
	// This class has static methods to solve the Max Flow Problem though a flow network with a single source and a single sink
	// The edges must have edge capacities and may or may not have edge demands
	private static FlowNetwork initializeResidualNetwork(FlowNetwork network ,HashMap<Edge, Double> flowMap) {
		// This function initializes the residual network
		// Note: the flow must be initialized to a feasible flow for this function to return a correct residual network
		int n = network.n;
		FlowNetwork residual = new FlowNetwork(n);
		
		// For each node on the original network
		for(int from=0; from < n; from++) {
			// Iterate over outbound edges 
			for(Edge e : network.edgesOut(from)) {
				// Create the forward edge on the residual graph
				residual.addEdge(e, network.getCapacity(e) - flowMap.get(e));
				
				Edge backwards = new Edge(e.to, e.from);
				// Create the backwards edge if it doens't exist already on the original graph
				// Note: flow contains all edges of the original graph and only them, use this for a O(1) check
				if(!flowMap.containsKey(backwards) && e.from != network.source && e.to != network.sink)
					residual.addEdge(backwards, flowMap.get(e)-network.getDemand(e));
			}
		}
		
		return residual;
	}
	
	private static boolean shortestPath(int[] paths, FlowNetwork residual) {
		// Searches for the shortest path from the source to the sink in BFS and stores the search tree, from which
		// the shortest path can be recovered.
		// Returns: true if a path was found, false if not.
		int n = residual.n;
		boolean[] visited = new boolean[n];  // visited[i] is true iff the node was already seen during the search 
		LinkedList<Integer> queue = new LinkedList<Integer>();  // the queue (FIFO) used by the BFS algorithm
		
		// Initialize the BFS from the source
		queue.addLast(residual.source);
		paths[residual.source] = residual.source;
		visited[residual.source] = true;
		
		// Search for the sink remembering the paths taken
		while(!queue.isEmpty() && !visited[residual.sink]) {
			int curr = queue.removeFirst(); // dequeue an node

			// Iterate over outbound edges
			for(Edge e: residual.edgesOut(curr))
				// If the adjacent node wasn't visited and edge has capacity
				if(!visited[e.to] && residual.getCapacity(e) > 0) {
					queue.addLast(e.to);  // enqueue the adjacent node
					paths[e.to] = curr;   // store curr as the parent of the adjacent node on the search tree
					visited[e.to] = true; // mark the adjacent node as visited
				}
		}
		
		return visited[residual.sink];
	}
	
	private static void augmentPath(int[] paths, Flow flow, FlowNetwork residual) {
		HashMap<Edge, Double> flowMap = flow.getMap();
		double bottleneck = Double.MAX_VALUE; // stores the minimum residual capacity along the path
		
		// Calculate the bottleneck of the path (minimum residual capacity)
		int curr = residual.sink;
		while(paths[curr] != curr) {
			int prev =  paths[curr];
			Edge e = new Edge(prev, curr);
			
			if(bottleneck > residual.getCapacity(e))
				bottleneck = residual.getCapacity(e);
			curr = prev; 
		}
		
		// Traverse the path again augmenting the flow
		curr = residual.sink;
		while(paths[curr] != curr) {
			int prev = paths[curr];
			Edge forward = new Edge(prev, curr);
			Edge backward = new Edge(curr, prev);

			// Update the flow
			// Note: again, use flow to check wheter an edge exists on the original graph
			if(!flowMap.containsKey(forward))
				// If this is a backward edge
				flowMap.put(backward, flowMap.get(backward)-bottleneck);
			else
				// If this is a forward edge
				flowMap.put(forward, flowMap.get(forward)+bottleneck);
			
			// Update the residual graph
			residual.setCapacity(forward, residual.getCapacity(forward)-bottleneck);
			residual.setCapacity(backward, residual.getCapacity(backward)+bottleneck);

			curr = prev; 
		}
		
		// Update flow value
		flow.setValue(bottleneck+flow.getValue());
	}
	
	public static Flow getFlow(FlowNetwork network) {
		// Shortest Augmenting Path implementation of the Ford-Fulkerson Algorithm
		// Also called the Dinitz-Edmonds-Karp Algorithm
		int n = network.n;
		Flow flow;
		
		// Initialize the flow to a feasible flow	
		flow = FeasibleFlow.getFeasibleFlow(network);
		
		// Given the initial flow, create the residual network
		FlowNetwork residual = initializeResidualNetwork(network, flow.getMap());
		
		// Initialize the path tree
		int[] paths = new int[n]; // stores the search paths of the last BFS
		
		// While there is a path in the residual graph between the source and the sink,
		// pick the shortest and augment the path.
		while(shortestPath(paths, residual))
			augmentPath(paths, flow, residual);
		
		return flow;
	}
}
