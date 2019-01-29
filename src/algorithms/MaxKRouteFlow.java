package algorithms;

import models.FlowNetwork;
import models.Node;

public class MaxKRouteFlow {
	public int K;
	public FlowNetwork nw; // The network for which the problem will be solved
	float maxFlowValue; // The value of the maximal K Route Flow
	float[][] maxFlow; // A matrix representing a maximal K Route Flow
	
	public MaxKRouteFlow(FlowNetwork nw, int K) {
		this.K = K;
		this.nw = nw;
		this.maxFlowValue = Float.NEGATIVE_INFINITY;
		
		// Create the flow matrix
		int V = nw.graph.nNodes;
		this.maxFlow = new float[V][V];
	}
	
	public FlowNetwork getMaxBoundedFlowNetwork(float maxBound) {
		int nNodes = nw.graph.nNodes;
		FlowNetwork minNW = new FlowNetwork(nNodes);
		
		for(int node=0; node < nNodes; node++) {
			Node origNode = nw.getNode(node);
			
			for(int adj : origNode.out)
				minNW.addEdge(node, adj, Math.min(nw.getCapacity(node, adj), maxBound));
		}
		
		return minNW;
	}
	
	private float Phi(float p) {
		FlowNetwork bounded = getMaxBoundedFlowNetwork(p);
		MaxFlow solver = new MaxFlow(bounded);
		
		return solver.getMaxFlowValue() - K*p;
	}
	
	public void solveMaxKRouteFlow() {
		// Returns a K-Route Flow with maximum possible value.
		int E = nw.graph.nEdges;
		float minStep = (float) 1/(E*E);
		
		MaxFlow solver = new MaxFlow(nw);
		float upper = solver.getMaxFlowValue()/K;
		
		// Find the root of Phi
		float root = upper;
		for(int r = 1; r <= E; r++) {
			float inv_r = 1/r;
			
			// Initialize p to the biggest multiple of 1/p thats smaller than upper
			float p = (float) (r*Math.floor(upper/r));
			float phi = Phi(p);
			float upperPhi = phi;
			
			// Decrease p until a inteval for the zero is found
			while(p >= 0 && phi < 0) {
				upperPhi = phi;
				p -= inv_r;
				phi = Phi(p);
			}
			upper = p + inv_r;
			
			// If phi is zero, a root was found
			if(phi == 0) {
				root = p;
				break;
			}
			
			// If the zero is inside an interval of size 1/E^2, calculate it
			float lower = upper - minStep;
			float lowerPhi = Phi(lower);
			if(lowerPhi >= 0) {
				// In this interval, this function is linear
				root = (lowerPhi*upper - upperPhi*lower)/(lowerPhi - upperPhi);
				break;
			}
		}
		solver.setFlowNetwork(getMaxBoundedFlowNetwork(root));
		maxFlowValue = solver.getMaxFlowValue();
		maxFlow = solver.getMaxFlow();
	}
	
	public float getMaxFlowValue() {
		if(maxFlowValue == Float.NEGATIVE_INFINITY)
			solveMaxKRouteFlow();
		
		return maxFlowValue;
	}
	
	public float[][] getMaxFlow() {
		if(maxFlowValue == Float.NEGATIVE_INFINITY)
			solveMaxKRouteFlow();
		
		return maxFlow;
	}
}
