package sensor_networks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ScheduleInterval {
	// Represents a interval in the schedule when the configuration of the sensors doesn't change
	public final int duration; // the duration of this interval
	private Collection<List<Integer>> activePaths; // the paths that remain active during this schedule

	public ScheduleInterval(int duration) {
		this.duration = duration;

		this.activePaths = new LinkedList<List<Integer>>();
	}

	public void addPath(List<Integer> path) {
		activePaths.add(path);
	}

	public Collection<List<Integer>> getPaths() {
		return activePaths;
	}
	
	public Collection<Integer> getAllSensors() {
		Collection<Integer> all = new LinkedList<Integer>();
		
		for(List<Integer> path: activePaths)
			all.addAll(path);
		
		return all;
	}
}
