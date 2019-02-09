package models;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ElementaryFlow {
	private double value;
	private int K;
	private Collection<List<Edge>> paths;

	public ElementaryFlow() {
		this.value = 0.0;
		this.K = 0;
		this.paths = new LinkedList<List<Edge>>();
	}
	
	public void addPath(List<Edge> path) {
		paths.add(path);
		K++;
	}
	
	public Collection<List<Edge>> getPaths() {
		return paths;
	}
	
	public Collection<Edge> getAllEdges() {
		Collection<Edge> all = new LinkedList<Edge>();
		
		for(List<Edge> path: paths)
			all.addAll(path);
		
		return all;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	public int getK() {
		return K;
	}
	
	public boolean isEmpty() {
		return paths.isEmpty();
	}
}
