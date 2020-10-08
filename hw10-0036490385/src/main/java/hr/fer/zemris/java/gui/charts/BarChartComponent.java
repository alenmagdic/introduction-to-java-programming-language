package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JComponent;

/**
 * This class represents a bar chart component. It uses a data encapsulated as
 * {@link BarChart} object to draw a bar chart.
 *
 * @author Alen Magdić
 *
 */
public class BarChartComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	/**
	 * An object that contains all information about the chart that this
	 * component represents.
	 **/
	private BarChart barChart;
	/**
	 * Distance of the component picture to the edge of the component itself.
	 **/
	private static final int DISTANCE_FROM_EDGE = 20;
	/** Length of the part of an axis that comes after the maximum value. **/
	private static final int LENGTH_AFTER_MAX_VALUE = 10;
	/** A gap between the axis name and the labels representing axis values. **/
	private static final int GAP_BETWEEN_AXIS_NAME_AND_VALUES = 20;
	/** A gap between an axis and the labels representing axis values. **/
	private static final int GAP_BETWEEN_VALUES_AND_AXIS = 10;
	/** Font size **/
	private static final int FONT_SIZE = 12;
	/** Preferred size of the component **/
	private static final Dimension PREFERRED_SIZE = new Dimension(800, 600);

	/**
	 * Constructor.
	 *
	 * @param barChart
	 *            an object that contains information about the bar chart that
	 *            is to be represented by this component
	 */
	public BarChartComponent(BarChart barChart) {
		super();
		this.barChart = barChart;
		this.setOpaque(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Dimension dim = getSize();
		Insets ins = getInsets();

		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(ins.left, ins.top, dim.width - ins.left - ins.right, dim.height - ins.top - ins.bottom);
		}

		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, FONT_SIZE));
		FontMetrics fontMet = g.getFontMetrics();

		int xAxisStart = determineStartOfXAxis(fontMet);
		int yAxisStart = dim.height - ins.bottom - DISTANCE_FROM_EDGE - 2 * fontMet.getAscent()
				- GAP_BETWEEN_AXIS_NAME_AND_VALUES - GAP_BETWEEN_VALUES_AND_AXIS;

		g.drawLine(xAxisStart, yAxisStart, dim.width - DISTANCE_FROM_EDGE + LENGTH_AFTER_MAX_VALUE, yAxisStart);
		g.drawLine(xAxisStart, yAxisStart, xAxisStart, ins.top + DISTANCE_FROM_EDGE - LENGTH_AFTER_MAX_VALUE);

		drawAxisNames(g, xAxisStart, yAxisStart);

		List<XYValue> values = barChart.getValues();
		int ySegments = (barChart.getMaxY() - barChart.getMinY()) / barChart.getyGap()
				+ ((barChart.getMaxY() - barChart.getMinY()) % barChart.getyGap() != 0 ? 1 : 0);
		int xSegments = values.size();
		int xAxisLen = dim.width - DISTANCE_FROM_EDGE - xAxisStart;
		int xSegmentSize = xAxisLen / xSegments;
		int yAxisLen = yAxisStart - ins.top - DISTANCE_FROM_EDGE;
		int ySegmentSize = yAxisLen / ySegments;

		drawGrid(g, xAxisStart, yAxisStart, xSegments, ySegments, xSegmentSize, ySegmentSize);

		// drawing strokes
		g.setColor(getBackground());
		for (int i = 0; i < xSegments; i++) {
			XYValue v = values.get(i);

			int height = (int) ((v.getY() - barChart.getMinY()) / (double) barChart.getyGap() * ySegmentSize);
			g.fillRect(xAxisStart + i * xSegmentSize + 1, yAxisStart - height, xSegmentSize - 1, height);
		}

		g.setColor(getForeground());
		for (int i = 0; i < xSegments; i++) {
			XYValue v = values.get(i);

			int height = (int) ((v.getY() - barChart.getMinY()) / (double) barChart.getyGap() * ySegmentSize);
			g.fillRect(xAxisStart + i * xSegmentSize + 1, yAxisStart - height, xSegmentSize - 2, height);

		}

		drawAxisValues(g, xAxisStart, yAxisStart, xSegmentSize, ySegments, ySegmentSize);
	}

	/**
	 * Draws a grid to the background of the bar chart.
	 *
	 * @param g
	 *            object used to create graphic content, i.e. to draw and to
	 *            write to the components surface
	 * @param xAxisStart
	 *            x-coordinate of the pixel at which is the starting point of
	 *            the x axis
	 * @param yAxisStart
	 *            y-coordinate of the pixel at which is the starting point of
	 *            the y axis
	 * @param xSegments
	 *            number of segments on the x-axis
	 * @param ySegments
	 *            number of segments on the y-axis
	 * @param xSegmentSize
	 *            size (in pixels) of the each segment on the x-axis
	 * @param ySegmentSize
	 *            size (in pixels) of the each segment on the y-axis
	 */
	private void drawGrid(Graphics g, int xAxisStart, int yAxisStart, int xSegments, int ySegments, int xSegmentSize,
			int ySegmentSize) {
		g.setColor(Color.GRAY);
		for (int i = 1; i <= xSegments; i++) {
			int xCoordinate = xAxisStart + i * xSegmentSize;
			g.drawLine(xCoordinate, yAxisStart - 1, xCoordinate,
					yAxisStart - ySegments * ySegmentSize - LENGTH_AFTER_MAX_VALUE);
		}

		for (int i = 1; i <= ySegments; i++) {
			int yCoordinate = yAxisStart - ySegmentSize * i;
			g.drawLine(xAxisStart + 1, yCoordinate, xAxisStart + xSegments * xSegmentSize + LENGTH_AFTER_MAX_VALUE,
					yCoordinate);
		}

		// draw mini lines that mark axes values
		g.setColor(Color.BLACK);
		for (int i = 1; i <= xSegments; i++) {
			int xCoordinate = xAxisStart + i * xSegmentSize - 1;
			g.drawLine(xCoordinate, yAxisStart, xCoordinate, yAxisStart + 6);
		}
		for (int i = 1; i <= ySegments; i++) {
			int yCoordinate = yAxisStart - ySegmentSize * i;
			g.drawLine(xAxisStart, yCoordinate, xAxisStart - 6, yCoordinate);
		}

		// draw arrows
		Insets ins = getInsets();
		int y = ins.top + DISTANCE_FROM_EDGE - LENGTH_AFTER_MAX_VALUE;
		int x = xAxisStart;
		int halfLen = LENGTH_AFTER_MAX_VALUE / 2;
		g.fillPolygon(new int[] { x, x - halfLen, x + halfLen }, new int[] { y, y + halfLen, y + halfLen }, 3);
		x = getWidth() - DISTANCE_FROM_EDGE + LENGTH_AFTER_MAX_VALUE;
		y = yAxisStart;
		g.fillPolygon(new int[] { x, x, x + halfLen }, new int[] { y + halfLen, y - halfLen, y }, 3);

	}

	/**
	 * Draws labels that represent axis values.
	 *
	 * @param g
	 *            object used to create graphic content, i.e. to draw and to
	 *            write to the components surface
	 * @param xAxisStart
	 *            x-coordinate of the pixel at which is the starting point of
	 *            the x axis
	 * @param yAxisStart
	 *            y-coordinate of the pixel at which is the starting point of
	 *            the y axis
	 * @param xSegmentSize
	 *            size (in pixels) of the each segment on the x-axis
	 * @param ySegments
	 *            number of segments on the y-axis
	 * @param ySegmentSize
	 *            size (in pixels) of the each segment on the x-axis
	 */
	private void drawAxisValues(Graphics g, int xAxisStart, int yAxisStart, int xSegmentSize, int ySegments,
			int ySegmentSize) {
		g.setColor(Color.BLACK);
		List<XYValue> values = barChart.getValues();
		FontMetrics fontMet = g.getFontMetrics();

		// marking x-axis values
		for (int i = 0, n = values.size(); i < n; i++) {
			XYValue v = values.get(i);
			String xValue = Integer.toString(v.getX());
			g.drawString(xValue, xAxisStart + i * xSegmentSize + xSegmentSize / 2 - fontMet.stringWidth(xValue) / 2,
					yAxisStart + GAP_BETWEEN_VALUES_AND_AXIS + fontMet.getAscent());
		}

		// marking y-axis values
		int xCoordinate = DISTANCE_FROM_EDGE + fontMet.getHeight() + GAP_BETWEEN_AXIS_NAME_AND_VALUES;
		for (int i = 1; i <= ySegments; i++) {
			String yValue = Integer.toString(barChart.getMinY() + i * barChart.getyGap());
			int yCoordinate = yAxisStart - ySegmentSize * i + fontMet.getAscent() / 2;
			g.drawString(yValue, xCoordinate, yCoordinate);
		}

	}

	/**
	 * Draws the axis names to the component surface.
	 *
	 * @param g
	 *            object used to create graphic content, i.e. to draw and to
	 *            write to the components surface
	 * @param xAxisStart
	 *            x-coordinate of the pixel at which is the starting point of
	 *            the x axis
	 * @param yAxisStart
	 *            y-coordinate of the pixel at which is the starting point of
	 *            the y axis
	 */
	private void drawAxisNames(Graphics g, int xAxisStart, int yAxisStart) {
		Dimension dim = getSize();
		Insets ins = getInsets();
		FontMetrics fontMet = g.getFontMetrics();

		String xName = barChart.getxAxisDescript();
		g.drawString(xName, xAxisStart + (dim.width - xAxisStart - ins.right) / 2 - fontMet.stringWidth(xName) / 2,
				dim.height - fontMet.getAscent() - DISTANCE_FROM_EDGE);

		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform origAt = g2d.getTransform();

		g2d.setTransform(at);
		String yName = barChart.getyAxisDescript();
		g2d.drawString(yName,
				-((int) getLocation().getY() + yAxisStart - (yAxisStart - ins.top) / 2
						+ fontMet.stringWidth(yName) / 2),
				(int) getLocation().getX() + DISTANCE_FROM_EDGE + fontMet.getAscent());
		g2d.setTransform(origAt);

	}

	/**
	 * Determines the x-coordinate (in pixels) of the starting point of the
	 * x-axis, i.e. the x-coordinate of the origin of the bar chart. This is
	 * determined by taking into account sizes of the labels that represent the
	 * y-axis values. So, it will be set so that the widest label does not
	 * collide with the y-axis.
	 *
	 * @param fontMetrics
	 *            metrics of the font that is used to write the labels
	 *            representing the y-axis values
	 * @return x-coordinate (in pixels) of the starting point of the x-axis
	 */
	private int determineStartOfXAxis(FontMetrics fontMetrics) {
		int longestStringWidth = 0;
		for (XYValue v : barChart.getValues()) {
			int width = fontMetrics.stringWidth(Integer.toString(v.getY()));
			if (width > longestStringWidth) {
				longestStringWidth = width;
			}
		}

		return DISTANCE_FROM_EDGE + fontMetrics.getHeight() + GAP_BETWEEN_AXIS_NAME_AND_VALUES + longestStringWidth
				+ GAP_BETWEEN_VALUES_AND_AXIS;

	}

	@Override
	public Dimension getPreferredSize() {
		return PREFERRED_SIZE;

	}

}
