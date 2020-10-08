package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JToolBar;

/**
 * A class that represents a localizable toolbar. It extends {@link JToolBar}
 * but enables automatic change of its name according to the current
 * localization. It registers a new listener to the specified (specified as a
 * constructor argument) localization provider. That listener updates the name
 * of this toolbar using the specified (specified as a constructor argument) key
 * to determine its name for the current localization.
 *
 * @author Alen Magdić
 *
 */
public class LJToolbar extends JToolBar {
	private static final long serialVersionUID = 1L;
	/**
	 * A key that is used to determine toolbar name for the current
	 * localization.
	 **/
	private String key;
	/**
	 * A localization provider used to get information about toolbar name that
	 * is appropriate for the current localization.
	 **/
	private ILocalizationProvider provider;

	/**
	 * Constructor.
	 *
	 * @param key
	 *            a key that is used to determine toolbar name for the current
	 *            localization
	 * @param lp
	 *            a localization provider used to get information about toolbar
	 *            name that is appropriate for the current localization
	 */
	public LJToolbar(String key, ILocalizationProvider lp) {
		this.key = key;
		this.provider = lp;
		updateName();
		lp.addLocalizationListener(() -> {
			updateName();
		});
	}

	/**
	 * Updates the toolbar name to a name that is appropriate for the current
	 * localization.
	 */
	private void updateName() {
		setName(provider.getString(key));
	}
}
