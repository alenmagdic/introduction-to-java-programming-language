package hr.fer.zemris.java.gui.layouts;

/**
 * An object that represents a position in the {@link CalcLayout}. It contains
 * two attributes: row number and column number.
 *
 * @author Alen Magdić
 *
 */
public class RCPosition {
	/** Row number **/
	private int row;
	/** Column number **/
	private int column;

	/**
	 * Constructor.
	 *
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets the row number.
	 *
	 * @return the row number
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the column number.
	 *
	 * @return the column number
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RCPosition)) {
			return false;
		}

		RCPosition other = (RCPosition) obj;
		if (column != other.column) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return row + "," + column;
	}

}
