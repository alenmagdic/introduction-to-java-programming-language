package hr.fer.zemris.java.hw16.jvdraw.modeling;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Klasa koja predstavlja model crtanja, odnosno objekt koji pohranjuje nacrtane
 * objekte te promatračima dojavljuje sve izmjene.
 *
 * @author Alen Magdić
 *
 */
public interface DrawingModel {

	/**
	 * Vraća broj objekata u modelu.
	 *
	 * @return broj objekata u modelu
	 */
	public int getSize();

	/**
	 * Vraća objekt na zadanom indeksu.
	 *
	 * @param index
	 *            indeks
	 * @return objekt na zadanom indeksu
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Dodaje objekt.
	 *
	 * @param object
	 *            objekt
	 */
	public void add(GeometricalObject object);

	/**
	 * Uklanja objekt.
	 *
	 * @param object
	 *            objekt
	 */
	public void remove(GeometricalObject object);

	/**
	 * Dodaje promatrača.
	 *
	 * @param l
	 *            promatrač
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Uklanja promatrača.
	 *
	 * @param l
	 *            promatrač
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
