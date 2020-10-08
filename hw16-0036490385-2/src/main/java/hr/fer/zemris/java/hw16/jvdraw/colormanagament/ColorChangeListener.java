package hr.fer.zemris.java.hw16.jvdraw.colormanagament;

import java.awt.Color;

/**
 * Sučelje koje predstavlja promatrača promjene boje.
 *
 * @author Alen Magdić
 *
 */
public interface ColorChangeListener {
	/**
	 * Poziva se kad dođe do promjene boje kod objekta tipa
	 * {@link IColorProvider}.
	 *
	 * @param source
	 *            izvor, objekt kod kojeg se dogodila promjena boje
	 * @param oldColor
	 *            stara boja
	 * @param newColor
	 *            nova boja
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
