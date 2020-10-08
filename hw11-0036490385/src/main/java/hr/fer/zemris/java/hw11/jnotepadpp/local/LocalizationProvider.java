package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A class that represents a localization provider. It extends
 * {@link AbstractLocalizationProvider} so that it can be actually used. It adds
 * a method to set the current localization (i.e. to set language) and
 * implements a method for getting an appropriate (appropriate for the current
 * localization) translation for the specified key.
 *
 * This is a singleton class, so it does not offer a public constructor but
 * instead offers a method to get the only instance of this class.
 *
 * @author Alen Magdić
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	/** Current language **/
	private String language;
	/** Current bundle for the current language **/
	private ResourceBundle bundle;
	/** A single instance of this class. **/
	private static LocalizationProvider instance;

	/**
	 * Constructor.
	 **/
	private LocalizationProvider() {
		this.language = "en";
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translations.translation",
				Locale.forLanguageTag(language));
	}

	/**
	 * Gets the single instance of this class.
	 *
	 * @return the single instance of this class
	 */
	public static LocalizationProvider getInstance() {
		if (instance == null) {
			instance = new LocalizationProvider();
		}
		return instance;
	}

	@Override
	public String getString(String key) {
		if (key == null) {
			throw new IllegalArgumentException("Key can not be null.");
		}
		return bundle.getString(key);
	}

	/**
	 * Sets the current localization language to the specified language.
	 *
	 * @param language
	 *            a language that is to bet as the current localization provider
	 *            language
	 */
	public void setLanguage(String language) {
		this.language = language;
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translations.translation",
				Locale.forLanguageTag(language));
		fire();
	}

	/**
	 * Gets a {@link Locale} object for the current language.
	 *
	 * @return a {@link Locale} object for the current language
	 */
	public Locale getLocale() {
		return bundle.getLocale();
	}

}
