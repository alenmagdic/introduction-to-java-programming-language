package hr.fer.zemris.java.hw16.jvdraw.mousedrawers;

import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.colormanagament.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;

/**
 * Objekt koji omogućuje crtanje kružnice pomoću klikanja mišem.
 *
 * @author Alen Magdić
 *
 */
public class CircleDrawer extends TwoClicksMouseDrawer {
	/**
	 * Nacrtani objekt.
	 */
	private Circle object = new Circle();
	/**
	 * Objekt koji daje informaciju o boji.
	 */
	private IColorProvider colorProvider;

	/**
	 * Konstruktor.
	 *
	 * @param colorProvider
	 *            objekt koji daje informaciju o boji
	 */
	public CircleDrawer(IColorProvider colorProvider) {
		this.colorProvider = colorProvider;
	}

	/**
	 * Konstruktor.
	 */
	public CircleDrawer() {
	}

	@Override
	public Circle getTempVersion(Point mousePosition) {
		if (pointsSelected.size() + 1 < EXPECTED_NUM_OF_POINTS) {
			return null;
		}

		Point pointOnCircle = done ? pointsSelected.get(1) : mousePosition;
		int radius = calculateDistanceBetweenPoints(pointsSelected.get(0), pointOnCircle);

		object.setCenterPoint(pointsSelected.get(0));
		object.setRadius(radius);

		if (colorProvider != null) {
			object.setColor(colorProvider.getCurrentColor());
		}
		return object;
	}

	/**
	 * Računa udaljenost između zadanih točaka.
	 *
	 * @param point1
	 *            prva točka
	 * @param point2
	 *            druga točka
	 * @return udaljenost između zadanih točaka
	 */
	protected int calculateDistanceBetweenPoints(Point point1, Point point2) {
		return (int) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
	}

	@Override
	protected void resetObject() {
		object = new Circle();
	}

}
