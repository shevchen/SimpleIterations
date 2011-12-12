import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.*;
import java.util.LinkedList;

import javax.swing.JFrame;

public class Convergence {

	private static final class Pixmap2 extends Panel {
		private final BufferedImage image;
		private static LinkedList<Double> roots;

		private final static int MAXSTEPS = 10000;
		private final static double EPS = 1e-6;
		private final static double x0 = 0.2;
		private static double[] seq;

		private static void getRoots(double r) {
			seq[0] = x0;
			double curX = x0;
			double nextX = x0;
			int step = 0;
			do {
				if (step == MAXSTEPS) {
					break;
				}
				++step;
				curX = nextX;
				nextX = r * curX * (1 - curX);
				seq[step] = nextX;
			} while ((Math.abs(nextX - curX) > EPS)
					&& (nextX != Double.POSITIVE_INFINITY)
					&& (nextX != Double.NEGATIVE_INFINITY));
			nextX = seq[step];
			roots.addFirst(nextX);
			if (step == 0) {
				return;
			}
			curX = seq[--step];
			while (step > 0 && Math.abs(curX - nextX) > EPS) {
				roots.addFirst(curX);
				curX = seq[--step];
			}
		}

		public Pixmap2(double r1, double r2) {
			seq = new double[MAXSTEPS + 1];
			roots = new LinkedList<Double>();
			image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < 800; ++i) {
				for (int j = 0; j < 600; ++j) {
					image.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
			double dr = (r2 - r1) / 800;
			double r = r1;
			for (int x = 0; x < 800; x++) {
				getRoots(r);
				while (!roots.isEmpty()) {
					double croot = roots.removeFirst();
					int y = 300 - (int) (croot * 300);
					if (y < 0) {
						y = 0;
					}
					if (y >= 600) {
						y = 599;
					}
					image.setRGB(x, y, Color.RED.getRGB());
				}
				r += dr;
			}
		}

		public void paint(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Results");
		frame.getContentPane().add(new Pixmap2(-2, 4));
		frame.setSize(800, 630);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
