package sensor_networks;

public class Sensor {
	// Represents a sensor
	public final double x, y; // its position
	public final int lifetime; // its lifetime
	private int number;  // the number of this sensor on the input
	
	public Sensor(double x, double y, int lifetime, int number) {
		this.x = x;
		this.y = y;
		this.lifetime = lifetime;
		this.number = number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public static double getDistance(Sensor s1, Sensor s2) {
		return Math.sqrt(Math.pow(s1.x-s2.x, 2) + Math.pow(s1.y-s2.y, 2));
	}
}
