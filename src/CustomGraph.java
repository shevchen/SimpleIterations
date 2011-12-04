import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;

public class CustomGraph {
	public static void createFrame(double[] sequence, double r) {
		String title = "График сходимости f(x) при r = " + r
				+ " и начальном приближении x = " + sequence[0];
		String xLabel = "номер итерации";
		String yLabel = "f(x)";
		DefaultXYDataset data = new DefaultXYDataset();
		double[] absc = new double[sequence.length];
		for (int i = 0; i < absc.length; ++i) {
			absc[i] = i;
		}
		data.addSeries("r = " + r, new double[][] { absc, sequence });
		JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel,
				yLabel, data, PlotOrientation.VERTICAL, true, false, false);
		XYItemRenderer ren = chart.getXYPlot().getRenderer();
		ren.setSeriesStroke(0, new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		ren.setSeriesPaint(0, Color.RED);
		JFrame frame = new JFrame();
		frame.add(new ChartPanel(chart));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
}
