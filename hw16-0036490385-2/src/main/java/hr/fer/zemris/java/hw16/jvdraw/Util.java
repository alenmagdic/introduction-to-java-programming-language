package hr.fer.zemris.java.hw16.jvdraw;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Klasa koja sadrži pomoćne metode za JVDraw aplikaciju.
 *
 * @author Alen Magdić
 *
 */
public class Util {

	/**
	 * Kreira panel sa zadanim komponentama. Komponente raspoređuje korištenjem
	 * {@link BoxLayout} layout managera uz opciju vodoravnog slaganja.
	 *
	 * @param components
	 *            komponente koje treba staviti u kreirani panel
	 * @return kreirani panel s zadanim komponentama
	 */
	public static JPanel createPanelWithComponents(JComponent... components) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		for (JComponent component : components) {
			panel.add(component);
		}
		return panel;
	}
}
