package sensor_networks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ScheduleInterval {
	public int start, end;
	private Collection<Integer> activeSensors;
	
	public ScheduleInterval(int start, int end) {
		this.start = start;
		this.end = end;
		
		this.activeSensors = new LinkedList<Integer>();
	}
	
	public ScheduleInterval(int start, int end, List<Integer> sensors) {
		this.start = start;
		this.end = end;
		
		this.activeSensors = new LinkedList<Integer>(sensors);
	}
	
	public void addSensors(List<Integer> sensors) {
		activeSensors.addAll(sensors);
	}
	
	public Collection<Integer> getSensors() {
		return activeSensors;
	}
}
