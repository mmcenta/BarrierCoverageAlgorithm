import java.util.*;

import BarrierCoverage.KBarrierCoverage;
import algorithms.MaxFlow;
import algorithms.MaxKRouteFlow;
import models.Node;
import models.FlowNetwork;
import models.Graph;

public class Main {
	static void printMatrix(float[][] m) {
		System.out.println();
		System.out.print("   ");
		for(int j=0; j < m[0].length; j++)
			System.out.format("  %2d", j);
		System.out.println();
		for(int i=0; i < m.length; i++) {
			System.out.format("%2d: ", i);
			for(int j=0; j < m[0].length; j++)
				if(m[i][j] != -1)
					System.out.format("%3f ", m[i][j]);
				else
					System.out.print("--- ");
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		FlowNetwork nw = new FlowNetwork(6);
		nw.addEdge(0, 1, 2);
		nw.addEdge(0, 2, 2);
		nw.addEdge(1, 2, 7);
		nw.addEdge(1, 3, 3);
		nw.addEdge(2, 4, 4);
		nw.addEdge(3, 5, 2);
		nw.addEdge(4, 3, 13);
		nw.addEdge(4, 5, 2);
		
		MaxKRouteFlow mkf = new MaxKRouteFlow(nw, 2);
		MaxFlow mf = new MaxFlow(nw);
		for(float p=(float) 0.01; p <= 20; p += 0.01) {
			mf.setFlowNetwork(mkf.getMaxBoundedFlowNetwork(p));
			float F_p = mf.getMaxFlowValue();
			float phi = F_p - 2*p;
			System.out.format("%f: (%f, %f) ", p, F_p, phi);
		}
		System.out.println();
		System.out.println(mkf.getMaxFlowValue());
		printMatrix(mkf.getMaxFlow());
	}
}
