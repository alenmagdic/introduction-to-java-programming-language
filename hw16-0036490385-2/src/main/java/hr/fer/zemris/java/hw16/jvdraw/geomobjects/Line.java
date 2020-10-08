package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Klasa koja predstavlja liniju.
 *
 * @author Alen Magdić
 *
 */
public class Line extends GeometricalObject {
	/**
	 * Ime tipa objekta.
	 */
	private static final String TYPE_NAME = "Line";
	/**
	 * Početna točka linije.
	 */
	private Point startPoint;
	/**
	 * Završna točka linije.
	 */
	private Point endPoint;
	/**
	 * Boja linije.
	 */
	private Color color;

	/**
	 * Konstruktor.
	 */
	public Line() {
		this(new Point(0, 0), new Point(0, 0), Color.BLACK);
	}

	/**
	 * Konstruktor.
	 *
	 * @param startPoint
	 *            početna točka linije
	 * @param endPoint
	 *            završna točka linije
	 * @param color
	 *            boja linije
	 */
	public Line(Point startPoint, Point endPoint, Color color) {
		this(startPoint, endPoint, color, "Line");
	}

	/**
	 * Konstruktor.
	 *
	 * @param startPoint
	 *            početna točka linije
	 * @param endPoint
	 *            završna točka linije
	 * @param color
	 *            boja linije
	 * @param name
	 *            ime linije
	 */
	public Line(Point startPoint, Point endPoint, Color color, String name) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
		super.setName(name);
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Dohvaća početnu točku linije.
	 *
	 * @return početna točka linije.
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * Postavlja početnu točku linije.
	 *
	 * @param startPoint
	 *            početna točka linije
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Vraća završnu točku linije.
	 *
	 * @return završna točka linije
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * Postavlja završnu točku linije.
	 *
	 * @param endPoint
	 *            završna točka linije
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public GeometricalObject getCopyOfObject() {
		return new Line(startPoint, endPoint, color, super.getName());
	}

	@Override
	public void copyValuesFromObject(GeometricalObject object) {
		Line line;
		try {
			line = (Line) object;
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException("Expected object of type Line.");
		}
		this.startPoint = line.startPoint;
		this.endPoint = line.endPoint;
		this.color = line.color;
		super.setName(line.getName());
	}

	@Override
	public Rectangle getBoundingBox() {
		Rectangle rect = new Rectangle();
		rect.x = Integer.min(startPoint.x, endPoint.x);
		rect.y = Integer.min(startPoint.y, endPoint.y);
		rect.width = Integer.max(startPoint.x, endPoint.x) - rect.x;
		rect.height = Integer.max(startPoint.y, endPoint.y) - rect.y;
		return rect;
	}

	@Override
	public void move(int x, int y) {
		startPoint.x += x;
		startPoint.y += y;
		endPoint.x += x;
		endPoint.y += y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (color == null ? 0 : color.hashCode());
		result = prime * result + (endPoint == null ? 0 : endPoint.hashCode());
		result = prime * result + (startPoint == null ? 0 : startPoint.hashCode());
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
		Line other = (Line) obj;
		if (color == null) {
			if (other.color != null) {
				return false;
			}
		} else if (!color.equals(other.color)) {
			return false;
		}
		if (endPoint == null) {
			if (other.endPoint != null) {
				return false;
			}
		} else if (!endPoint.equals(other.endPoint)) {
			return false;
		}
		if (startPoint == null) {
			if (other.startPoint != null) {
				return false;
			}
		} else if (!startPoint.equals(other.startPoint)) {
			return false;
		}
		return true;
	}

}
