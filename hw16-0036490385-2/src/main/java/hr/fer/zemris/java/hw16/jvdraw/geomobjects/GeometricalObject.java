package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Apstraktna klasa koja predstavlja geometrijski objekt.
 *
 * @author Alen Magdić
 *
 */
public abstract class GeometricalObject extends Nameable implements Drawable {
	/**
	 * Kreira kopiju ovog objekta i vraća je.
	 *
	 * @return kopija ovog objekta
	 */
	public abstract GeometricalObject getCopyOfObject();

	/**
	 * Kopira vrijednosti iz zadanog objekta i pridjeljuje ih sebi.
	 *
	 * @param object
	 *            objekt iz kojeg se kopiraju vrijednosti
	 */
	public abstract void copyValuesFromObject(GeometricalObject object);

	/**
	 * Postavlja boju objekta.
	 *
	 * @param color
	 *            boja objekta
	 */
	public abstract void setColor(Color color);

	/**
	 * Dohvaća boju objekta.
	 *
	 * @return boja objekta
	 */
	public abstract Color getColor();

	/**
	 * Dohvaća {@link Rectangle} koji omeđuje objekt.
	 *
	 * @return {@link Rectangle} koji omeđuje objekt
	 */
	public abstract Rectangle getBoundingBox();

	/**
	 * Pomiče objekt za zadani pomak po x i po y osi.
	 *
	 * @param x
	 *            pomak po x osi
	 * @param y
	 *            pomak po y osi
	 */
	abstract public void move(int x, int y);
}
