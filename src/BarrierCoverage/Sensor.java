package BarrierCoverage;

import java.util.Comparator;

public class Sensor {
	public float x, y;
	int durability;
	int number;  // the number of this sensor on the input
	
	public Sensor(float x, float y, int durability) {
		this.x = x;
		this.y = y;
		this.durability = durability;
	}
	
	public void setNumber(int num) {
		this.number = num;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public static float getDistance(Sensor s1, Sensor s2) {
		return (float) Math.sqrt(Math.pow(s1.x-s2.x, 2) + Math.pow(s1.y-s2.y, 2));
	}
	
	public static Comparator<Sensor> xCoordCompare = new Comparator<Sensor>() {
		public int compare(Sensor sensor1, Sensor sensor2) { 
			return (int) (sensor1.x - sensor2.x);
		}
	};
}
