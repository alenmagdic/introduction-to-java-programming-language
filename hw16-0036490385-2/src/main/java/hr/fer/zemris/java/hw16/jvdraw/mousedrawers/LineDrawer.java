package hr.fer.zemris.java.hw16.jvdraw.mousedrawers;

import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.colormanagament.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Line;

/**
 * Objekt koji omogućuje crtanje linije pomoću klikanja mišem.
 *
 * @author Alen Magdić
 *
 */
public class LineDrawer extends TwoClicksMouseDrawer {
	/**
	 * Nacrtani objekt.
	 */
	private Line object = new Line();
	/**
	 * Objekt koji daje informaciju o boji.
	 */
	private IColorProvider colorProvider;

	/**
	 * Konstruktor.
	 *
	 * @param colorProvider
	 *            objekt koji daje informaciju o bojis
	 */
	public LineDrawer(IColorProvider colorProvider) {
		this.colorProvider = colorProvider;
	}

	/**
	 * Konstruktor.
	 */
	public LineDrawer() {
	}

	@Override
	public Line getTempVersion(Point mousePosition) {
		if (pointsSelected.size() + 1 < EXPECTED_NUM_OF_POINTS) {
			return null;
		}

		object.setStartPoint(pointsSelected.get(0));
		object.setEndPoint(done ? pointsSelected.get(1) : mousePosition);

		if (colorProvider != null) {
			object.setColor(colorProvider.getCurrentColor());
		}
		return object;
	}

	@Override
	protected void resetObject() {
		object = new Line();
	}

}
