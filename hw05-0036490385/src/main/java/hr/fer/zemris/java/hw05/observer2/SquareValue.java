package hr.fer.zemris.java.hw05.observer2;

/**
 * This class is an implementation of {@link IntegerStorageObserver}. It prints
 * a square of the new value stored in the specified {@link IntegerStorage}.
 *
 * @author Alen Magdić
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		if (istorageChange == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		int value = istorageChange.getNewValue();
		System.out.printf("Provided new value: %d, square is %d\n", value, value * value);
	}

}
