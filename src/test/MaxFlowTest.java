package test;

import algorithms.MaxFlow;
import models.Edge;
import models.FlowNetwork;

public class MaxFlowTest {
	public static void sourceAndSinkTest() {
		System.out.print("	Source and Sink Test: ");
		FlowNetwork nw = new FlowNetwork(2);
		Edge e = new Edge(0, 1);
		nw.addEdge(e, -1);
		
		double[] validCapacities = {0, Double.MIN_VALUE, 0.1, 1, 3.2, 37.4, 22102049.223143, Double.MAX_VALUE};
		for(double cap : validCapacities) {
			nw.setCapacity(e, cap);
			MaxFlow solver = new MaxFlow(nw);
			double flowValue = solver.getFlow().getValue();
			
			if(flowValue != cap)
				System.out.format("ERROR: expected max flow = %f, got %f. ", cap, flowValue);
			assert(flowValue == cap);
		}
		
		double[] invalidCapacities = {-Double.MIN_VALUE, -0.1, -1, -231, -Double.MAX_VALUE, Double.NEGATIVE_INFINITY};
		for(double cap : invalidCapacities) {
			nw.setCapacity(e, cap);
			MaxFlow solver = new MaxFlow(nw);
			double flowValue = solver.getFlow().getValue();
			
			if(flowValue != 0.0)
				System.out.format("ERROR: expected max flow = 0, got %f. ", flowValue);
			assert(flowValue == 0);
		}
		System.out.println("Finished.");
	}
	
	public static void twoPathTest() {
		System.out.print("	Two Path Test: ");
		FlowNetwork nw = new FlowNetwork(4);
		nw.addEdge(new Edge(0, 1), 1);
		nw.addEdge(new Edge(0, 2), 1);
		nw.addEdge(new Edge(1, 2), 1);
		nw.addEdge(new Edge(1, 3), 1);
		nw.addEdge(new Edge(2, 3), 1);
		
		MaxFlow solver = new MaxFlow(nw);
		double flowValue = solver.getFlow().getValue();
		
		if(flowValue != 2)
			System.out.format("ERROR: expected max flow = 2, got %f. ", flowValue);
		assert(flowValue == 2);
		System.out.println("Finished.");
	}
	
	public static void shortestPathNotOptimalTest() {
		System.out.print("	Shortest Path Not Optimal Test: ");
		FlowNetwork nw =  new FlowNetwork(8);
		// Create the shortest path
		nw.addEdge(new Edge(0, 1), 1);
		nw.addEdge(new Edge(1, 2), 1);
		nw.addEdge(new Edge(2, 7), 1);
		
		// Create edges for optimal paths
		nw.addEdge(new Edge(0, 3), 1);
		nw.addEdge(new Edge(3, 4), 1);
		nw.addEdge(new Edge(4, 2), 1);
	 	
		nw.addEdge(new Edge(1, 5), 1);
		nw.addEdge(new Edge(5, 6), 1);
		nw.addEdge(new Edge(6, 7), 1);
		
		// Solve the problem
		MaxFlow solver = new MaxFlow(nw);
		double flowValue = solver.getFlow().getValue();
		
		if(flowValue != 2)
			System.out.format("ERROR: expected max flow = 2, got %f. ", flowValue);
		assert(flowValue == 2);
		System.out.println("Finished.");
	}
	
	public static void start() {
		System.out.println("Testing MaxFlow: ");
		sourceAndSinkTest();
		twoPathTest();
		shortestPathNotOptimalTest();
		System.out.println("Done testing MaxFlow.");
	}
	
	public void main(String[] args) {
		System.out.println("Testing MaxFlow: ");
		sourceAndSinkTest();
		twoPathTest();
		shortestPathNotOptimalTest();
	}
}
