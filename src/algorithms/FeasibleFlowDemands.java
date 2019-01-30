package algorithms;

import models.FlowNetwork;
import models.Node;

public class FeasibleFlowDemands {
	FlowNetwork original;
	FlowNetwork reduced;
	float[][] demand;
	float[][] feasibleFlow;
	
	public FeasibleFlowDemands(FlowNetwork nw, float[][] demand) {
		this.original = nw;
		this.demand = demand;
	}
	
	private void reduceFlowNetwork() {
		int V = original.graph.nNodes;
		reduced = new FlowNetwork(V+2);
		
		reduced.addEdge(1, V, Float.POSITIVE_INFINITY);
		for(int k=0; k < V; k++) {
			reduced.addEdge(reduced.source, k+1, 0);
			reduced.addEdge(k+1, reduced.sink, 0);
		}
		for(int k=0; k < V; k++) {
			Node origNode = original.getNode(k);

			for(int adj: origNode.out) {
				reduced.updateCapacity(reduced.source, adj+1, +demand[k][adj]);
				reduced.updateCapacity(k+1, reduced.sink, +demand[k][adj]);
				reduced.addEdge(k+1, adj+1, original.getCapacity(k, adj)-demand[k][adj]);
			}
		}
	}
	
	private float[][] recoverOriginalFlow(float[][] reducedFlow) {
		int V = original.graph.nNodes;
		float[][] origFlow = new float[V][V];
		
		for(int i=0; i<V; i++)
			for(int j=0; j<V; j++)
				origFlow[i][j] = reducedFlow[i+1][j+1]+demand[i][j];
		
		return origFlow;
	}
	
	public float[][] getFeasibleFlow() {
		reduceFlowNetwork();
		
		MaxFlow solver = new MaxFlow(reduced);
		float[][] flow = recoverOriginalFlow(solver.getMaxFlow());
		
		return flow;
	}
}
