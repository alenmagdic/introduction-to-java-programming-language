package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link ILocalizationProvider} that does not implement
 * getString(String key) method but implements adding and removing localization
 * listeners and also contains a method that informs the currently registered
 * listeners that the localization has just been changed.
 *
 * @author Alen Magdić
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/** A list containing the currently registered listeners. **/
	private List<ILocalizationListener> listeners;

	/**
	 * Constructor.
	 *
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Can not add null as a listener.");
		}

		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Informs all currently registered listeners that the localization has just
	 * been changed.
	 *
	 */
	public void fire() {
		for (ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}

}
