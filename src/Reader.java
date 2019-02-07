import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import sensor_networks.KBarrierCoverage;

public class Reader {
	public static KBarrierCoverage readKBarrierCoverage(String filename) throws IOException {
		Scanner scanner = new Scanner(new FileReader(".\\input\\" + filename));

		int nSensors = (int) scanner.nextFloat();
		int K = (int) scanner.nextFloat();
		float x_max = scanner.nextFloat();
		float y_max = scanner.nextFloat();
		float R = scanner.nextFloat();

		float[] x_coords = new float[nSensors];
		float[] y_coords = new float[nSensors];
		int[] lifetimes = new int[nSensors];

		for (int k = 0; k < nSensors; k++)
			x_coords[k] = scanner.nextFloat();

		for (int k = 0; k < nSensors; k++)
			y_coords[k] = scanner.nextFloat();

		for (int k = 0; k < nSensors; k++)
			lifetimes[k] = (int) scanner.nextFloat();

		KBarrierCoverage ret = new KBarrierCoverage(K, x_max, y_max, R, nSensors);
		for (int k = 0; k < nSensors; k++)
			ret.addSensor(x_coords[k], y_coords[k], lifetimes[k], k + 1);

		scanner.close();

		return ret;
	}
}
