package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A functional interface representing a localization listener. Contains a
 * method that does some actions whenever localization changes.
 *
 * @author Alen Magdić
 *
 */
public interface ILocalizationListener {

	/**
	 * Does appropriate actions in order to execute change of localization.
	 */
	public void localizationChanged();
}
