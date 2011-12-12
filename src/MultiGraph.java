import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.*;

import javax.swing.JFrame;

public class MultiGraph {

	private static final class Pixmap extends Panel {
		private final BufferedImage image;
		private static boolean oscill;

		private final static int MAXSTEPS = 1000;
		private final static double EPS = 1e-6;

		private static Color getIteration(double x0, double r) {
			double curX = x0;
			double nextX = x0;
			int step = 0;
			double xfirst = x0, xsecond = x0;
			do {
				step++;
				if (step > MAXSTEPS) {
					break;
				}
				curX = nextX;
				nextX = r * curX * (1 - curX);
				if (Math.abs(nextX - curX) > EPS) {
					xfirst = xsecond;
					xsecond = nextX;
				}
			} while ((Math.abs(nextX - curX) > EPS)
					&& (nextX != Double.POSITIVE_INFINITY)
					&& (nextX != Double.NEGATIVE_INFINITY));
			if (Math.abs(nextX - curX) <= EPS) {
				oscill = xfirst > nextX != xsecond > nextX;
			} else {
				oscill = false;
			}
			return getColor(nextX, r, step);
		}

		private static Color getColor(double X, double r, int steps) {
			if (X == Double.POSITIVE_INFINITY)
				return Color.WHITE;
			if (X == Double.NEGATIVE_INFINITY)
				return Color.BLACK;
			int part = (int) ((1. - 0.8 * Math.log(steps) / Math.log(MAXSTEPS)) * 255);
			if (Math.abs(X - r * X * (1 - X)) < EPS) {
				if (Math.abs(X) < 1e-2) {
					return oscill ? new Color(part, part / 2, 0) : new Color(
							part, part, 0);
				}
				return oscill ? new Color(0, 0, part) : new Color(0, part, 0);
			}
			return Color.GRAY;
		}

		public Pixmap(double x1, double x2, double r1, double r2) {
			image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
			double dr = (r2 - r1) / 800;
			double dx = (x2 - x1) / 600;
			double r = r1;
			for (int x = 0; x < 800; x++) {
				double x0 = x1;
				for (int y = 0; y < 600; y++) {
					image.setRGB(x, y, getIteration(x0, r).getRGB());
					x0 += dx;
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
		frame.getContentPane().add(new Pixmap(-3, 3, -5, 5));
		frame.setSize(800, 630);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
