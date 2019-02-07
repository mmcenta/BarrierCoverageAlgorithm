import java.io.IOException;
import java.util.*;

import algorithms.*;
import models.*;
import sensor_networks.*;
import test.MaxFlowTest;

public class Main {
	public static void main(String[] args) {
//		long t0 = System.currentTimeMillis();
//		
//		KBarrierCoverage kbar = null;
//		try {
//			kbar = Reader.readKBarrierCoverage("sensornetwork0.doc");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		List<ScheduleInterval> schedule = kbar.homogenousScheduling();
//		
//		SchedulePrinter.printToConsole(schedule);
//		
//		long t1 = System.currentTimeMillis();
//		
//		System.out.println("Time: "+(t1-t0)+" ms");

		FlowNetwork nw = new FlowNetwork(4);
		nw.addEdge(new Edge(0, 1), 2);
		nw.addEdge(new Edge(0, 2), 3);
		nw.addEdge(new Edge(0, 3), 5);
		nw.addEdge(new Edge(1, 2), 1);
		nw.addEdge(new Edge(2, 3), 5);
		nw.addEdge(new Edge(3, 1), 2);

		long t0 = System.currentTimeMillis();

		Flow kMax = MaxKRouteFlow.getFlow(2, nw);
		double root = kMax.getValue() / 2;

		long t1 = System.currentTimeMillis();
		System.out.println("Time MaxKRouteFlow : " + (t1 - t0) + " ms.");

		System.out.println("root = " + root);
		System.out.println("flow falue = " + kMax.getValue());
		System.out.println("flow : " + kMax.getMap());

		System.out.println();

		t0 = System.currentTimeMillis();

		Collection<ElementaryFlow> elemFlows = KRouteDecomposition.decompose(2, kMax, nw);

		t1 = System.currentTimeMillis();
		System.out.println("Time KRouteDecomposition : " + (t1 - t0) + " ms.");

		for (ElementaryFlow kf : elemFlows) {
			System.out.println("flow value = " + kf.getValue());
			System.out.println("flow edges : " + kf.getAllEdges());
		}

	}
}
