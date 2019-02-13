package models;

import java.util.HashMap;

public class Flow {
	// Represents a flow in a flow network.
	private double value; // the value of this flow (total flow received/sent)
	private HashMap<Edge, Double> map; // the flow at each edge
	
	public Flow(HashMap<Edge, Double> map, double value) {
		this.map = map;
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public HashMap<Edge, Double> getMap() {
		return map;
	}
}
