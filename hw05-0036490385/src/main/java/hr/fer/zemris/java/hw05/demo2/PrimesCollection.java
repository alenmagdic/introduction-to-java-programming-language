package hr.fer.zemris.java.hw05.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a collection of prime numbers. It does not contain methods for adding
 * or removing numbers from collection. The only way this collection can be used
 * is through an iterator which returns the first prime number, then the second,
 * the third,etc.*
 *
 * @author Alen Magdić
 *
 */
public class PrimesCollection implements Iterable<Integer> {
	/** Number of prime numbers that this collection should return. **/
	private int numberOfPrimes;

	/**
	 * Constructor.
	 *
	 * @param numberOfPrimes
	 *            number of prime numbers that the collection should return
	 */
	public PrimesCollection(int numberOfPrimes) {
		this.numberOfPrimes = numberOfPrimes;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PrimesIterator();
	}

	/**
	 * An iterator of this collection.
	 *
	 * @author Alen Magdić
	 *
	 */
	private class PrimesIterator implements Iterator<Integer> {
		/** Index of the next number in the collection. **/
		private int indexOfNext;

		@Override
		public boolean hasNext() {
			return indexOfNext < numberOfPrimes;
		}

		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException("There are no more elements in the collection.");
			}

			int indexOfCurrent = indexOfNext;
			indexOfNext++;
			return getPrimeAtIndex(indexOfCurrent);
		}

	}

	/**
	 * Gets a prime number that is at the specified index.
	 *
	 * @param index
	 *            index of number that is to be returned
	 * @return a prime number that is at the specified index
	 */
	private Integer getPrimeAtIndex(int index) {
		int primesFound = 0;

		for (int number = 2;; number++) {
			if (isPrimeNumber(number)) {
				primesFound++;
			}

			boolean isPrimeAtSpecifiedIndex = primesFound - 1 == index;
			if (isPrimeAtSpecifiedIndex) {
				return number;
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

}
