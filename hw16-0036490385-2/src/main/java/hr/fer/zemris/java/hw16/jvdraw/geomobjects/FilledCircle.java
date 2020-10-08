package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Klasa koja predstavlja krug.
 *
 * @author Alen Magdić
 *
 */
public class FilledCircle extends Circle {
	/**
	 * Boja ispune.
	 *
	 */
	private Color fillColor;

	/**
	 * Konstruktor.
	 *
	 */
	public FilledCircle() {
	}

	/**
	 * Konstruktor.
	 *
	 * @param centerPoint
	 *            središte kruga
	 * @param radius
	 *            polumjer kruga
	 * @param outlineColor
	 *            boja obruba
	 * @param fillColor
	 *            boja ispune
	 * @param circleName
	 *            ime kruga
	 */
	public FilledCircle(Point centerPoint, int radius, Color outlineColor, Color fillColor, String circleName) {
		super(centerPoint, radius, outlineColor, circleName);
		this.fillColor = fillColor;
	}

	/**
	 * Konstruktor.
	 *
	 * @param centerPoint
	 *            središte kruga
	 * @param radius
	 *            polumjer kruga
	 * @param outlineColor
	 *            boja obruba
	 * @param fillColor
	 *            boja ispune
	 */
	public FilledCircle(Point centerPoint, int radius, Color outlineColor, Color fillColor) {
		this(centerPoint, radius, outlineColor, fillColor, "Circle");
	}

	/**
	 * Dohvaća boju ispune.
	 *
	 * @return boja ispune
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Postavlja boju ispune.
	 *
	 * @param fillColor
	 *            boja ispune
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public void draw(Graphics g) {
		Point cp = getCenterPoint();
		int r = getRadius();

		g.setColor(fillColor);
		g.fillOval(cp.x - r, cp.y - r, 2 * r, 2 * r);
		super.draw(g);
	}

	@Override
	public GeometricalObject getCopyOfObject() {
		return new FilledCircle(super.getCenterPoint(), super.getRadius(), super.getColor(), fillColor,
				super.getName());
	}

	@Override
	public void copyValuesFromObject(GeometricalObject object) {
		FilledCircle circle;
		try {
			circle = (FilledCircle) object;
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException("Expected object of type FilledCircle.");
		}
		super.setCenterPoint(circle.getCenterPoint());
		super.setRadius(circle.getRadius());
		super.setColor(circle.getColor());
		fillColor = circle.fillColor;
		super.setName(circle.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (fillColor == null ? 0 : fillColor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FilledCircle other = (FilledCircle) obj;
		if (fillColor == null) {
			if (other.fillColor != null) {
				return false;
			}
		} else if (!fillColor.equals(other.fillColor)) {
			return false;
		}
		return true;
	}

}
