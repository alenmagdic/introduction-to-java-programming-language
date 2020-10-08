package hr.fer.zemris.java.hw05.observer2;

/**
 * This class is an implementation of {@link IntegerStorageObserver}. It counts
 * the number of changes, i.e. it counts how many times it's single method has
 * been called.
 *
 * @author Alen Magdić
 *
 */
public class ChangeCounter implements IntegerStorageObserver {
	/** Number of changes. **/
	private int numOfChanges;

	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		if (istorageChange == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		numOfChanges++;
		System.out.printf("Number of value changes since tracking: %d\n", numOfChanges);
	}

}
