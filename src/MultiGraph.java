import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.*;

import javax.swing.JFrame;
import javax.swing.JPanel;



public final class MultiGraph {
	
	private static final class Pixmap extends Panel {
		private final BufferedImage image;		
		private static final double PRECISION = 1e-12;
		private static final int MAX_ITERATION = 100;
		
		private static int getIteration(double x0, double r) {
			double curX;
			double nextX = x0;
			int step = 0;
			do {
				step++;
				curX = nextX;
				nextX = r * curX * (1 - curX);
			} while ((Math.abs(nextX - curX) > 0.0) && (step < MAX_ITERATION));
			return step;
		}

		private static Color getColor(double percent) {
			if (percent > 0.35) return Color.red;
			if (percent > 0.3) return Color.orange;
			if (percent > 0.25) return Color.yellow;
			if (percent > 0.2) return Color.blue;
			return Color.green;
		}
		public Pixmap(double x1, double x2, double r1, double r2) {
			image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
			double dx = (x2 - x1) / 800;
			double dy = (r2 - r1) / 600;
			double r = r2;
			int maxStep = 0;
			for (int x = 0; x < 800; x++) {
				double x0 = x1;
				for (int y = 0; y < 600; y++) {
					int step = getIteration(x0, r);
					maxStep = (step > maxStep) ? step : maxStep;
					x0 += dx;
				}
				r2 -= dy;
			}
			System.out.println(maxStep);
			for (int x = 0; x < 800; x++) {
				double x0 = x1;
				for (int y = 0; y < 600; y++) {
					int step = getIteration(x0, r);
					image.setRGB(x, y, getColor(1. * step / maxStep).getRGB());
					x0 += dx;
				}
				r2 -= dy;
			}
		}
		
		public void paint(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Results");
		frame.getContentPane().add(new Pixmap(-2, 2, -1, 1));
		frame.setSize(816, 638);
		frame.setBounds(250, 50, 816, 638);
		frame.setVisible(true);
	}
}
