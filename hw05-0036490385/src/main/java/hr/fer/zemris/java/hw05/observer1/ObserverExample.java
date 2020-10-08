package hr.fer.zemris.java.hw05.observer1;

/**
 * This program is a demonstration of {@link IntegerStorageObserver}. It
 * repatedly sets new values for an {@link IntegerStorage} which leads to some
 * actions that it's observers do whenever the value has been changed. The
 * program demonstrates the following observers: {@link SquareValue},
 * {@link ChangeCounter}, {@link DoubleValue}.
 *
 * @author Alen Magdić
 *
 */
public class ObserverExample {
	/**
	 * This method is the starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);

		IntegerStorageObserver observer = new SquareValue();
		istorage.addObserver(observer);

		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);

		istorage.removeObserver(observer);
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));

		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
}
