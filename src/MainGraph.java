import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;

public class MainGraph {	
	public static void createFrame() {
		DefaultXYDataset data = new DefaultXYDataset();

		JFreeChart chart = ChartFactory.createScatterPlot("asd", "x", "y",
				data, PlotOrientation.VERTICAL, true, false, false);
		XYItemRenderer renderer = chart.getXYPlot().getRenderer();
		renderer.setSeriesPaint(0, Color.RED);
		Shape rect = new Rectangle(5, 5);
		renderer.setSeriesShape(0, rect);
		JFrame frame = new JFrame();
		frame.add(new ChartPanel(chart));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
