import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sensor_networks.ScheduleInterval;

public class ResultWriter {
	public static void printSchedule(List<ScheduleInterval> schedule, String filename) throws IOException {
		FileWriter writer = new FileWriter(".\\output\\" + filename);

		writer.write("schedule: /n");
		
		int time = 0;
		for (ScheduleInterval interval : schedule) {
			List<Integer> sensors = (List<Integer>) interval.getAllSensors();

			Collections.sort(sensors);

			writer.write(String.format("[%2d, %2d]: ", time, time + interval.duration));
			time += interval.duration;

			for (int sensor : sensors)
				writer.write(sensor + ", ");
			writer.write("\n");
		}
		writer.write("network lifetime: " + time + "\n");

		writer.close();
	}

	public static void printNodeDisjointPaths(Collection<List<Integer>> paths, Map<List<Integer>, Integer> values,
			String filename) throws IOException {
		FileWriter writer = new FileWriter(".\\output\\" + filename);

		writer.write("node-disjoint paths: \n");
		for (List<Integer> path : paths) {
			Iterator<Integer> iter = path.listIterator();
			
			writer.write("value = " + values.get(path) + " | ");
			writer.write(iter.next());
			while (iter.hasNext())
				writer.write("---" + iter.next());
			writer.write("\n");
		}
		writer.write("\n");

		writer.close();
	}
}
