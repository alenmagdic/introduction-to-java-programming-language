package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A {@link ListModel} that generates and stores prime numbers. There is
 * initially a one number in the list, the number 1.
 *
 * @author Alen Magdić
 *
 */
public class PrimListModel implements ListModel<Integer> {
	/** List of generated prime numbers. **/
	private List<Integer> generatedNumbers;
	/** List of {@link ListDataListener} listeners. **/
	private List<ListDataListener> listeners;

	/**
	 * Constructor.
	 **/
	public PrimListModel() {
		generatedNumbers = new ArrayList<>();
		generatedNumbers.add(1);
		listeners = new ArrayList<>();
	}

	/**
	 * Generates the next prime number and adds it to the list of generated
	 * numbers, i.e. calls its listeners so that they now that an interval has
	 * been added to the list.
	 *
	 */
	public void next() {
		generatedNumbers.add(getNextPrimNumber());
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, generatedNumbers.size() - 1,
				generatedNumbers.size() - 1);
		for (ListDataListener l : listeners) {
			l.intervalAdded(event);
		}

	}

	/**
	 * Calculates the next prime number.
	 *
	 * @return the next prime number
	 */
	private Integer getNextPrimNumber() {
		int lastAdded = generatedNumbers.get(generatedNumbers.size() - 1);
		for (int num = lastAdded + 1;; num++) {
			if (isPrimeNumber(num)) {
				return num;
			}
		}
	}

	/**
	 * Checks if the specified number is a prime number.
	 *
	 * @param number
	 *            number that is to be checked
	 * @return true if the specified number is a prime number
	 */
	private boolean isPrimeNumber(int number) {
		for (int num = 2; num <= number / 2; num++) {
			if (number % num == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getSize() {
		return generatedNumbers.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return generatedNumbers.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

}
