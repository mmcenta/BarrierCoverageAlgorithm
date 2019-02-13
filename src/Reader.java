import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import sensor_networks.KBarrierCoverageInstance;

public class Reader {
	// Reader class that reads a file in the proper format and creates an instance of the K-Barrier Coverage Problem
	public static KBarrierCoverageInstance readKBarrierCoverage(String filename) throws IOException {
		Scanner scanner = new Scanner(new FileReader(".\\input\\" + filename));

		int nSensors = (int) scanner.nextDouble(); // read the number of sensors
		int K = (int) scanner.nextDouble(); // read the constant K
		double x_max = scanner.nextDouble(); // read the x_max (x coordinate of the border of the region)
		double y_max = scanner.nextDouble(); // read the y_max (y coordinate of the border of the region)
		double R = scanner.nextDouble(); // read R (the radius of coverage of eah sensor) 

		double[] x_coords = new double[nSensors];
		double[] y_coords = new double[nSensors];
		int[] lifetimes = new int[nSensors+2];

		// Read the x coord of the sensors
		for (int k = 0; k < nSensors; k++)
			x_coords[k] = scanner.nextDouble();

		// Read the y coord of the sensors
		for (int k = 0; k < nSensors; k++)
			y_coords[k] = scanner.nextDouble();

		// Read the lifetimes of the sensors
		for (int k = 0; k < nSensors+2; k++)
			lifetimes[k] = (int) scanner.nextDouble(); // ignore the first and last lifetimes

		
		// Create the problem instance
		KBarrierCoverageInstance ret = new KBarrierCoverageInstance(K, x_max, y_max, R, nSensors);
		for (int k = 0; k < nSensors; k++)
			ret.addSensor(x_coords[k], y_coords[k], lifetimes[k+1], k+1);

		scanner.close();

		return ret;
	}
}
