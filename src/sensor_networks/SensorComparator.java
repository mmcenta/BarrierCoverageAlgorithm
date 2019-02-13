package sensor_networks;

import java.util.Comparator;

public class SensorComparator implements Comparator<Sensor> {
	// Compares sensors by their x-coordinate
	@Override
	public int compare(Sensor sensor1, Sensor sensor2) {
		if(sensor1.x-sensor2.x > 0)
			return 1;
		else if(sensor1.x-sensor2.x < 0)
			return -1;
		return 0;
	}
}
