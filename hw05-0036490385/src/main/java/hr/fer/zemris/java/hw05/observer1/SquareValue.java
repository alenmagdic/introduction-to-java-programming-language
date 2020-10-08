package hr.fer.zemris.java.hw05.observer1;

/**
 * This class is an implementation of {@link IntegerStorageObserver}. It prints
 * a square of the new value stored in the specified {@link IntegerStorage}.
 *
 * @author Alen Magdić
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorage istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		int value = istorage.getValue();
		System.out.printf("Provided new value: %d, square is %d\n", value, value * value);
	}

}
