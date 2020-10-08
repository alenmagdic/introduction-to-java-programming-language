package hr.fer.zemris.java.hw16.jvdraw.colormanagament;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Komponenta koja omogućuje odabir boje.
 *
 * @author Alen Magdić
 *
 */
public class JColorArea extends JComponent implements IColorProvider {
	private static final long serialVersionUID = 1L;
	/**
	 * Odabrana boja.
	 */
	private Color selectedColor;
	/**
	 * Promatrači komponente.
	 */
	private List<ColorChangeListener> listeners;

	/**
	 * Konstruktor.
	 *
	 * @param color
	 *            inicijalna boja
	 */
	public JColorArea(Color color) {
		this.selectedColor = color;
		listeners = new ArrayList<>();

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				executeColorSelection();
			}
		});
	}

	/**
	 * Izvršava opciju odabira boje. Otvara dijalog za odabir boje.
	 */
	private void executeColorSelection() {
		Color oldColor = selectedColor;
		selectedColor = JColorChooser.showDialog(this, "Choose color", selectedColor);

		if (selectedColor == null) {
			selectedColor = oldColor;
			return;
		}

		for (ColorChangeListener l : listeners) {
			l.newColorSelected(this, oldColor, selectedColor);
		}

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Insets ins = getInsets();
		g.setColor(selectedColor);
		g.fillRect(ins.left, ins.top, getWidth() - ins.left - ins.right, getHeight() - ins.top - ins.bottom);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException("Listener can not be null.");
		}
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

}
