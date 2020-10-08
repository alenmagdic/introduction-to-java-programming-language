package hr.fer.zemris.java.hw16.jvdraw.propertiespanels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw16.jvdraw.Util;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Panel koji omogućuje odabir svojstava kružnice.
 *
 * @author Alen Magdić
 *
 */
public class CirclePropertiesPanel extends ObjectPropertiesPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Kružnica.
	 */
	private Circle circle = new Circle();
	/**
	 * Unos x kooridnate središta.
	 */
	private JSpinner xInput = new JSpinner();
	/**
	 * Unos y koordinate središta.
	 */
	private JSpinner yInput = new JSpinner();
	/**
	 * Unos polumjera.
	 */
	private JSpinner radiusInput = new JSpinner();
	/**
	 * Unos red komponente boje.
	 */
	private JSpinner rInput = new JSpinner();
	/**
	 * Unos green komponente boje.
	 */
	private JSpinner gInput = new JSpinner();
	/**
	 * Unos blue komponente boje.
	 */
	private JSpinner bInput = new JSpinner();

	/**
	 * Konstruktor.
	 */
	public CirclePropertiesPanel() {
		setLayout(new GridLayout(3, 2));

		add(new JLabel("Center point(x,y): "));
		add(Util.createPanelWithComponents(xInput, yInput));

		add(new JLabel("Radius: "));
		add(Util.createPanelWithComponents(radiusInput));

		add(new JLabel("Circle color: "));
		add(Util.createPanelWithComponents(rInput, gInput, bInput));

		ChangeListener changeListener = e -> updateData();
		xInput.addChangeListener(changeListener);
		yInput.addChangeListener(changeListener);
		radiusInput.addChangeListener(changeListener);
		rInput.addChangeListener(changeListener);
		gInput.addChangeListener(changeListener);
		bInput.addChangeListener(changeListener);
	}

	/**
	 * Ažurira podatke.
	 */
	private void updateData() {
		circle.setCenterPoint(new Point((int) xInput.getValue(), (int) yInput.getValue()));
		circle.setRadius((int) radiusInput.getValue());
		circle.setColor(new Color((int) rInput.getValue(), (int) gInput.getValue(), (int) bInput.getValue()));
	}

	@Override
	public void setObject(GeometricalObject object) {
		if (object == null) {
			throw new IllegalArgumentException("Object can not be set to null.");
		}
		try {
			circle = (Circle) object;
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException("Expected object of type Circle");
		}

		int x = circle.getCenterPoint().x;
		int y = circle.getCenterPoint().y;
		int radius = circle.getRadius();
		int r = circle.getColor().getRed();
		int g = circle.getColor().getGreen();
		int b = circle.getColor().getBlue();
		xInput.setValue(x);
		yInput.setValue(y);
		radiusInput.setValue(radius);
		rInput.setValue(r);
		gInput.setValue(g);
		bInput.setValue(b);
	}

	@Override
	public GeometricalObject getObject() {
		return circle;
	}

}
