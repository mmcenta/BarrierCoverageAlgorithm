import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sensor_networks.ScheduleInterval;

public class ResultPrinter {
	public static void printToFile(Collection<List<Integer>> ndPaths, List<ScheduleInterval> schedule, String filename)
			throws IOException {
		FileWriter writer = new FileWriter(".\\output\\" + filename);

		writer.write("schedule:/n");
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
		writer.write("network lifetime: " + time);

		writer.close();
	}

	public static void printNodeDisjointPaths(Collection<List<Integer>> paths, Map<List<Integer>, Integer> values) {
		System.out.println("node-disjoint paths: ");
		for (List<Integer> path : paths) {
			Iterator<Integer> iter = path.listIterator();

			System.out.print("value = " + values.get(path) + " | ");
			System.out.print(iter.next());
			while (iter.hasNext())
				System.out.print("---" + iter.next());
			System.out.println();
		}
		System.out.println();
	}

	public static void printSchedule(List<ScheduleInterval> schedule) {
		int time = 0;

		System.out.println("schedule: ");
		for (ScheduleInterval interval : schedule) {
			List<Integer> sensors = (List<Integer>) interval.getAllSensors();

			Collections.sort(sensors);

			System.out.format("[%2d, %2d]: ", time, time + interval.duration);
			time += interval.duration;

			for (int sensor : sensors)
				System.out.format("%d, ", sensor);
			System.out.println();
		}
		System.out.println();

		System.out.println("network lifetime: " + time);
		System.out.println();
	}
}
