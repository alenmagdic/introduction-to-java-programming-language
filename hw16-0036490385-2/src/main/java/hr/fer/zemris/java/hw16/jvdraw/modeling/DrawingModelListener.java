package hr.fer.zemris.java.hw16.jvdraw.modeling;

/**
 * Promatrač modela crtanja odnosno objekta tipa {@link DrawingModel}.
 *
 * @author Alen Magdić
 *
 */
public interface DrawingModelListener {
	/**
	 * Poziva se kada su u model dodani objekti.
	 *
	 * @param source
	 *            model
	 * @param index0
	 *            početni indeks promjene
	 * @param index1
	 *            završni indeks promjene
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Poziva se kada su iz modela uklonjeni objekti.
	 *
	 * @param source
	 *            model
	 * @param index0
	 *            početni indeks promjene
	 * @param index1
	 *            završni indeks promjene
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Poziva se prilikom kompleksne izmjene modela.
	 *
	 * @param source
	 *            model
	 * @param index0
	 *            početni indeks promjene
	 * @param index1
	 *            završni indeks promjene
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);

}
