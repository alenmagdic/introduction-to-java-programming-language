package hr.fer.zemris.java.hw05.observer1;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores a single integer value. It contains a list of observers and
 * calls them whenever the stored value is changed.
 *
 * @author Alen Magdić
 *
 */
public class IntegerStorage {
	/** Stored value. **/
	private int value;
	/** List of observers. **/
	private List<IntegerStorageObserver> observers; // use ArrayList here!!!

	/**
	 * Constructor.
	 *
	 * @param initialValue
	 *            initial value that is to be stored
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds the specified observer to the list of observers.
	 *
	 * @param observer
	 *            an observer that is to be added to the list of observers
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("It is illegal to add null as an observer.");
		}

		if (observers == null) {
			observers = new ArrayList<>();
		}

		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	/**
	 * Removes the specified observer from the list of observers.
	 *
	 * @param observer
	 *            an observer that is to be removed from the list of observers
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if (observers != null && observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	/**
	 * Clears the list of observers.
	 */
	public void clearObservers() {
		if (observers != null) {
			observers.clear();
		}
	}

	/**
	 * Gets the stored value.
	 *
	 * @return stored value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the stored value and calls the observers.
	 *
	 * @param value
	 *            a new value that is to be stored
	 */
	public void setValue(int value) {
		if (this.value != value) {
			this.value = value;

			if (observers != null) {
				List<IntegerStorageObserver> copyOfObservers = new ArrayList<>(observers);
				// iterating over a copy of observers in order to prevent
				// ConcurrentModificationException when an observer tries to
				// deregister itself
				for (IntegerStorageObserver observer : copyOfObservers) {
					observer.valueChanged(this);
				}
			}
		}
	}
}