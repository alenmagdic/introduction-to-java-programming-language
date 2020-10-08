package hr.fer.zemris.java.hw13.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * Class that contains a static method for generating a pie chart and writing it
 * as a png image to the specified output stream. It can also generate a pie
 * chart without the specified data, but instead using some default data about
 * OS usage.
 *
 * @author Alen Magdić
 *
 */
public class PieChartGenerator {

	/**
	 * Generates the specified pie chart and writes it as a png image to the
	 * specified output stream. The chart is specified by a map containing
	 * element names and their shares and by the chart title.
	 *
	 * @param chartData
	 *            a map containg pie chart elements as keys and their shares as
	 *            values
	 * @param chartTitle
	 *            chart title
	 * @param outputStream
	 *            output stream that will be used for writing the generated
	 *            chart as png image
	 * @throws IOException
	 *             if there is a problem writing the generated png image
	 */
	public static void generateChart(Map<String, Integer> chartData, String chartTitle, OutputStream outputStream)
			throws IOException {
		PieDataset dataset = createDataset(chartData);
		JFreeChart chart = createChart(dataset, chartTitle);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ChartUtilities.writeChartAsPNG(bos, chart, 600, 500);
			outputStream.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputStream.flush();
	}

	/**
	 * Creates a model of pie chart (modeled as a {@link JFreeChart} object)
	 * specified by the specified dataset and title.
	 *
	 * @param dataset
	 *            chart dataset
	 * @param title
	 *            chart title
	 * @return a model of pie chart, i.e. a {@link JFreeChart} object
	 */
	private static JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(1f);
		return chart;
	}

	/**
	 * Creates a pie dataset using the specified map of chart values. The map
	 * contains chart pie elements as keys and their shares as values.
	 *
	 * @param values
	 *            a map containing chart pie elements as keys and their shares
	 *            as values
	 * @return a pie dataset
	 */
	private static PieDataset createDataset(Map<String, Integer> values) {
		DefaultPieDataset result = new DefaultPieDataset();

		if (values == null) {
			result.setValue("Linux", 29);
			result.setValue("Mac", 20);
			result.setValue("Windows", 51);
			return result;
		}

		for (Entry<String, Integer> entry : values.entrySet()) {
			result.setValue(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
