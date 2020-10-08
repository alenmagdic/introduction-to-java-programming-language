package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Klasa koja predstavlja kružnicu.
 *
 * @author Alen Magdić
 *
 */
public class Circle extends GeometricalObject {
	/**
	 * Ime tipa objekta.
	 */
	public static final String TYPE_NAME = "Circle";
	/**
	 * Točka središta kružnice.
	 */
	private Point centerPoint;
	/**
	 * Polumjer kružnice.
	 *
	 */
	private int radius;
	/**
	 * Boja kružnice.
	 */
	private Color color;

	/**
	 * Konstruktor.
	 */
	public Circle() {
		this(new Point(0, 0), 0, Color.BLACK);
	}

	/**
	 * Konstruktor.
	 *
	 * @param centerPoint
	 *            središte kružnice
	 * @param radius
	 *            polumjer kružnice
	 * @param color
	 *            boja kružnice
	 */
	public Circle(Point centerPoint, int radius, Color color) {
		this(centerPoint, radius, color, "Circle");
	}

	/**
	 * Konstruktor.
	 *
	 * @param centerPoint
	 *            središte kružnice
	 * @param radius
	 *            polumjer kružnice
	 * @param color
	 *            boja kružnice
	 * @param circleName
	 *            ime kružnice
	 */
	public Circle(Point centerPoint, int radius, Color color, String circleName) {
		this.centerPoint = centerPoint;
		this.radius = radius;
		this.color = color;
		super.setName(circleName);
	}

	/**
	 * Dohvaća središnju točku kružnice.
	 *
	 * @return središnja točka kružnice
	 */
	public Point getCenterPoint() {
		return centerPoint;
	}

	/**
	 * Postavlja središnju točku kružnice.
	 *
	 * @param centerPoint
	 *            središnja točka kružnice
	 */
	public void setCenterPoint(Point centerPoint) {
		this.centerPoint = centerPoint;
	}

	/**
	 * Dohvaća polumjer kružnice.
	 *
	 * @return polumjer kružnice
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Postavlja polumjer kružnice.
	 *
	 * @param radius
	 *            polumjer kružnice
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawOval(centerPoint.x - radius, centerPoint.y - radius, 2 * radius, 2 * radius);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public GeometricalObject getCopyOfObject() {
		return new Circle(centerPoint, radius, color, super.getName());
	}

	@Override
	public void copyValuesFromObject(GeometricalObject object) {
		Circle circle;
		try {
			circle = (Circle) object;
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException("Expected object of type Circle.");
		}
		this.centerPoint = circle.centerPoint;
		this.radius = circle.radius;
		this.color = circle.color;
		super.setName(circle.getName());
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(centerPoint.x - radius, centerPoint.y - radius, 2 * radius, 2 * radius);
	}

	@Override
	public void move(int x, int y) {
		centerPoint.x += x;
		centerPoint.y += y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (centerPoint == null ? 0 : centerPoint.hashCode());
		result = prime * result + (color == null ? 0 : color.hashCode());
		result = prime * result + radius;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Circle other = (Circle) obj;
		if (centerPoint == null) {
			if (other.centerPoint != null) {
				return false;
			}
		} else if (!centerPoint.equals(other.centerPoint)) {
			return false;
		}
		if (color == null) {
			if (other.color != null) {
				return false;
			}
		} else if (!color.equals(other.color)) {
			return false;
		}
		if (radius != other.radius) {
			return false;
		}
		return true;
	}

}
