import java.io.IOException;
import java.util.*;

import algorithms.*;
import models.*;
import sensor_networks.*;

@SuppressWarnings("unused")
public class Main {
	public static void baseTests() {
		// Runs the algorithm on the provided files, was used to provide the results.txt
		// file
		KBarrierCoverageInstance prob = null;

		// sensornetwork0.doc
		System.out.println("file: sensonetwork0.doc");
		try {
			prob = Reader.readKBarrierCoverage("sensornetwork0.doc");
		} catch (IOException e) {
			e.printStackTrace();
		}

		long t0 = System.currentTimeMillis(); // start timer

		Collection<List<Integer>> ndPaths = NodeDisjointPaths.getNodeDisjointPaths(prob.getCoverageGraph());
		List<ScheduleInterval> schedule = HomogeneousSolver.schedule(prob);

		long t1 = System.currentTimeMillis(); // stop timer

		Printer.printNodeDisjointPaths(ndPaths);
		Printer.printSchedule(schedule);
		System.out.println("computation time: " + (t1 - t0) + " ms");

		// sensornetwork1.doc
		System.out.println("\nfile: sensonetwork1.doc");
		try {
			prob = Reader.readKBarrierCoverage("sensornetwork1.doc");
		} catch (IOException e) {
			e.printStackTrace();
		}

		t0 = System.currentTimeMillis(); // start timer

		schedule = HeterogeneousSolver.schedule(prob);
		ndPaths = NodeDisjointPaths.getNodeDisjointPaths(prob.getCoverageGraph());
		Map<List<Integer>, Integer> values = HeterogeneousSolver.getValuesFromPaths(ndPaths, prob);

		t1 = System.currentTimeMillis(); // stop timer

		Printer.printNodeDisjointPaths(ndPaths, values);
		Printer.printSchedule(schedule);
		System.out.println("computation time: " + (t1 - t0) + " ms");
	}

	public static void main(String[] args) {
		// Initialize problem instance
		KBarrierCoverageInstance prob = null;

		if(!args[0].equals("-hm") && !args[0].equals("-ht")) {
			System.out.println("Invalid mode argument: expected '-hm' or '-ht', got '" + args[0] + "'.");
		} else {
			// Read the file
			System.out.println("file: " + args[1]);
			try {
				prob = Reader.readKBarrierCoverage(args[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}

			long t0 = 0;
			long t1 = 0;
			Collection<List<Integer>> ndPaths;
			List<ScheduleInterval> schedule = null;

			// Solution for homogeneous lifetimes
			if(args[0].equals("-hm")) {
				t0 = System.currentTimeMillis(); // start timer

				ndPaths = NodeDisjointPaths.getNodeDisjointPaths(prob.getCoverageGraph());
				schedule = HomogeneousSolver.schedule(prob);

				t1 = System.currentTimeMillis(); // stop timer

				Printer.printNodeDisjointPaths(ndPaths);
			}
			// Solution for heterogeneous lifetimes
			else if(args[0].equals("-ht")) {
				t0 = System.currentTimeMillis(); // start timer

				schedule = HeterogeneousSolver.schedule(prob);

				t1 = System.currentTimeMillis(); // start timer
			}

			Printer.printSchedule(schedule);
			System.out.println("computation time: " + (t1 - t0) + " ms");
		}
	}
}
