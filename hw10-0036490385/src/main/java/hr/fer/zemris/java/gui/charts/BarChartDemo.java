package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This program is a demonstration of {@link BarChartComponent}. It shows the
 * specified bar chart. The bar chart by a command line argument determining the
 * path to the text file containing all specifications about the bar chart that
 * is to be drawn on the window of this program. That text file has to contain
 * x-axis description in the first row, then y-axis description in the next row,
 * then pairs of values (every pair is separated by a space, while pairs are
 * written following the pattern "x,y"), then the minimum y-value that should be
 * shown on y-axis, then the maximum y-value, and finally the gap between the
 * closest marked y-axis values.
 *
 * @author Alen Magdić
 *
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected exactly one argument. Number of arguments given: " + args.length);
			return;
		}

		Path filePath = Paths.get(args[0]);
		if (!Files.exists(filePath)) {
			System.out.println("The specified file does not exist. File: " + filePath);
			return;
		} else if (Files.isDirectory(filePath)) {
			System.out.println("The specified path represents a directory, expected path to a file. Path: " + filePath);
			return;
		}

		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(filePath);
		} catch (IOException e) {
			System.out.println("Unable to open the specified file. File: " + filePath);
			return;
		}

		BarChart barChart;
		try {
			barChart = readAndGenerateBarChart(reader);
		} catch (Exception e) {
			System.out.println("The specified file contains invalid data or it could not be read.");
			return;
		}

		SwingUtilities.invokeLater(
				() -> new BarChartDemo(barChart, filePath.normalize().toAbsolutePath().toString()).setVisible(true));
	}

	/**
	 * Reads the content of the file that specifies the bar chart that is to be
	 * drawn and generates an object encapsulation that data. It throws
	 * IOException if there is a problem with reading the file.
	 *
	 * @param reader
	 *            a reader used to read the specified file
	 * @return an object representing a bar chart specified in the file
	 * @throws IOException
	 *             thrown when there is a problem with reading the file
	 */
	private static BarChart readAndGenerateBarChart(BufferedReader reader) throws IOException {
		String xAxisName = reader.readLine().trim();
		String yAxisName = reader.readLine().trim();
		String data = reader.readLine().trim();
		String yMinString = reader.readLine().trim();
		String yMaxString = reader.readLine().trim();
		String yGapString = reader.readLine().trim();

		int yMin = Integer.parseInt(yMinString);
		int yMax = Integer.parseInt(yMaxString);
		int yGap = Integer.parseInt(yGapString);

		List<XYValue> values = new ArrayList<>();
		for (String value : data.split(" ")) {
			String[] valArray = value.split(",");
			int x = Integer.parseInt(valArray[0]);
			int y = Integer.parseInt(valArray[1]);
			values.add(new XYValue(x, y));
		}

		return new BarChart(values, xAxisName, yAxisName, yMin, yMax, yGap);
	}

	/**
	 * Constructor.
	 *
	 * @param barChart
	 *            an object containing information about a bar chart that is to
	 *            be drawn
	 * @param filePath
	 *            path to the file containing all the chart information
	 */
	public BarChartDemo(BarChart barChart, String filePath) {

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar chart");

		initGUI(barChart, filePath);

		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Initializes the graphical user interface.
	 *
	 * @param barChart
	 *            an object containing information about a bar chart that is to
	 *            be drawn
	 * @param filePath
	 *            path to the file containing all the chart information
	 */
	private void initGUI(BarChart barChart, String filePath) {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		BarChartComponent bc = new BarChartComponent(barChart);
		bc.setBackground(Color.WHITE);
		bc.setForeground(Color.RED);
		cp.add(bc, BorderLayout.CENTER);

		JLabel pathLB = new JLabel(filePath);
		JPanel pathPanel = new JPanel();
		pathPanel.add(pathLB, BorderLayout.CENTER);
		cp.add(pathPanel, BorderLayout.PAGE_START);
	}
}
