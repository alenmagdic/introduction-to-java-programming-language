package hr.fer.zemris.java.hw16.jvdraw.mousedrawers;

import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.colormanagament.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.FilledCircle;

/**
 * Objekt koji omogućuje crtanje kruga pomoću klikanja mišem.
 *
 * @author Alen Magdić
 *
 */
public class FilledCircleDrawer extends CircleDrawer {
	/**
	 * Objekt koji daje informacije o boji obruba.
	 */
	private IColorProvider fgColorProvider;
	/**
	 * Objekt koji daje informacije o boji ispune.
	 */
	private IColorProvider bgColorProvider;
	/**
	 * Nacrtani objekt.
	 */
	private FilledCircle object = new FilledCircle();

	/**
	 * Konstruktor.
	 *
	 * @param fgColorProvider
	 *            objekt koji daje informacije o boji obruba
	 * @param bgColorProvider
	 *            objekt koji daje informacije o boji ispune
	 */
	public FilledCircleDrawer(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;
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

		if (fgColorProvider != null) {
			object.setColor(fgColorProvider.getCurrentColor());
		}
		if (bgColorProvider != null) {
			object.setFillColor(bgColorProvider.getCurrentColor());
		}
		return object;
	}

	@Override
	protected void resetObject() {
		object = new FilledCircle();
	}
}
