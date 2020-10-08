package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A class that extends a {@link LocalizationProviderBridge}. It does not add
 * new methods, but in its constructor adds a new window listener to the
 * specified frame. That listener disconnects this bridge after the window
 * closes and connects when the window opens. This class is made in order to
 * enable garbage collector clear the closed window from memory (because
 * disconnecting the bridge removes a reference to that window).
 *
 * @author Alen Magdić
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor.
	 *
	 * @param provider
	 *            a localization provider that is to be wrapped by by this
	 *            provider
	 * @param frame
	 *            a frame that is to be connected/disconnected with the
	 *            specified localization provider
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
		});
	}
}
