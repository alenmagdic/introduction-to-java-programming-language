package hr.fer.zemris.java.hw05.observer1;

/**
 * This is an observer of an {@link IntegerStorage}. It has only one method that
 * is called when a value of the specified {@link IntegerStorage} has been
 * changed and then does some actions.
 *
 * @author Alen Magdić
 *
 */
public interface IntegerStorageObserver {
	/**
	 * The method that is called when a value of an {@link IntegerStorage} has
	 * been changed, so this method does it's work using the new value.
	 *
	 * @param istorage
	 *            integer storage whose value has been changed
	 */
	public void valueChanged(IntegerStorage istorage);
}