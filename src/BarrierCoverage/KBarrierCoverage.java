package BarrierCoverage;

import java.util.ArrayList;
import java.util.Collections;

import FordFulkerson.FlowNetwork;
import models.Graph;

public class KBarrierCoverage {
	float x_max, y_max, R;
	int K;
	Sensor left, right;
	ArrayList<Sensor> sensors;
	Graph coverage;
	
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
	}
	
	public void sortSensors() {
		Collections.sort(sensors, Sensor.xCoordCompare);
	}
	
	public void buildCoverageGraph() {
		int V = sensors.size()+2;  // number of vertices on the coverage graph (counting left and right wall)
		
		// Sort the sensors according to their x-coordinate
		sortSensors();
		
		// Create coverage graph
		coverage = new Graph(V);   
		left.setNode(0);  // the left wall is defaulted to node 0 (first node)
		right.setNode(V-1);  // the right wall is defaulted to node V-1 (last node)
		
		// Assign a node to each other sensor
		for(int k=0; k < sensors.size(); k++)
			sensors.get(k).setNode(k+1);
		
		// Traverse the array of sensors, creating the necessary edges
		for(int k=0; k < sensors.size(); k++) {
			Sensor s = sensors.get(k);
			
			if(s.x - left.x < R)
				// if the sensor is close (< R) to the left wall, connect the two
				coverage.addUndirectedEdge(s.getNode(), left.getNode());
			
			if(right.x - s.x < R)
				// if the sensor is close (< R) to the right wall, connect the two
				coverage.addUndirectedEdge(s.getNode(), right.getNode());
			
			// Check the sensors to the left of sensor until they are too far away
			for(int left=k-1; left >= 0; left--) {
				Sensor leftSensor = sensors.get(left);
				
				if(s.x - leftSensor.x < R && Sensor.getDistance(s, leftSensor) < R)
					// if they are close (< R), connect them
					coverage.addUndirectedEdge(s.getNode(), leftSensor.getNode());
				
				else if (s.x - leftSensor.x > R)
					// if the sensors are too far away to the left, stop checking
					break;
			}
			// Check the sensors to the right of sensor until they are too far away
			for(int right=k+1; right < sensors.size(); right++) {
				Sensor rightSensor = sensors.get(right);
				
				if(rightSensor.x - s.x < R && Sensor.getDistance(s, rightSensor) < R)
					// if they are close (< R), connect them
					coverage.addUndirectedEdge(s.getNode(), rightSensor.getNode());
					
				else if(rightSensor.x - s.x > R)
					// if the sensors are too far away to the right, stop checking
					break;
			}
		}
	}
	
	private FlowNetwork buildFlowNetwork() {
		
	}
}
