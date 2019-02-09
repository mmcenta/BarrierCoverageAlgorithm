import java.io.IOException;
import java.util.*;

import algorithms.*;
import models.*;
import sensor_networks.*;

@SuppressWarnings("unused")
public class Main {
	public static void main(String[] args) {		
		// Read the input file
		KBarrierCoverageInstance prob = null;
		try {
			prob = Reader.readKBarrierCoverage("sensornetwork1.doc");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long t0 = System.currentTimeMillis(); // start timer
		
		Collection<List<Integer>> ndPaths = NodeDisjointPaths.getNodeDisjointPaths(prob.getCoverageGraph());
		Map<List<Integer>, Integer> values = HeterogeneousSolver.getValuesFromPaths(ndPaths, prob);
		List<ScheduleInterval> schedule = HeterogeneousSolver.schedule(prob);
		
		long t1 = System.currentTimeMillis(); // stop timer
		
		ResultPrinter.printNodeDisjointPaths(ndPaths, values);
		ResultPrinter.printSchedule(schedule);
		System.out.println("computation time: "+(t1-t0)+" ms");
	}
}
