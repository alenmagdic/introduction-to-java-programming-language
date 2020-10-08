package hr.fer.zemris.java.hw16.jvdraw.colormanagament;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Labela koja sadrži informaciju o trenutnoj foreground i pozadinskoj boji.
 *
 * @author Alen Magdić
 *
 */
public class ColorInfoLabel extends JLabel implements ColorChangeListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Objekt iz kojeg se dobiva informacija o foreground boji.
	 */
	private IColorProvider fgColorProvider;
	/**
	 * Objekt iz kojeg se dobiva informacija o pozadinskoj boji.
	 */
	private IColorProvider bgColorProvider;

	/**
	 * Konstruktor.
	 *
	 * @param fgColorProvider
	 *            objekt iz kojeg se dobiva informacija o foreground boji
	 * @param bgColorProvider
	 *            objekt iz kojeg se dobiva informacija o pozadinskoj boji
	 */
	public ColorInfoLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;

		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);

		updateText();
	}

	/**
	 * Ažurira tekst labele.
	 */
	private void updateText() {
		Color fgCol = fgColorProvider.getCurrentColor();
		Color bgCol = bgColorProvider.getCurrentColor();

		int fgR = fgCol.getRed();
		int fgG = fgCol.getGreen();
		int fgB = fgCol.getBlue();
		int bgR = bgCol.getRed();
		int bgG = bgCol.getGreen();
		int bgB = bgCol.getBlue();
		this.setText(String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d).", fgR, fgG, fgB,
				bgR, bgG, bgB));
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		updateText();
	}
}
