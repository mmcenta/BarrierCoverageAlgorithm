package algorithms;

import models.Edge;
import models.Flow;
import models.FlowNetwork;

public class MaxKRouteFlow {
	public static FlowNetwork getUpperBoundedFlowNetwork(double upperBound, FlowNetwork network) {
		// Returns a flow network with the same nodes and edges as network
		// but with new edge capacities w' such that w' = min(w, upperBound)		
		int n = network.n;
		FlowNetwork minNW = new FlowNetwork(n);
		
		// For each node
		for(int node=0; node < n; node++) {
			// Iterate over outbound edges
			for(Edge e: network.edgesOut(node))
				minNW.addEdge(e, Math.min(network.getCapacity(e), upperBound));
		}
		
		return minNW;
	}
	
	private static double Phi(double p, int K, FlowNetwork network) {
		// The Phi function. If F(p) is the max flow in the network with
		// edge capacities bounded above by p, then Phi(p) = F(p) - K*p
		FlowNetwork bounded = getUpperBoundedFlowNetwork(p, network);
		
		return MaxFlow.getFlow(bounded).getValue() - K*p;
	}
	
	public static Flow getFlow(int K, FlowNetwork network) {
		// Returns a K-Route Flow with maximum possible value.
		int m = network.m;
		double minStep = (double) 1/(m*m);
		
		// Calculate an upper bound for the root
		double upper = MaxFlow.getFlow(network).getValue()/K;
		
		// Find the root of Phi
		double root = upper;
		for(int r = 1; r <= m; r++) {
			double inv_r = 1/r;
			
			// Initialize p to the biggest multiple of 1/p thats smaller than upper
			double p = r*Math.floor(upper/r);
			double phi = Phi(p, K, network);
			double upperPhi = phi;
			
			// Decrease p until a inteval for the zero is found
			while(p >= 0 && phi < 0) {
				upperPhi = phi;
				p -= inv_r;
				phi = Phi(p, K, network);
			}
			upper = p + inv_r;
			
			// If phi is zero, a root was found
			if(phi == 0) {
				root = p;
				break;
			}
			
			// If the zero is inside an interval of size 1/E^2, calculate it
			double lower = upper - minStep;
			double lowerPhi = Phi(lower, K, network);
			if(lowerPhi >= 0) {
				// In this interval, this function is linear
				root = (lowerPhi*upper - upperPhi*lower)/(lowerPhi - upperPhi);
				break;
			}
		}
		
		return MaxFlow.getFlow(getUpperBoundedFlowNetwork(root, network));
	}
}
