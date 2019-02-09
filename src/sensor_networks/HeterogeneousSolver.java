package sensor_networks;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import algorithms.KRouteDecomposition;
import algorithms.MaxKRouteFlow;
import models.Edge;
import models.ElementaryFlow;
import models.FlowNetwork;
import models.Graph;

public class HeterogeneousSolver {
	private static FlowNetwork getFlowNetwork(KBarrierCoverageInstance ins) {
		// Build flow nework by spliting sensor nodes (not left and right) into a
		// in-node" and a out-node
		// The capacity of the edge between the in-node and the out-node is the sensor
		// durability
		// The capacity between other nodes should be high enough to never restrict
		// flow, we chose to sensor durability as well.
		int nSensors = ins.nSensors;
		int left = 0;
		int right = nSensors+1;
		FlowNetwork network = new FlowNetwork(2 * nSensors + 2); // The source is the left wall node and the sink
																 // is the original node

		// Build the coverage graph
		Graph coverage = ins.getCoverageGraph();

		// Add the edges parting from the source (left wall)
		for (Edge e : coverage.edgesOut(left))
			network.addEdge(e, ins.getSensor(e.to).lifetime);

		// Separate each sensor node into two and link the adjacent nodes from the
		// out-node to the adjacent node's in-node
		for (int k = 1; k <= nSensors; k++) {
			int dur = ins.getSensor(k).lifetime;

			// The out-node for the sensor k is the node k+nSensors
			network.addEdge(new Edge(k, k + nSensors), dur);

			for (Edge e : coverage.edgesOut(k)) {
				if (e.to != right && e.to != left)
					network.addEdge(new Edge(e.from + nSensors, e.to), dur);
				else if (e.to == right)
					network.addEdge(new Edge(k + nSensors, 2 * nSensors + 1), dur);
			}
		}
		
		return network;
	}
	
	public static Map<List<Integer>, Integer> getValuesFromPaths(Collection<List<Integer>> paths, KBarrierCoverageInstance ins) {
		Map<List<Integer>, Integer> values = new HashMap<List<Integer>, Integer>();
		
		for(List<Integer> path: paths) {
			int value = Integer.MAX_VALUE;
			
			for(int sensor: path) {
				if(ins.getSensor(sensor).lifetime < value)
					value = ins.getSensor(sensor).lifetime;
			}
			
			values.put(path, value);
		}
		
		return values;
	}
	
	public static List<ScheduleInterval> schedule(KBarrierCoverageInstance ins) {
		List<ScheduleInterval> schedule = new LinkedList<ScheduleInterval>();
		FlowNetwork network = getFlowNetwork(ins);
		int K = ins.K;
		int nSensors = ins.nSensors;

		Collection<ElementaryFlow> elemPaths = KRouteDecomposition.decompose(K, MaxKRouteFlow.getFlow(K, network), network);

		for (ElementaryFlow ef : elemPaths) {
			ScheduleInterval interval = new ScheduleInterval((int) ef.getValue());

			for (List<Edge> path : ef.getPaths()) {
				List<Integer> sensorPath = new LinkedList<Integer>();

				for (Edge e : path)
					if (e.to > 0 && e.to <= nSensors)
						sensorPath.add(e.to);

				interval.addPath(sensorPath);
			}

			schedule.add(interval);
		}

		return schedule;
	}
}
