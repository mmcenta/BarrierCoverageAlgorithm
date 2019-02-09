package algorithms;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import models.Edge;
import models.Flow;
import models.FlowNetwork;

public class AllFlowPaths {
	private static void dfsPaths(int node, int target, boolean[] visited, int[] parent,
						         FlowNetwork network,  HashMap<Edge, Double> flowMap,  Collection<List<Edge>> paths) {
		visited[node] = true;

		// Check if the target was reached
		if(node == target) {
			// Build the list representing the path that was used
			LinkedList<Edge> path = new LinkedList<Edge>();

			int curr = target;
			while(parent[curr] != curr) {
				path.addFirst(new Edge(parent[curr], curr));
				curr = parent[curr];
			}
	
			//Add this path to the list of found paths
			paths.add(path);
		}
		// If the target was not reached, continue the depth first search
		else {
			for(Edge e: network.edgesOut(node))
				if(!visited[e.to] && flowMap.containsKey(e) && flowMap.get(e) > 0) {
					parent[e.to] = node;
					dfsPaths(e.to, target, visited, parent, network, flowMap, paths);
				}
		}
		visited[node] = false;
	}
	
	public static Collection<List<Edge>> getAllFlowPaths(FlowNetwork network, Flow flow) {
		int n = network.n;
		HashMap<Edge, Double> flowMap = flow.getMap();
		Collection<List<Edge>> paths = new LinkedList<List<Edge>>();

		// Initialize auxiliary arrays
		boolean[] visited = new boolean[n]; // Already initialized to all false values
		int[] parent = new int[n];
		
		// Get all paths from the source to the sink
		parent[network.source] = network.source;
		dfsPaths(network.source, network.sink, visited, parent, network, flowMap, paths);

		return paths;
	}
}
