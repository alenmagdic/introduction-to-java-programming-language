package hr.fer.zemris.java.hw05.observer2;

import hr.fer.zemris.java.hw05.observer1.IntegerStorage;

/**
 * This class is an implementation of {@link IntegerStorageObserver}. It prints
 * the new value stored in the specified {@link IntegerStorage} multiplied by 2.
 * But, it does it's work only the first few times and then it deregisters
 * itself from the list of observers of the {@link IntegerStorage}. The number
 * of times it should work is specified in the constructor.
 *
 * @author Alen Magdić
 *
 */
public class DoubleValue implements IntegerStorageObserver {
	/**
	 * Number of changes that need be done before deregistering.
	 */
	private int numOfChangesRemaining;

	/**
	 * Constructor. Takes a number as a parameter that specifies how many times
	 * does this observer need to work before deregistering itself from the list
	 * of observers of the {@link IntegerStorage}.
	 *
	 * @param numberOfChanges
	 */
	public DoubleValue(int numberOfChanges) {
		numOfChangesRemaining = numberOfChanges;
	}

	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		if (istorageChange == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		numOfChangesRemaining--;
		System.out.printf("Double value: %d\n", 2 * istorageChange.getNewValue());

		if (numOfChangesRemaining == 0) {
			istorageChange.getStorage().removeObserver(this);
		}
	}

}
