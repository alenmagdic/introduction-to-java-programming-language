package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This class represents a localization provider bridge. It extends the
 * {@link AbstractLocalizationProvider} class and has the specified
 * {@link ILocalizationProvider} as its parent. It adds methods to connect and
 * to disconnect this "bridge", i.e. by connecting it starts to inform all its
 * listeners whenever the localization changes, and by disconnecting it stops to
 * inform them. In order to know when the localization changes, it tracks its
 * parent (tracks by adding a localization listener to the parent).
 *
 * @author Alen Magdić
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/** A parent of this bridge **/
	private ILocalizationProvider parent;
	/** State of the bridge connection **/
	private boolean connected;
	/**
	 * A listener that is to be added to the parent and that informs all
	 * listeners of this bridge.
	 **/
	private ILocalizationListener listener;

	/**
	 * Constructor.
	 *
	 * @param parent
	 *            a parent of this bridge
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		if (parent == null) {
			throw new IllegalArgumentException("Parent can not be null.");
		}
		this.parent = parent;
		listener = () -> {
			fire();
		};
	}

	/**
	 * Connects this "bridge", i.e. it enables the listeners of this class to be
	 * informed when the localization of parent provider changes.
	 */
	public void connect() {
		if (connected) {
			return;
		}
		parent.addLocalizationListener(listener);
	}

	/**
	 * Disconnects this "bridge", i.e. it disables the listeners of this class
	 * from being informed when the localization of parent provider changes.
	 */
	public void disconnect() {
		if (!connected) {
			return;
		}
		parent.removeLocalizationListener(listener);
	}

	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

}
