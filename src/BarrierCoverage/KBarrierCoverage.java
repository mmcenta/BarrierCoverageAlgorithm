package BarrierCoverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import algorithms.MaxFlow;
import models.FlowNetwork;
import models.Graph;
import models.Node;

public class KBarrierCoverage {
	float x_max, y_max, R;
	int K, nSensors;
	Sensor left, right;
	ArrayList<Sensor> sensors;
	Graph coverage;
	FlowNetwork nw;
	
	public KBarrierCoverage(float x_max, float y_max, float R, int K) {
		this.x_max = x_max;
		this.y_max = y_max;
		
		this.R = R;
		this.K = K;

		this.sensors = new ArrayList<Sensor>();
		this.left = new Sensor(0, 0, Integer.MAX_VALUE);
		this.right = new Sensor(x_max, 0 , Integer.MAX_VALUE);
	}
	
	public KBarrierCoverage(float x_max, float y_max, float R, int K, int nSensors) {
		this.x_max = x_max;
		this.y_max = y_max;
		
		this.R = R;
		this.K = K;

		this.sensors = new ArrayList<Sensor>(nSensors);
		this.left = new Sensor(0, 0, Integer.MAX_VALUE);
		this.right = new Sensor(x_max, 0 , Integer.MAX_VALUE);
	}
	
	public void addSensor(float x, float y, int durability) {
		sensors.add(new Sensor(x, y, durability));
		nSensors++;
	}
	
	private List<Sensor> sortedSensors() {
		ArrayList<Sensor> ans = new ArrayList<Sensor>(sensors);
		Collections.sort(ans, Sensor.xCoordCompare);
		
		return ans;
	}
	
	public void buildCoverageGraph() {
		int V = nSensors+2;  // number of vertices on the coverage graph (counting left and right wall)
		
		// Sort the sensors according to their x-coordinate
		List<Sensor> sorted = sortedSensors();
		
		// Create coverage graph
		coverage = new Graph(V);   
		left.setNumber(0);  // the left wall is defaulted to node 0 (first node)
		right.setNumber(V-1);  // the right wall is defaulted to node V-1 (last node)
		
		// Traverse the array of sensors, creating the necessary edges
		for(int k=0; k < nSensors; k++) {
			Sensor s = sorted.get(k);
			int numS = s.getNumber();
			
			if(s.x - left.x < R)
				// if the sensor is close (< R) to the left wall, connect the two
				coverage.addUndirectedEdge(numS, left.getNumber());
			
			if(right.x - s.x < R)
				// if the sensor is close (< R) to the right wall, connect the two
				coverage.addUndirectedEdge(numS, right.getNumber());
			
			// Check the sensors to the left of sensor until they are too far away
			for(int leftIdx = k-1; leftIdx >= 0; leftIdx--) {
				Sensor leftSensor = sorted.get(leftIdx);
				
				if(s.x - leftSensor.x < R && Sensor.getDistance(s, leftSensor) < R)
					// if they are close (< R), connect them
					coverage.addUndirectedEdge(numS, leftSensor.getNumber());
				
				else if (s.x - leftSensor.x > R)
					// if the sensors are too far away to the left, stop checking
					break;
			}
			// Check the sensors to the right of sensor until they are too far away
			for(int rightIdx = k+1; rightIdx < sensors.size(); rightIdx++) {
				Sensor rightSensor = sorted.get(rightIdx);
				
				if(rightSensor.x - s.x < R && Sensor.getDistance(s, rightSensor) < R)
					// if they are close (< R), connect them
					coverage.addUndirectedEdge(numS, rightSensor.getNumber());
					
				else if(rightSensor.x - s.x > R)
					// if the sensors are too far away to the right, stop checking
					break;
			}
		}
	}
	
	public void buildFlowNetwork() {
		// Build flow nework by spliting sensor nodes (not left and right) into a in-node" and a out-node
		// The capacity of the edge between the in-node and the out-node is the sensor durability
		// The capacity between other nodes should be high enough to never restrict flow, we chose to sensor durability as well.
		
		nw = new FlowNetwork(2*nSensors + 2); // The source is the left wall node and the sink is the right wall node
		
		// Build the coverage graph
		buildCoverageGraph();
		
		// Add the edges parting from the source (left wall, which is numbered 0 by convention)
		for(int adj : coverage.getNode(0).out)
			nw.addEdge(0, adj, sensors.get(adj).durability);
		
		// Separate each sensor node into two and link the adjacent nodes from the out-node to the adjacent node's in-node
		for(int k = 1; k <= nSensors; k++) {
			int dur = sensors.get(k).durability;
			
			// The out-node for the sensor with number k is k+nSensors
			nw.addEdge(k, k + nSensors, dur); 

			Node coverageNode = coverage.getNode(k);
			for(int adj: coverageNode.out)
				nw.addEdge(k + nSensors, adj, dur);
		}
	}
	
	private LinkedList<Integer> dfsPath(int[][] adj) {
		LinkedList<Integer> path = new LinkedList<Integer>();
		
		return path;
	}
	
	public LinkedList<LinkedList<Integer>> getNodeDisjointPaths() {
		LinkedList<LinkedList<Integer>> paths = new LinkedList<>();
		buildFlowNetwork();
		
		MaxFlow max = new MaxFlow(nw);
		float maxFlow = max.getMaxFlowValue();
		float[][] flow = max.getMaxFlow();
		
		// Depth-first search
		LinkedList<Integer> currPath = new LinkedList<Integer>();
		LinkedList<Integer> stack = new LinkedList<Integer>();
		stack.push(0);
		
		
		return paths;
	}
}
