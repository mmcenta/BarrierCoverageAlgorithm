package sensor_networks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import algorithms.MaxElementaryFlow;
import models.Edge;
import models.FlowNetwork;
import models.Graph;
import models.Node;

public class KBarrierCoverage {
	public final double x_max, y_max, R;
	public final int K;
	public int nSensors;
	private Sensor left, right;
	private ArrayList<Sensor> sensors;
	private Graph coverage;
	private FlowNetwork network;
	
	public KBarrierCoverage(int K, double x_max, double y_max, double R) {
		this.x_max = x_max;
		this.y_max = y_max;
		this.R = R;
		
		this.K = K;
		this.nSensors = 0;
		
		this.sensors = new ArrayList<Sensor>();
		this.left = new Sensor(0, 0, Integer.MAX_VALUE);
		this.right = new Sensor(x_max, 0 , Integer.MAX_VALUE);
	}
	
	public KBarrierCoverage(int K, double x_max, double y_max, double R, int nSensors) {
		this.x_max = x_max;
		this.y_max = y_max;
		this.R = R;
		
		this.K = K;
		this.nSensors = 0;
		
		this.sensors = new ArrayList<Sensor>(nSensors);
		this.left = new Sensor(0, 0, Integer.MAX_VALUE);
		this.right = new Sensor(x_max, 0 , Integer.MAX_VALUE);
	}
	
	/* General Functions */
	public void addSensor(double x, double y, int durability, int number) {
		sensors.add(new Sensor(x, y, durability, number));
		nSensors++;
	}
	
	private ArrayList<Sensor> sortedSensors() {
		ArrayList<Sensor> ans = new ArrayList<Sensor>(sensors);
		Collections.sort(ans, new SensorComparator());
		
		return ans;
	}
	
	public void buildCoverageGraph() {
		int n = nSensors+2;  // number of vertices on the coverage graph (counting left and right wall)
		
		// Sort the sensors according to their x-coordinate
		ArrayList<Sensor> sorted = sortedSensors();
		
		// Create coverage graph
		coverage = new Graph(n);   
		left.setNumber(0);  // the left wall is defaulted to node 0 (first node)
		right.setNumber(n-1);  // the right wall is defaulted to node V-1 (last node)
		
		// Traverse the array of sensors, creating the necessary edges
		for(int k=0; k < nSensors; k++) {
			Sensor s = sorted.get(k);
			int numS = s.number;
			
			if(s.x - left.x < R)
				// if the sensor is close (< R) to the left wall, connect the two
				coverage.addEdge(left.number, numS);
			
			if(right.x - s.x < R)
				// if the sensor is close (< R) to the right wall, connect the two
				coverage.addEdge(numS, right.number);
			
			// Check the sensors to the left of sensor until they are too far away
			for(int leftIdx = k-1; leftIdx >= 0; leftIdx--) {
				Sensor leftSensor = sorted.get(leftIdx);
				
				if(s.x - leftSensor.x < R && Sensor.getDistance(s, leftSensor) < R)
					// if they are close (< R), connect them
					coverage.addEdge(numS, leftSensor.number);
				
				else if (s.x - leftSensor.x > R)
					// if the sensors are too far away to the left, stop checking
					break;
			}
			// Check the sensors to the right of sensor until they are too far away
			for(int rightIdx = k+1; rightIdx < sensors.size(); rightIdx++) {
				Sensor rightSensor = sorted.get(rightIdx);
				
				if(rightSensor.x - s.x < R && Sensor.getDistance(s, rightSensor) < R)
					// if they are close (< R), connect them
					coverage.addEdge(numS, rightSensor.number);
					
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
		
		network = new FlowNetwork(2*nSensors+2); // The source is the left wall node and the sink is the right wall node
		
		// Build the coverage graph
		buildCoverageGraph();
		
		// Add the edges parting from the source (left wall)
		for(Edge e: coverage.getNode(left.number).out)
			network.addEdge(e, sensors.get(e.to-1).lifetime);
		
		// Separate each sensor node into two and link the adjacent nodes from the out-node to the adjacent node's in-node
		for(int k = 1; k <= nSensors; k++) {
			int dur = sensors.get(k-1).lifetime;
			
			// The out-node for the sensor k is the node k+nSensors
			network.addEdge(new Edge(k, k+nSensors), dur); 

			Node coverageNode = coverage.getNode(k);
			for(Edge e: coverageNode.out) {
				if(e.to != right.number && e.to != left.number)
					network.addEdge(new Edge(e.from+nSensors, e.to), dur);
				else if(e.to == right.number)
					network.addEdge(new Edge(k + nSensors, 2*nSensors+1), dur);
			}
		}
	}

	/* Homogenous Lifetimes Solution */
	public Collection<List<Integer>> getNodeDisjointPaths() {
		// Build the coverage graph and the associated flow network for this instance of the problem
		buildFlowNetwork();
		
		Collection<List<Edge>> paths = MaxElementaryFlow.getElementaryKFlow(network).getPaths();
		
		// Extract the node-disjoint sensor paths
		LinkedList<List<Integer>> sensorPaths = new LinkedList<List<Integer>>();
		for(List<Edge> path : paths) {
			LinkedList<Integer> sensorPath = new LinkedList<Integer>();
			
			for(Edge e: path)
				if(e.to > 0 && e.to <= nSensors) 
					sensorPath.add(e.to);
			
			sensorPaths.add(sensorPath);
			System.out.println(sensorPath);
		}
		
		return sensorPaths;
	}
	
	public List<ScheduleInterval> homogenousScheduling() {
		Collection<List<Integer>> paths = getNodeDisjointPaths();
		int M = paths.size();
		
		List<ScheduleInterval> schedule = new LinkedList<ScheduleInterval>();
		
		int lifetime = M/K;
		
		Iterator<List<Integer>> iter = paths.iterator();
		for(int t = 0; t < lifetime; t++) {
			ScheduleInterval interval = new ScheduleInterval(t, t+1);
			
			for(int i=0; i < K; i++)
				interval.addSensors(iter.next());

			schedule.add(interval);
		}
		
		return schedule;
	}

	/* Distinct Lifetimes Solution */
	
}
