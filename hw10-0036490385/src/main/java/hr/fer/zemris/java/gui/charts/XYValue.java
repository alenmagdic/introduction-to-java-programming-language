package hr.fer.zemris.java.gui.charts;

/**
 * A class that encapsulates a pair of integer values.
 *
 * @author Alen Magdić
 *
 */
public class XYValue {
	/** The first value of the pair. **/
	private int x;
	/** The second value of the pair. **/
	private int y;

	/**
	 * Constructor.
	 *
	 * @param x
	 *            the first value of the pair
	 * @param y
	 *            the second value of the pair
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the first value of the pair.
	 *
	 * @return the first value of the pair
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the second value of the pair.
	 *
	 * @return the second value of the pair
	 */
	public int getY() {
		return y;
	}
}
