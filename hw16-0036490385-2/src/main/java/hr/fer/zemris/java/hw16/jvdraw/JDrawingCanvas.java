package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.modeling.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.modeling.DrawingModelListener;

/**
 * Klasa koja reprezentira platno na kojem se crta slika.
 *
 * @author Alen Magdić
 *
 */
public class JDrawingCanvas extends JComponent {
	private static final long serialVersionUID = 1L;
	/** Model koji pohranjuje nacrtane objekte. **/
	private DrawingModel model;
	/**
	 * Privremeni objekt, onaj čije je crtanje u tijeku.
	 */
	private GeometricalObject tempObject;

	/**
	 * Konstruktor.
	 *
	 * @param model
	 *            model koji pohranjuje nacrtane objekte
	 */
	public JDrawingCanvas(DrawingModel model) {
		this.model = model;

		model.addDrawingModelListener(new DrawingModelListener() {

			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				repaint();
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				repaint();
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		Insets ins = getInsets();

		g.setColor(Color.WHITE);
		g.fillRect(ins.left, ins.top, getWidth() - ins.left - ins.right, getHeight() - ins.top - ins.bottom);

		for (int i = 0, n = model.getSize(); i < n; i++) {
			model.getObject(i).draw(g);
		}

		if (tempObject != null) {
			tempObject.draw(g);
		}
	}

	/**
	 * Postavlja privremeni objekt, tj. objekt čije je crtanje u tijeku.
	 *
	 * @param tempObject
	 *            privremeni objekt
	 */
	public void setTempObject(GeometricalObject tempObject) {
		this.tempObject = tempObject;
		repaint();
	}

	/**
	 * Dohvaća model koji pohranjuje nacrtane objekte.
	 *
	 * @return model koji pohranjuje nacrtane objekte
	 */
	public DrawingModel getDrawingModel() {
		return model;
	}
}
