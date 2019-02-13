package algorithms;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import models.Edge;
import models.ElementaryFlow;
import models.Flow;
import models.FlowNetwork;

public class KRouteDecomposition {
	// Class that provides funtionality to decompose a K-Route Flow into elementary flows.
	
	private static double getBottleneck(Collection<Edge> edges, HashMap<Edge, Double> flowMap) {
		double bottleneck = Double.POSITIVE_INFINITY;
		
		for(Edge e: edges)
			if(flowMap.containsKey(e) && flowMap.get(e) < bottleneck)
				bottleneck = flowMap.get(e);
		
		return bottleneck;
	}
	
	private static ElementaryFlow extractElementaryFlow(int K, Flow flow, FlowNetwork network, double v) {
		int n = network.n;
		HashMap<Edge, Double> flowMap = flow.getMap();
		ElementaryFlow elem = new ElementaryFlow();
		
		// Create the network with demands used to get an elementary flow
		FlowNetwork demandNW = new FlowNetwork(n+1);
		
		// Set the capacities and demands according to Lemma 4
		for(int node = 0; node < n; node++) {
			for(Edge e: network.edgesOut(node)) {
				demandNW.addEdge(e, Math.ceil(flowMap.get(e)/v));
				demandNW.setDemand(e, Math.floor(flowMap.get(e)/v));
			}
		}
		Edge toNewSink = new Edge(n-1, n);
		demandNW.addEdge(toNewSink, K);

		// Get the max flow on the new network
		Flow maxFlow = MaxFlow.getFlow(demandNW);
		
		// Check if there are K  paths
		if(maxFlow.getValue() < K)
			return null;
		
		// Extract the paths taken by the max flow
		Collection<List<Edge>> paths = AllFlowPaths.getAllFlowPaths(demandNW, maxFlow);
		for(List<Edge> path: paths)
			elem.addPath(path);
		
		// Find the minimal flow on all paths edges and set the flow value to it
		elem.setValue(getBottleneck(elem.getAllEdges(), flowMap));
		
		// Remove edges to the new sink
		for(List<Edge> path: elem.getPaths())
			((LinkedList<Edge>) path).removeLast();
		
		// Subtract this flow from the original flow
		for(Edge e: elem.getAllEdges())
			flowMap.put(e, flowMap.get(e)-elem.getValue());
		
		return elem;
	}
	
	public static Collection<ElementaryFlow> decompose(int K ,Flow route, FlowNetwork network) {
		// Returns the decomposition of a K-route flow, in the form of a set of elementary K-flows
		
		Collection<ElementaryFlow> allElemFlows = new LinkedList<ElementaryFlow>();
		double v = route.getValue()/K;
		
		ElementaryFlow elemFlow = extractElementaryFlow(K, route, network, v);
		while(elemFlow != null) {
			allElemFlows.add(elemFlow);
			elemFlow = extractElementaryFlow(K, route, network, v);
		}
	
		return allElemFlows;
	}
}
