package sensor_networks;

import java.util.Arrays;

import models.Graph;

public class KBarrierCoverageInstance {
	// Represents an instance of the K-Barrier Coverage Problem
	public final double x_max, y_max, R;
	public final int K;
	public int nSensors; // the number of sensors
	private int left, right; // the number associated to the left and right walls
	private Sensor[] sensors; // a array storing information on the sensors

	public KBarrierCoverageInstance(int K, double x_max, double y_max, double R, int nSensors) {
		this.x_max = x_max;
		this.y_max = y_max;
		this.R = R;

		this.K = K;
		this.nSensors = 0;

		this.sensors = new Sensor[nSensors+2];
		
		// As a convention, the left wall sensor is numbered 0
		this.left = 0;
		sensors[left] = new Sensor(0, 0, Integer.MAX_VALUE, left);
		
		// And the right wall sensor is numbered nSensors + 1
		this.right = nSensors+1;
		sensors[right] = new Sensor(x_max, 0, Integer.MAX_VALUE, right);
	}

	public void addSensor(double x, double y, int durability, int number) {
		sensors[number] = new Sensor(x, y, durability, number);
		nSensors++;
	}

	public Sensor getSensor(int number) {
		return sensors[number];
	}
	
	private Sensor[] sortedSensors() {
		Sensor[] ans = Arrays.copyOf(sensors, sensors.length);
		Arrays.sort(ans, new SensorComparator());
		
		return ans;
	}

	public Graph getCoverageGraph() {
		int n = nSensors + 2; // number of vertices on the coverage graph (counting left and right wall)

		// Sort the sensors according to their x-coordinate
		Sensor[] sorted = sortedSensors();
		
		// Create coverage graph
		Graph coverage = new Graph(n);

		// Traverse the array of sensors, creating the necessary edges
		for(int k = 1; k <= nSensors; k++) {
			Sensor s = sorted[k];
			int numS = s.getNumber();

			if(s.x - sensors[left].x < R)
				// if the sensor is close (< 2R) to the left wall, connect the two
				coverage.addEdge(left, numS);

			if(sensors[right].x - s.x < R)
				// if the sensor is close (< 2R) to the right wall, connect the two
				coverage.addEdge(numS, right);

			// Check the sensors to the left of sensor until they are too far away
			for(int leftIdx = k-1; leftIdx > 0; leftIdx--) {
				Sensor leftSensor = sorted[leftIdx];

				if (s.x - leftSensor.x < 2*R && Sensor.getDistance(s, leftSensor) < 2*R)
					// if they are close (< 2R), connect them
					coverage.addEdge(numS, leftSensor.getNumber());

				else if (s.x - leftSensor.x > 2*R)
					// if the sensors are too far away to the left, stop checking
					break;
			}
			// Check the sensors to the right of sensor until they are too far away
			for(int rightIdx = k + 1; rightIdx <= nSensors; rightIdx++) {
				Sensor rightSensor = sorted[rightIdx];

				if(rightSensor.x - s.x < 2*R && Sensor.getDistance(s, rightSensor) < 2*R)
					// if they are close (< 2R), connect them
					coverage.addEdge(numS, rightSensor.getNumber());

				else if(rightSensor.x - s.x > 2*R)
					// if the sensors are too far away to the right, stop checking
					break;
			}
		}
		
		return coverage;
	}
}
