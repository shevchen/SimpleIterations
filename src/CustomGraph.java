import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;

public class CustomGraph {
	private static final double XMIN = -0.5, XMAX = 1.1, YMIN = -1.;
	private static double YMAX;
	private static final int PRECISION = 500;
	private static final int ITERATIONS = 10000;
	private static long PAUSE = 1000;

	private static double getFi(double r, double x) {
		return r * x * (1 - x);
	}

	private static double[] getSequence(double r, double x0) {
		double[] seq = new double[ITERATIONS + 1];
		seq[0] = x0;
		for (int i = 0; i < ITERATIONS; ++i) {
			seq[i + 1] = getFi(r, seq[i]);
		}
		return seq;
	}

	public static void createFrame(double r, double x0) {
		YMAX = Math.max(0.3 * r, 1.5);
		double[] sequence = getSequence(r, x0);
		String title = "Сходимость f(x) при r=" + r
				+ " и начальном приближении x=" + sequence[0];
		String xLabel = "x";
		String yLabel = "y";
		DefaultXYDataset data = new DefaultXYDataset();
		double[] fi = new double[PRECISION];
		double[] xs = new double[PRECISION];
		double[] ys = new double[PRECISION];
		double[] zero = new double[PRECISION];
		for (int i = 0; i < PRECISION; ++i) {
			xs[i] = XMIN + (XMAX - XMIN) * i / (PRECISION - 1);
			fi[i] = getFi(r, xs[i]);
			ys[i] = YMIN + (YMAX - YMIN) * i / (PRECISION - 1);
			zero[i] = 0.;
		}
		data.addSeries("y(x)=rx(1-x)", new double[][] { xs, fi });
		data.addSeries("y(x)=x", new double[][] { xs, xs });
		data.addSeries("domain axis", new double[][] { xs, zero });
		data.addSeries("value axis", new double[][] { zero, ys });
		double[] spiralx = { x0, x0 };
		double[] spiraly = { 0, 0 };
		data.addSeries("spiral", new double[][] { spiralx, spiraly });
		JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel,
				yLabel, data, PlotOrientation.VERTICAL, true, false, false);
		XYItemRenderer ren = chart.getXYPlot().getRenderer();
		for (int i = 0; i < data.getSeriesCount(); ++i) {
			ren.setSeriesStroke(i, new BasicStroke(i == 4 ? 2.f : 1.f,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		}
		ren.setSeriesPaint(0, Color.BLUE);
		ren.setSeriesPaint(1, Color.GRAY);
		ren.setSeriesPaint(2, Color.BLACK);
		ren.setSeriesPaint(3, Color.BLACK);
		ren.setSeriesPaint(4, Color.RED);
		chart.getXYPlot().getDomainAxis().setPositiveArrowVisible(true);
		chart.getXYPlot().getRangeAxis().setPositiveArrowVisible(true);
		JFrame frame = new JFrame();
		frame.add(new ChartPanel(chart));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		double[] seq = getSequence(r, x0);
		System.out.println(Arrays.toString(seq));
		try {
			Thread.sleep(PAUSE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.setVisible(true);
		for (int i = 0; i < ITERATIONS; ++i) {
			spiralx[0] = spiralx[1];
			spiraly[0] = spiraly[1];
			spiraly[1] = seq[i + 1];
			try {
				Thread.sleep(PAUSE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			chart.fireChartChanged();
			spiralx[0] = spiralx[1];
			spiraly[0] = spiraly[1];
			spiralx[1] = seq[i + 1];
			try {
				Thread.sleep(PAUSE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			chart.fireChartChanged();
		}
	}

	public static void main(String[] args) {
		createFrame(2.1, 0.2); // r, x0
	}
}
