public class Main {
	private final static int ITERATIONS = 100;

	public static void main(String[] args) {
		double[] test = new double[ITERATIONS];
		for (int i = 0; i < 100; ++i) {
			test[i] = 1. * i * i;
		}
		Vis.createFrame(test, 1.5);
	}
}
