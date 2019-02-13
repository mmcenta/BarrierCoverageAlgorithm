package sensor_networks;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import algorithms.NodeDisjointPaths;

public class HomogeneousSolver {
	public static List<ScheduleInterval> schedule(KBarrierCoverageInstance ins) {
		// Provides a optimal schedule for an instance of the K-Barrier Coverage Problem with unit sensor lifetimes.
		
		Collection<List<Integer>> paths = NodeDisjointPaths.getNodeDisjointPaths(ins.getCoverageGraph());
		int K = ins.K;
		int M = paths.size();
		
		// Initialize the schedule
		List<ScheduleInterval> schedule = new LinkedList<ScheduleInterval>();
		
		Iterator<List<Integer>> iter = paths.iterator();
		while(iter.hasNext()) {
			LinkedList<Integer> path = (LinkedList<Integer>) iter.next();
			path.removeFirst(); // get rid of the source node
			path.removeLast(); // get rid of the sink node
		}
		
		iter = paths.iterator();
		int lifetime = M / K;
		for(int t = 0; t < lifetime; t++) {
			ScheduleInterval interval = new ScheduleInterval(1);

			for(int i = 0; i < K; i++)
				interval.addPath(iter.next());

			schedule.add(interval);
		}

		return schedule;
	}
}
