package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that represent a calculator layout. It is something like a grid
 * layout but with the fixed dimensions (5 rows and 8 columns). Another
 * difference to the grid layout is the top left cell which is the wide cell,
 * covering the width of the 5 normal cells.*
 *
 * @author Alen Magdić
 *
 */
public class CalcLayout implements LayoutManager2 {
	/** Number of rows **/
	private static final int NUMBER_OF_ROWS = 5;
	/** Number of columns **/
	private static final int NUMBER_OF_COLUMNS = 7;
	/** Position of the big cell. **/
	private static final RCPosition POSITION_OF_BIG_CELL = new RCPosition(1, 1);
	/**
	 * The width of the big cell, expressed as a number of normal cells that it
	 * covers.
	 **/
	private static final int BIG_CELL_WIDTH = 5;
	/** Gap (in pixels) between the cells. **/
	private int gap;
	/** A map that associates components to their position in the layout. **/
	Map<Object, RCPosition> mapCompToPosition;

	/**
	 * Constructor.
	 *
	 * @param gap
	 *            gap (in pixels) between the cells
	 */
	public CalcLayout(int gap) {
		this.gap = gap;
		mapCompToPosition = new HashMap<>();

	}

	/**
	 * Default constructor. Setting the gap to zero.
	 *
	 */
	public CalcLayout() {
		this(0);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if (!mapCompToPosition.containsKey(comp)) {
			throw new IllegalArgumentException("Trying to remove a component that has not been added.");
		}
		mapCompToPosition.remove(comp);
	}

	/**
	 * An enumerator representing a type of the size that is to be determined.
	 * There are three size types: preferred size, minimum size and the maximum
	 * size.
	 *
	 * @author Alen Magdić
	 *
	 */
	private enum SizeType {
		PREFERRED_SIZE, MINIMUM_SIZE, MAXIMUM_SIZE;
	}

	/**
	 * Determines the specified type of size of the layout.
	 *
	 * @param parent
	 *            layout parent
	 * @param sizeType
	 *            type of the size that is to be determined
	 * @return the specified type of size of the layout
	 */
	private Dimension layoutSize(Container parent, SizeType sizeType) {
		if (parent.getComponentCount() == 0) {
			return new Dimension(0, 0);
		}

		Component[] components = parent.getComponents();
		double mWidth = 0;
		double mHeight = 0;
		for (Component comp : components) {
			Dimension compSize;
			if (sizeType == SizeType.PREFERRED_SIZE) {
				compSize = comp.getPreferredSize();
			} else if (sizeType == SizeType.MAXIMUM_SIZE) {
				compSize = comp.getMaximumSize();
			} else {
				compSize = comp.getMinimumSize();
			}

			if (compSize == null) {
				continue;
			}

			RCPosition compPosition = mapCompToPosition.get(comp);
			if (compPosition == null) {
				continue;
			}

			double widthPerNormalCell;
			if (compPosition.equals(POSITION_OF_BIG_CELL)) {
				widthPerNormalCell = (compSize.getWidth() - (BIG_CELL_WIDTH - 1) * gap) / BIG_CELL_WIDTH;
			} else {
				widthPerNormalCell = compSize.getWidth();
			}

			double height = compSize.getHeight();
			if (sizeType == SizeType.MAXIMUM_SIZE) {
				if (widthPerNormalCell < mWidth) {
					mWidth = widthPerNormalCell;
				}
				if (height < mHeight) {
					mHeight = height;
				}
			} else {
				if (widthPerNormalCell > mWidth) {
					mWidth = widthPerNormalCell;
				}
				if (height > mHeight) {
					mHeight = height;
				}
			}
		}

		Insets insets = parent.getInsets();
		int layoutWidth = (int) mWidth * NUMBER_OF_COLUMNS + insets.left + insets.right;
		int layoutHeight = (int) mHeight * NUMBER_OF_ROWS + insets.top + insets.bottom;
		return new Dimension(layoutWidth, layoutHeight);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return layoutSize(parent, SizeType.PREFERRED_SIZE);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return layoutSize(parent, SizeType.MINIMUM_SIZE);
	}

	@Override
	public Dimension maximumLayoutSize(Container parent) {
		return layoutSize(parent, SizeType.MAXIMUM_SIZE);
	}

	@Override
	public void layoutContainer(Container parent) {
		Dimension parentSize = parent.getSize();
		Insets insets = parent.getInsets();
		double cellWidth = (parentSize.getWidth() - insets.right - insets.left - (NUMBER_OF_COLUMNS - 1) * gap)
				/ NUMBER_OF_COLUMNS;
		double cellHeight = (parentSize.getHeight() - insets.top - insets.bottom - (NUMBER_OF_ROWS - 1) * gap)
				/ NUMBER_OF_ROWS;

		for (Component comp : parent.getComponents()) {
			RCPosition compPosition = mapCompToPosition.get(comp);
			if (compPosition == null) {
				continue;
			}

			int colIndex = compPosition.getColumn() - 1;
			int rowIndex = compPosition.getRow() - 1;
			comp.setLocation(insets.left + colIndex * ((int) cellWidth + gap),
					insets.top + rowIndex * ((int) cellHeight + gap));

			if (compPosition.equals(POSITION_OF_BIG_CELL)) {
				double width = cellWidth * BIG_CELL_WIDTH + (BIG_CELL_WIDTH - 1) * gap;
				comp.setSize((int) width, (int) cellHeight);
			} else {
				if (compPosition.getColumn() == NUMBER_OF_COLUMNS) {
					// stretch to the right edge
					comp.setSize((int) parentSize.width - insets.right - comp.getX(), (int) cellHeight);
				} else {
					comp.setSize((int) cellWidth, (int) cellHeight);
				}
			}

		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (comp == null || constraints == null) {
			throw new IllegalArgumentException("null argument given");
		}

		RCPosition position;
		if (constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
		} else if (constraints instanceof String) {
			position = parseConstraints((String) constraints);
		} else {
			throw new IllegalArgumentException(
					"Unacceptable constraints object given. Expected instance of RCPosition or String, but given instance of "
							+ constraints.getClass());
		}

		if (position.getRow() < 0 || position.getRow() > NUMBER_OF_ROWS || position.getColumn() < 0
				|| position.getColumn() > NUMBER_OF_COLUMNS) {
			throw new IllegalArgumentException("Given position out of bounds: " + position);
		} else if (position.getRow() == POSITION_OF_BIG_CELL.getRow()
				&& position.getColumn() > POSITION_OF_BIG_CELL.getColumn()
				&& position.getColumn() < POSITION_OF_BIG_CELL.getColumn() + BIG_CELL_WIDTH) {
			throw new IllegalArgumentException("Given illegal position: " + position);
		}

		if (mapCompToPosition.containsValue(position)) {
			throw new IllegalArgumentException(
					"There already is a component with the specified constraints. Constraints: " + position);
		}
		mapCompToPosition.put(comp, position);

	}

	/**
	 * Parses the specified constraints, i.e. position of a component.
	 *
	 * @param constraints
	 *            constraints that are to be parsed
	 * @return a {@link RCPosition} object generated by parsing the specified
	 *         constraints
	 */
	private RCPosition parseConstraints(String constraints) {
		String[] parts = constraints.split(",");
		if (parts.length != 2) {
			throw new RuntimeException("Invalid constraints! Constraints: " + constraints);
		}

		int row;
		int columns;
		try {
			row = Integer.parseInt(parts[0]);
			columns = Integer.parseInt(parts[1]);
		} catch (NumberFormatException ex) {
			throw new RuntimeException("Invalid constraints! Constraints: " + constraints);
		}

		return new RCPosition(row, columns);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

}
