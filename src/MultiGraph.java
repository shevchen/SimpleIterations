import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.*;

import javax.swing.JFrame;
import javax.swing.JPanel;



public final class MultiGraph {
	
	private static final class Pixmap extends Panel {
		private final BufferedImage image;		
		
		private static double getIteration(double x0, double r) {
			double curX;
			double nextX = x0;
			int step = 0;
			do {
				step++;
				curX = nextX;
				nextX = r * curX * (1 - curX);
			} while ((Math.abs(nextX - curX) > 1e-6) && 
					(nextX != Double.POSITIVE_INFINITY) && (nextX != Double.NEGATIVE_INFINITY));
			return nextX;
		}

		private static Color getColor(double X, double r) {
			if (X == Double.POSITIVE_INFINITY) return Color.red;
			if (X == Double.NEGATIVE_INFINITY) return Color.yellow;
			if (Math.abs(X - r * X * (1 - X)) < 1e-6) return Color.GREEN;
			return Color.BLACK;
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
					image.setRGB(x, y, getColor(getIteration(x0, r), r).getRGB());
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
		frame.getContentPane().add(new Pixmap(-1, 2, -5, 5));
		frame.setSize(816, 638);
		frame.setBounds(250, 50, 816, 638);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
