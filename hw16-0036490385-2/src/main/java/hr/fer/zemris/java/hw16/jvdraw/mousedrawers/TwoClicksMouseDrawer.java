package hr.fer.zemris.java.hw16.jvdraw.mousedrawers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Apstraktna klasa koja predstavlja {@link MouseDrawer} specijaliziran za
 * crtanje objekta zadavanjem dva klika mišem.
 *
 * @author Alen Magdić
 *
 */
public abstract class TwoClicksMouseDrawer implements MouseDrawer {
	/**
	 * Lista odabranih točki klikanjem miša.
	 */
	protected List<Point> pointsSelected = new ArrayList<>();
	/**
	 * True ako je objekt do kraja nacrtan.
	 */
	protected boolean done;
	/**
	 * Očekivani broj klikova mišem za kompletno crtanje objekta.
	 */
	protected static final int EXPECTED_NUM_OF_POINTS = 2;

	@Override
	public void mouseClicked(Point mousePosition) {
		pointsSelected.add(mousePosition);
		if (pointsSelected.size() == EXPECTED_NUM_OF_POINTS) {
			done = true;
		}

	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public GeometricalObject getCreatedObject() {
		if (!done) {
			return null;
		}

		return getTempVersion(null);
	}

	@Override
	public void restart() {
		done = false;
		pointsSelected.clear();
		resetObject();
	}

	/**
	 * Resetira objekt koji se gradi, tj. stvara konstruira novu instancu
	 * objekta i pamti je.
	 */
	abstract protected void resetObject();

}
