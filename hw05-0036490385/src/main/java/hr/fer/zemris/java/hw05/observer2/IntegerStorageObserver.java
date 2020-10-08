package hr.fer.zemris.java.hw05.observer2;

import hr.fer.zemris.java.hw05.observer1.IntegerStorage;

/**
 * This is an observer of an {@link IntegerStorage}. It has only one method that
 * is called when a value of the specified {@link IntegerStorage} has been
 * changed and done does some actions.
 *
 * @author Alen Magdić
 *
 */
public interface IntegerStorageObserver {
	/**
	 * The method that is called when a value of an {@link IntegerStorage} has
	 * been changed, so this method does it's work using the new value.
	 *
	 * @param istorageChange
	 *            an encapsulation of change that has happened
	 */
	public void valueChanged(IntegerStorageChange istorageChange);
}