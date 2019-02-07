import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import sensor_networks.ScheduleInterval;

public class SchedulePrinter {
	public static void printToFile(List<ScheduleInterval> schedule, String filename) throws IOException {
		FileWriter writer = new FileWriter(".\\output\\" + filename);

		for (ScheduleInterval interval : schedule) {
			List<Integer> sensors = (List<Integer>) interval.getSensors();

			Collections.sort(sensors);
			writer.write(String.format("[%2d, %2d]: ", interval.start, interval.end));

			for (int sensor : sensors)
				writer.write(sensor + ", ");
			writer.write("\n");
		}
		writer.close();
	}

	public static void printToConsole(List<ScheduleInterval> schedule) {
		for (ScheduleInterval interval : schedule) {
			List<Integer> sensors = (List<Integer>) interval.getSensors();

			Collections.sort(sensors);
			System.out.format("[%2d, %2d]: ", interval.start, interval.end);

			for (int sensor : sensors)
				System.out.format("%d, ", sensor);
			System.out.println();
		}
	}
}
