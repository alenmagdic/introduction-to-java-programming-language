package hr.fer.zemris.java.hw05.observer2;

/**
 * This class encapsulates a single change made to the {@link IntegerStorage}.
 * It contains the old value stored in the {@link IntegerStorage}, and the new
 * value, and a reference to the {@link IntegerStorage} itself.
 *
 *
 * @author Alen Magdić
 *
 */
public class IntegerStorageChange {
	/** The storage whose value has been changed. **/
	private IntegerStorage storage;
	/** The old value stored before a change has been done. **/
	private int oldValue;
	/** The new value stored. **/
	private int newValue;

	/**
	 * Constructor.
	 *
	 * @param integerStorage
	 *            an integer storage whose value has been changed
	 * @param oldValue
	 *            old value stored before a change has been done
	 * @param newValue
	 *            new value stored
	 */
	public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
		if (integerStorage == null) {
			throw new IllegalArgumentException(
					"A null reference is not a legal argument for the parameter integerStorage.");
		}

		this.storage = integerStorage;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Gets the {@link IntegerStorage}
	 *
	 * @return integer storage
	 */
	public IntegerStorage getStorage() {
		return storage;
	}

	/**
	 * Gets the old value.
	 *
	 * @return old value
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Gets the new value.
	 *
	 * @return new value
	 */
	public int getNewValue() {
		return newValue;
	}

}
