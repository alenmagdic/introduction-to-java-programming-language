package hr.fer.zemris.java.hw16.jvdraw.propertiespanels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw16.jvdraw.Util;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Line;

/**
 * Panel koji omogućuje odabir svojstava linije.
 *
 * @author Alen Magdić
 *
 */
public class LinePropertiesPanel extends ObjectPropertiesPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Linija.
	 *
	 */
	private Line line = new Line();
	/**
	 * Unos x koordinate početne točke linije.
	 */
	private JSpinner x0Input = new JSpinner();
	/**
	 * Unos y koordinate početne točke linije.
	 */
	private JSpinner y0Input = new JSpinner();
	/**
	 * Unos x koordinate završne točke linije.
	 */
	private JSpinner x1Input = new JSpinner();
	/**
	 * Unos y koordinate završne točke linije.
	 */
	private JSpinner y1Input = new JSpinner();
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
	public LinePropertiesPanel() {
		setLayout(new GridLayout(3, 2));

		add(new JLabel("Start point(x,y): "));
		add(Util.createPanelWithComponents(x0Input, y0Input));

		add(new JLabel("End point(x,y): "));
		add(Util.createPanelWithComponents(x1Input, y1Input));

		add(new JLabel("Line color: "));
		add(Util.createPanelWithComponents(rInput, gInput, bInput));

		ChangeListener changeListener = e -> updateData();
		x0Input.addChangeListener(changeListener);
		y0Input.addChangeListener(changeListener);
		x1Input.addChangeListener(changeListener);
		y1Input.addChangeListener(changeListener);
		rInput.addChangeListener(changeListener);
		gInput.addChangeListener(changeListener);
		bInput.addChangeListener(changeListener);
	}

	/**
	 * Ažurira podatke.
	 */
	private void updateData() {
		line.setStartPoint(new Point((int) x0Input.getValue(), (int) y0Input.getValue()));
		line.setEndPoint(new Point((int) x1Input.getValue(), (int) y1Input.getValue()));
		line.setColor(new Color((int) rInput.getValue(), (int) gInput.getValue(), (int) bInput.getValue()));
	}

	@Override
	public void setObject(GeometricalObject object) {
		if (object == null) {
			throw new IllegalArgumentException("Object can not be set to null.");
		}
		try {
			line = (Line) object;
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException("Expected object of type Line");
		}

		int x0 = line.getStartPoint().x;
		int y0 = line.getStartPoint().y;
		int x1 = line.getEndPoint().x;
		int y1 = line.getEndPoint().y;
		int r = line.getColor().getRed();
		int g = line.getColor().getGreen();
		int b = line.getColor().getBlue();
		x0Input.setValue(x0);
		y0Input.setValue(y0);
		x1Input.setValue(x1);
		y1Input.setValue(y1);
		rInput.setValue(r);
		gInput.setValue(g);
		bInput.setValue(b);
	}

	@Override
	public GeometricalObject getObject() {
		return line;
	}

}
