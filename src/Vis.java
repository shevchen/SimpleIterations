import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;

public class Vis {
	public static void createFrame(double[] sequence, double r) {
		String title = "Simple iterations";
		String xLabel = "iteration";
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
		ren.setSeriesStroke(0, new BasicStroke(2.f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		ren.setSeriesPaint(0, Color.BLACK);
		JFrame frame = new JFrame();
		frame.add(new ChartPanel(chart));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
