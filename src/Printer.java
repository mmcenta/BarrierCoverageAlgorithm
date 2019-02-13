import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sensor_networks.ScheduleInterval;

public class Printer {
	// Auxiliary class that prints results to the console
	public static void printNodeDisjointPaths(Collection<List<Integer>> paths, Map<List<Integer>, Integer> values) {
		// Prints a list of node disjoint paths and their values
		System.out.println("node-disjoint paths: ");
		for (List<Integer> path : paths) {
			Iterator<Integer> iter = path.listIterator();

			System.out.print("    value = " + values.get(path) + " | ");
			System.out.print(iter.next());
			while (iter.hasNext())
				System.out.print("---" + iter.next());
			System.out.println();
		}
	}

	public static void printSchedule(List<ScheduleInterval> schedule) {
		// Prints a shedule and the network lifetime
		int time = 0;
		System.out.println("schedule: ");
		for (ScheduleInterval interval : schedule) {
			Collection<List<Integer>> paths = interval.getPaths();
			Iterator<List<Integer>> iter = paths.iterator();
			
			System.out.format("(%2d, %2d): ", time, time + interval.duration);
			time += interval.duration;

			System.out.println(iter.next());
			while(iter.hasNext())
				System.out.println("          "+iter.next());
		}
		System.out.println("network lifetime: " + time);
	}
	
	public static void printNodeDisjointPaths(Collection<List<Integer>> paths) {
		// Print a list of nde-disjoint paths (without associated values)
		System.out.println("node-disjoint paths: ");
		for (List<Integer> path : paths) {
			Iterator<Integer> iter = path.listIterator();

			System.out.print("    "+iter.next());
			while (iter.hasNext())
				System.out.print("---" + iter.next());
			System.out.println();
		}
	}
}
