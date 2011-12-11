import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	private static double[] seq, spiralx, spiraly, xs, fi, ys, zero;
	private static JButton start, stop;
	private static JFormattedTextField fieldr, fieldx0;
	private static volatile double curr = 2.5, curx0 = 0.3;
	private static JFreeChart chart;
	private static volatile boolean stopped = true;
	private static volatile Thread runningThread;
	private static PropertyChangeListener listener;

	private static double getFi(double r, double x) {
		return r * x * (1 - x);
	}

	private static double[] getSequence() {
		double[] seq = new double[ITERATIONS + 1];
		seq[0] = curx0;
		for (int i = 0; i < ITERATIONS; ++i) {
			seq[i + 1] = getFi(curr, seq[i]);
		}
		return seq;
	}

	private static boolean abort(Thread curThread) {
		return stopped || runningThread != curThread;
	}

	private static void proc() {
		Thread curThread = Thread.currentThread();
		runningThread = curThread;
		for (int i = 0; i < PRECISION; ++i) {
			if (abort(curThread)) {
				return;
			}
			fi[i] = getFi(curr, xs[i]);
		}
		chart.fireChartChanged();
		seq = getSequence();
		spiralx[0] = spiralx[1] = curx0;
		spiraly[0] = spiraly[1] = 0;
		chart.fireChartChanged();
		System.out.println(Arrays.toString(seq));
		for (int i = 0; i < ITERATIONS; ++i) {
			if (abort(curThread)) {
				break;
			}
			spiralx[0] = spiralx[1];
			spiraly[0] = spiraly[1];
			spiraly[1] = seq[i + 1];
			chart.fireChartChanged();
			try {
				Thread.sleep(PAUSE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (abort(curThread)) {
				break;
			}
			spiralx[0] = spiralx[1];
			spiraly[0] = spiraly[1];
			spiralx[1] = seq[i + 1];
			chart.fireChartChanged();
			try {
				Thread.sleep(PAUSE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (stopped) {
			spiralx[0] = spiralx[1] = curx0;
			spiraly[0] = spiraly[1] = 0;
			chart.fireChartChanged();
		}
	}

	public static void createFrame() {
		YMAX = Math.max(0.3 * curr, 1.5);
		double[] sequence = getSequence();
		String title = "Сходимость f(x) при r=" + curr
				+ " и начальном приближении x=" + sequence[0];
		String xLabel = "x";
		String yLabel = "y";
		DefaultXYDataset data = new DefaultXYDataset();
		fi = new double[PRECISION];
		xs = new double[PRECISION];
		ys = new double[PRECISION];
		zero = new double[PRECISION];
		for (int i = 0; i < PRECISION; ++i) {
			xs[i] = XMIN + (XMAX - XMIN) * i / (PRECISION - 1);
			ys[i] = YMIN + (YMAX - YMIN) * i / (PRECISION - 1);
			zero[i] = 0.;
		}
		data.addSeries("y(x)=rx(1-x)", new double[][] { xs, fi });
		data.addSeries("y(x)=x", new double[][] { xs, xs });
		data.addSeries("domain axis", new double[][] { xs, zero });
		data.addSeries("value axis", new double[][] { zero, ys });
		spiralx = new double[] { curx0, curx0 };
		spiraly = new double[] { 0, 0 };
		data.addSeries("spiral", new double[][] { spiralx, spiraly });
		chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, data,
				PlotOrientation.VERTICAL, true, false, false);
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
		start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopped = false;
				new Thread(new Runnable() {
					@Override
					public void run() {
						proc();
					}
				}).start();
			}
		});
		stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopped = true;
			}
		});
		listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Object source = evt.getSource();
				if (source == fieldr) {
					try {
						double r = Double.parseDouble(fieldr.getText());
						if (r != curr) {
							stopped = true;
							curr = r;
						}
					} catch (NumberFormatException e) {
						fieldr.setValue(curr);
					}
				} else if (source == fieldx0) {
					try {
						double x0 = Double.parseDouble(fieldx0.getText());
						if (x0 != curx0) {
							stopped = true;
							curx0 = x0;
						}
					} catch (NumberFormatException e) {
						fieldx0.setValue(curx0);
					}
				}
			}
		};
		fieldr = new JFormattedTextField(NumberFormat
				.getNumberInstance(Locale.US));
		fieldr.setValue(curr);
		fieldr.setPreferredSize(new Dimension(50, 20));
		fieldr.addPropertyChangeListener(listener);
		fieldx0 = new JFormattedTextField(NumberFormat
				.getNumberInstance(Locale.US));
		fieldx0.setValue(curx0);
		fieldx0.setPreferredSize(new Dimension(50, 20));
		fieldx0.addPropertyChangeListener(listener);
		JPanel menuPanel = new JPanel();
		menuPanel.add(start);
		menuPanel.add(stop);
		menuPanel.add(new JLabel("r ="));
		menuPanel.add(fieldr);
		menuPanel.add(new JLabel("x0 ="));
		menuPanel.add(fieldx0);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(new ChartPanel(chart));
		mainPanel.add(menuPanel);
		JFrame frame = new JFrame();
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		createFrame();
	}
}
