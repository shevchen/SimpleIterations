import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

public class Searcher {
	private final static int ITERATIONS = 100;
	public final static String fileName = "results";

	private static final double[] xs, rs;
	private static final double maxX = 1.5, maxR = 1.5;
	private static final int itersX = 100, itersR = 10;

	static {
		xs = new double[2 * itersX + 1];
		xs[itersX] = 0;
		for (int i = 0; i < itersX; ++i) {
			xs[itersX + i + 1] = maxX / itersX * (i + 1);
			xs[itersX - i - 1] = -xs[itersX + i + 1];
		}
		rs = new double[2 * itersR + 1];
		rs[itersR] = 0;
		for (int i = 0; i < itersR; ++i) {
			rs[itersR + i + 1] = maxR / itersR * (i + 1);
			rs[itersR - i - 1] = -rs[itersR + i + 1];
		}
	}

	public static double[] getSequence(double x0, double r) {
		double[] seq = new double[ITERATIONS + 1];
		seq[0] = x0;
		for (int i = 0; i < ITERATIONS; ++i) {
			seq[i + 1] = r * seq[i] * (1 - seq[i]);
		}
		return seq;
	}

	public static void main(String[] args) throws FileNotFoundException {		
		PrintWriter res = new PrintWriter(new File(fileName));
		Locale.setDefault(Locale.US);
		for (double r : rs) {
			for (double x0 : xs) {
				System.err.println("r = " + r + ", x0 = " + x0);
				double[] seq = getSequence(x0, r);
				res.printf("r = %5f, x0 = %5f", r, x0);
				res.println();
				for (double d : seq) {
					res.print(d + " ");
				}
				res.println();
				res.println();
			}
		}
		res.close();
	}
}
