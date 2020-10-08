package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * An encapsulation of a bar chart. It stores integer pairs of numbers
 * encapsulated by {@link XYValue} where the first value represents a x-axis
 * value, while the second value represents the corresponding y-axis value. It
 * also contains data like minimum or maximum y-axis value and a gap between two
 * closest marked numbers on the y-axis. *
 *
 * @author Alen Magdić
 *
 */
public class BarChart {
	/** Bar chart values. **/
	private List<XYValue> values;
	/** Description of x-axis data. **/
	private String xAxisDescript;
	/** Description of y-axis data. **/
	private String yAxisDescript;
	/** Minimal number that should be shown on y-axis. **/
	private int minY;
	/** Maximum number that should be shown on y-axis. **/
	private int maxY;
	/**
	 * A gap between two closest marked numbers on the y-axis.
	 **/
	private int yGap;

	/**
	 * Constructor.
	 *
	 * @param values
	 *            a list containing pairs of x-y values, where x represents an
	 *            x-axis value, while the y represents the corresponding y-axis
	 *            value
	 * @param xAxisDescript
	 *            description of the x-axis
	 * @param yAxisDescript
	 *            description of the y-axis
	 * @param minY
	 *            minimal number that should be shown on y-axis
	 * @param maxY
	 *            maximum number that should be shown on y-axis
	 * @param yGap
	 *            a gap between two closest marked numbers on the y-axis
	 */
	public BarChart(List<XYValue> values, String xAxisDescript, String yAxisDescript, int minY, int maxY, int yGap) {
		super();
		this.values = new ArrayList<>(values);
		this.xAxisDescript = xAxisDescript;
		this.yAxisDescript = yAxisDescript;
		this.minY = minY;
		this.maxY = maxY;
		this.yGap = yGap;
	}

	/**
	 * Gets the list of bar chart values.
	 *
	 * @return the list of bar chart values
	 */
	public List<XYValue> getValues() {
		return new ArrayList<>(values);
	}

	/**
	 * Gets the description of x-axis.
	 *
	 * @return the description of x-axis
	 */
	public String getxAxisDescript() {
		return xAxisDescript;
	}

	/**
	 * Gets the description of y-axis.
	 *
	 * @return the description of y-axis
	 */
	public String getyAxisDescript() {
		return yAxisDescript;
	}

	/**
	 * Gets the minimal number that should be shown on y-axis.
	 *
	 * @return the minimal number that should be shown on y-axis
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Gets the maximum number that should be shown on y-axis.
	 *
	 * @return the maximum number that should be shown on y-axis
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Gets the gap between two closest marked numbers on the y-axis.
	 *
	 * @return the gap between two closest marked numbers on the y-axis
	 */
	public int getyGap() {
		return yGap;
	}

}
