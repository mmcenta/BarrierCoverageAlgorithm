package algorithms;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import models.Edge;
import models.FlowNetwork;
import models.Graph;

public class NodeDisjointPaths {
	private static FlowNetwork buildFlowNetwork(Graph g) {
		// Build flow nework by spliting intermediate nodes (not source r target) into
		// an "in-node" and an "out-node"
		// The capacity of the edge between the in-node and the out-node is 1
		// The capacity between other nodes should be high enough to never restrict
		// flow, so it is set to 1 as well
		int source = 0;
		int target = g.n - 1;
		int nSplit = g.n - 2;
		FlowNetwork network = new FlowNetwork(2*nSplit+2); // All nodes except the source and target are split
		
		// Add the edges parting from the source
		for (Edge e :g.edgesOut(source))
			network.addEdge(e, 1);

		// Separate each sensor node into two and link the adjacent nodes from the
		// out-node to the adjacent node's in-node
		for (int k = 1; k <= nSplit; k++) {
			// The out-node for the sensor k is the node k + nSplit
			network.addEdge(new Edge(k, k + nSplit), 1);

			for (Edge e : g.edgesOut(k)) {
				if (e.to != source && e.to != target)
					network.addEdge(new Edge(e.from + nSplit, e.to), 1);
				else if (e.to == target)
					network.addEdge(new Edge(k + nSplit, 2*nSplit + 1), 1);
			}
		}
		
		return network;
	}
	
	public static Collection<List<Integer>> getNodeDisjointPaths(Graph g) {	
		int source = 0;
		int target = g.n - 1;
		int n = g.n;
		FlowNetwork network = buildFlowNetwork(g);
		
		Collection<List<Edge>> nwPaths = AllFlowPaths.getAllFlowPaths(network, MaxFlow.getFlow(network));
		
		// Extract the node-disjoint sensor paths
		LinkedList<List<Integer>> graphPaths = new LinkedList<List<Integer>>();
		for (List<Edge> nwPath : nwPaths) {
			LinkedList<Integer> graphPath = new LinkedList<Integer>();

			graphPath.add(source); // the first node in the path is the source 
			for (Edge e : nwPath) {
				if (e.to >= 0 && e.to < n && e.to != source && e.to != target) 
					graphPath.add(e.to);
			}
			graphPath.add(target); // the last node in the path is the target
			
			graphPaths.add(graphPath); // add this path to the collection of paths
		}

		return graphPaths;
	}
}
