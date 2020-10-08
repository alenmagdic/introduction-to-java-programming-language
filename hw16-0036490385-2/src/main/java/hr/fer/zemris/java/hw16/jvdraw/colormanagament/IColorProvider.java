package hr.fer.zemris.java.hw16.jvdraw.colormanagament;

import java.awt.Color;

/**
 * Sučelje koje predstavlja objekt koji daje informaciju o odabranoj boji te
 * obavještava promatrače kada se dogodi promjena.
 *
 * @author Alen Magdić
 *
 */
public interface IColorProvider {
	/**
	 * Vraća trenutnu odabranu boju.
	 *
	 * @return trenutna odabrana boja
	 */
	public Color getCurrentColor();

	/**
	 * Registrira zadanog promatrača.
	 *
	 * @param l
	 *            promatrač
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Uklanja zadanog promatrača.
	 *
	 * @param l
	 *            promatrač
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
