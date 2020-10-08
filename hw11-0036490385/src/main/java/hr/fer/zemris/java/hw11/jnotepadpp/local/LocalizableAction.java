package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.MissingResourceException;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * This class extends {@link AbstractAction} class but does not implement its
 * method (actionPerformed(ActionEvent e)). It is an abstract class representing
 * a class that can be localized. So, it adds a new listener to the specified
 * {@link ILocalizationProvider} (specifid as constructor argument) that
 * refreshes the current action specifications (action name, short description
 * and mnemonic) so that they are appropriate for the current localization. In
 * order to check if there is a short description or a mnemonic for this action,
 * it is assumed that keys for short descriptions are made by concatenating the
 * key for action name and "_desc" (or "_mnm" for mnemonics). For example, if
 * there is a key for action name named "open", then a key for a description of
 * that action is expected to be equal to "open_desc".*
 *
 * @author Alen Magdić
 *
 */
public abstract class LocalizableAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 *
	 * @param key
	 *            a key that is used to determine action specifications for the
	 *            current localization
	 * @param lp
	 *            a localization provider used to get information about action
	 *            specifications for the current localization
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		if (key == null || lp == null) {
			throw new IllegalArgumentException("Null given.");
		}
		putValues(key, lp);
		lp.addLocalizationListener(() -> {
			putValues(key, lp);
		});
	}

	/**
	 * Updates the action specifications for the current localization.
	 *
	 * @param key
	 *            a key that is used to determine action specifications for the
	 *            current localization
	 * @param lp
	 *            a localization provider used to get information about action
	 *            specifications for the current localization
	 */
	private void putValues(String key, ILocalizationProvider lp) {
		putValue(Action.NAME, lp.getString(key));
		try {
			putValue(Action.MNEMONIC_KEY, (int) lp.getString(key + "_mnm").charAt(0));
			putValue(Action.SHORT_DESCRIPTION, lp.getString(key + "_desc"));
		} catch (MissingResourceException ignorable) {
		}
	}
}
