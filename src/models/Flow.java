package models;

import java.util.HashMap;

public class Flow {
	private double value;
	private HashMap<Edge, Double> map;
	
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
