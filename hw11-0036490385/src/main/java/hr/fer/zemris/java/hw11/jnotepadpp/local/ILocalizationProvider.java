package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * An interface representing a localization provider. According to this
 * interface, every localization provider has to offer a method that gets
 * appropriate (appropriate for the current localization) translate for the
 * specified key. It also has to offer methods that enable registering and
 * deregistering {@link ILocalizationListener} listeners.
 *
 * @author Alen Magdić
 *
 */
public interface ILocalizationProvider {

	/**
	 * Gets an appropriate (appropriate for the current localization)
	 * translation for the specified key.
	 *
	 * @param key
	 *            a key for translation
	 * @return appropriate translation for the specified key
	 */
	public String getString(String key);

	/**
	 * Registers the specified localization listener.
	 *
	 * @param listener
	 *            a listener that is to be registered
	 */
	public void addLocalizationListener(ILocalizationListener listener);

	/**
	 * Deregisters the specified localization listener.
	 *
	 * @param listener
	 *            a listener that is to be deregistered
	 */
	public void removeLocalizationListener(ILocalizationListener listener);
}
