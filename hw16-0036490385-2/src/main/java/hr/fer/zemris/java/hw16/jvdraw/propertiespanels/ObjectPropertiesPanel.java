package hr.fer.zemris.java.hw16.jvdraw.propertiespanels;

import javax.swing.JPanel;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Apstraktna klasa koja modelira panel za promjenu svojstava geometrijskog
 * objekta.
 *
 * @author Alen Magdić
 *
 */
public abstract class ObjectPropertiesPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Postavlja objekt čija svojstva treba uređivati.
	 *
	 * @param object
	 *            objekt čija svojstva treba uređivati
	 */
	abstract public void setObject(GeometricalObject object);

	/**
	 * Dohvaća objekt sa uređenim svojstvima.
	 *
	 * @return objekt sa uređenim svojstvima
	 */
	abstract public GeometricalObject getObject();
}
