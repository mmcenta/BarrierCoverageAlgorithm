import java.util.*;

import MaximumFlow.FlowNetwork;
import MaximumFlow.MaxFlowSolver;
import models.Node;
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
		ArrayList<Integer> t = new ArrayList<Integer>(3);
		
		t.add(3);
		t.add(2);
		t.add(1);
		System.out.println(t);
		
		ArrayList<Integer> t_shallow = new ArrayList<Integer>(t);
		Collections.sort(t_shallow);
		System.out.println(t_shallow);
		
		/*
		Graph g = new Graph(6);
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 2);
		g.addEdge(1, 3);
		g.addEdge(2, 4);
		g.addEdge(3, 5);
		g.addEdge(4, 5);
		
		FlowNetwork nw = new FlowNetwork(g);
		nw.setCapacity(0, 1, 1);
		nw.setCapacity(0, 2, 1);
		nw.setCapacity(1, 2, 1);
		nw.setCapacity(1, 3, 1);
		nw.setCapacity(2, 4, 1);
		nw.setCapacity(3, 5, 1);
		nw.setCapacity(4, 5, 1);
		
		MaxFlowSolver mf = new MaxFlowSolver(nw);
		float max = mf.getMaxFlow();
		float[][] flow = mf.getFlow();
		
		System.out.println(max);
		printMatrix(flow);
		*/
	}

}
