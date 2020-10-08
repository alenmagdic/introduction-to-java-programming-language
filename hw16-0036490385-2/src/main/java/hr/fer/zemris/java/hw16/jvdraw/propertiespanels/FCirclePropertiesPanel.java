package hr.fer.zemris.java.hw16.jvdraw.propertiespanels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw16.jvdraw.Util;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Panel koji omogućuje odabir svojstava kruga.
 *
 * @author Alen Magdić
 *
 */
public class FCirclePropertiesPanel extends ObjectPropertiesPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Krug.
	 */
	private FilledCircle circle = new FilledCircle();
	/**
	 * Unos x komponente središta.
	 */
	private JSpinner xInput = new JSpinner();
	/**
	 * Unos y komponente središta.
	 */
	private JSpinner yInput = new JSpinner();
	/**
	 * Unos polumjera.
	 */
	private JSpinner radiusInput = new JSpinner();
	/**
	 * Unos red komponente boje obruba.
	 */
	private JSpinner r1Input = new JSpinner();
	/**
	 * Unos green komponente boje obruba.
	 */
	private JSpinner g1Input = new JSpinner();
	/**
	 * Unos blue komponente boje obruba.
	 */
	private JSpinner b1Input = new JSpinner();
	/**
	 * Unos red komponente boje ispune.
	 */
	private JSpinner r2Input = new JSpinner();
	/**
	 * Unos green komponente boje ispune.
	 */
	private JSpinner g2Input = new JSpinner();
	/**
	 * Unos blue komponente boje ispune.
	 */
	private JSpinner b2Input = new JSpinner();

	/**
	 * Konstruktor.
	 */
	public FCirclePropertiesPanel() {
		setLayout(new GridLayout(4, 2));

		add(new JLabel("Center point(x,y): "));
		add(Util.createPanelWithComponents(xInput, yInput));

		add(new JLabel("Radius: "));
		add(Util.createPanelWithComponents(radiusInput));

		add(new JLabel("Outline color: "));
		add(Util.createPanelWithComponents(r1Input, g1Input, b1Input));

		add(new JLabel("Fill color: "));
		add(Util.createPanelWithComponents(r2Input, g2Input, b2Input));

		ChangeListener changeListener = e -> updateData();
		xInput.addChangeListener(changeListener);
		yInput.addChangeListener(changeListener);
		radiusInput.addChangeListener(changeListener);
		r1Input.addChangeListener(changeListener);
		g1Input.addChangeListener(changeListener);
		b1Input.addChangeListener(changeListener);
		r2Input.addChangeListener(changeListener);
		g2Input.addChangeListener(changeListener);
		b2Input.addChangeListener(changeListener);
	}

	/**
	 * Ažurira podatke.
	 */
	private void updateData() {
		circle.setCenterPoint(new Point((int) xInput.getValue(), (int) yInput.getValue()));
		circle.setRadius((int) radiusInput.getValue());
		circle.setColor(new Color((int) r1Input.getValue(), (int) g1Input.getValue(), (int) b1Input.getValue()));
		circle.setFillColor(new Color((int) r2Input.getValue(), (int) g2Input.getValue(), (int) b2Input.getValue()));
	}

	@Override
	public void setObject(GeometricalObject object) {
		if (object == null) {
			throw new IllegalArgumentException("Object can not be set to null.");
		}
		try {
			circle = (FilledCircle) object;
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException("Expected object of type FilledCircle");
		}

		int x = circle.getCenterPoint().x;
		int y = circle.getCenterPoint().y;
		int radius = circle.getRadius();
		int r1 = circle.getColor().getRed();
		int g1 = circle.getColor().getGreen();
		int b1 = circle.getColor().getBlue();
		int r2 = circle.getFillColor().getRed();
		int g2 = circle.getFillColor().getGreen();
		int b2 = circle.getFillColor().getBlue();
		xInput.setValue(x);
		yInput.setValue(y);
		radiusInput.setValue(radius);
		r1Input.setValue(r1);
		g1Input.setValue(g1);
		b1Input.setValue(b1);
		r2Input.setValue(r2);
		g2Input.setValue(g2);
		b2Input.setValue(b2);
	}

	@Override
	public GeometricalObject getObject() {
		return circle;
	}

}
