package hr.fer.zemris.java.hw05.observer2;

import hr.fer.zemris.java.hw05.observer1.ChangeCounter;
import hr.fer.zemris.java.hw05.observer1.DoubleValue;
import hr.fer.zemris.java.hw05.observer1.IntegerStorage;
import hr.fer.zemris.java.hw05.observer1.IntegerStorageObserver;
import hr.fer.zemris.java.hw05.observer1.SquareValue;

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
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);

		istorage.addObserver(new SquareValue());
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(2));

		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
		istorage.setValue(55);
		istorage.setValue(29);
		istorage.setValue(38);
	}
}
