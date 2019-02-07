package algorithms;

import java.util.HashMap;

import models.Edge;
import models.Flow;
import models.FlowNetwork;

public class FeasibleFlow {
	private static FlowNetwork reduceFlowNetwork(FlowNetwork original) {
		// Reduce this flow network with demands to a new flow network without demands
		int n = original.n;
		FlowNetwork reduced = new FlowNetwork(n+2); // the reduction has a new source and sink
													// Note: as a result, the node k on the original
													// flow network is now the node k+1
		
		// Add an edge with infinite value from the original sink to the original source
		reduced.addEdge(new Edge(original.sink+1, original.source+1), Double.POSITIVE_INFINITY);
		
		// Create all edges to the new source and sink (with dummy values)
		for(int k=0; k < n; k++) {
			reduced.addEdge(new Edge(reduced.source, k+1), 0);
			reduced.addEdge(new Edge(k+1, reduced.sink), 0);
		}
		// Create/Update the values of the edges capacities on the new graph
		for(int k=0; k < n; k++) {
			// Iterate over outbound edges
			for(Edge e: original.edgesOut(k)) {
				double dem = original.getDemand(e); // The demand from k to adj on the original problem
				
				// This is an incident edge on e.to, add this demand to the edge capacity from the new source to e.to
				Edge fromSource = new Edge(reduced.source, e.to+1);
				reduced.setCapacity(fromSource, dem + reduced.getCapacity(fromSource));
				// This is an outbound edge on e.from, add this demand to the edge capacity from e.from to the new sink
				Edge toSink = new Edge(e.from+1, reduced.sink);
				reduced.setCapacity(toSink, dem + reduced.getCapacity(toSink));
				// Add the edge from e.from to e.to
				reduced.addEdge(new Edge(e.from+1, e.to+1), original.getCapacity(e) - dem);
			}
		}
		
		return reduced;
	}
	
	private static Flow recoverOriginalFlow(Flow reducedFlow, FlowNetwork reduced, FlowNetwork original) {
		// Transform a max flow in the reduced problem to a feasible flow in the original problem
		// Returns: the recovered flow, a feasible flow for the original network
		HashMap<Edge, Double> originalFlowMap = new HashMap<Edge, Double>();
		HashMap<Edge, Double> reducedFlowMap = reducedFlow.getMap();
		double originalFlowValue = 0;
		
		// Iterate over all edges on the reduced problem flow
		for(Edge reduc: reducedFlowMap.keySet())
			if(reduc.from != reduced.source && reduc.to != reduced.sink) {
				Edge orig = new Edge(reduc.from-1, reduc.to-1); // the corresponding edge on the original problem
				
				originalFlowMap.put(orig, reducedFlowMap.get(reduc) + original.getDemand(orig)); // origFlow = reducFlow + demand
				
				if(reduc.to == original.sink)
					originalFlowValue += originalFlowMap.get(orig);
			}
		
		return new Flow(originalFlowMap, originalFlowValue);
	}
	
	public static Flow getFeasibleFlow(FlowNetwork original) {
		// Returns a feasible flow for this flow network
		int n = original.n;
		
		// If there aren't any edge demands
		if(!original.hasDemands()) {
			// Initialize the flow on edges on the network to 0
			HashMap<Edge, Double> flowMap = new HashMap<Edge, Double>();
			
			// For each node
			for(int from=0; from < n; from++)
				// Iterate over oubound edges
				for(Edge e: original.edgesOut(from))
					flowMap.put(e, 0.0); // set the flow on this edge to 0
		
			return new Flow(flowMap, 0.0);
		}
		// If there are edge demands
		FlowNetwork reduced = reduceFlowNetwork(original); // reduce the problem
		
		// recover the original flow and return it
		return recoverOriginalFlow(MaxFlow.getFlow(reduced), reduced, original);
	}
}
