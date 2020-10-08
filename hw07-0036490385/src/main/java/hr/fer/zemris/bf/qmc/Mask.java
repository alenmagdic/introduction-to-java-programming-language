package hr.fer.zemris.bf.qmc;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.utils.Util;

/**
 * This class represents a logical product of the specified variables. It is
 * used for minimization of logical functions. A mask has a read only attribute
 * 'dont care' and a read-write attribute 'combined'. It also stores set of
 * indexes that are represented by this mask. Every bit of the mask can contain
 * value 0 (value of the variable at that index is false), value 1 (true) or
 * value 2 which represents that the variable at that index is not present in
 * the logical product represented by this instance of mask. For example, for
 * variables {A,B,C,D}, mask 1201 represents the following product: A*!C*D
 *
 * @author Alen Magdić
 *
 */
public class Mask {
	/** 'Dont care' flag **/
	private boolean dontCare;
	/** 'Combined' flag **/
	private boolean combined;
	/** Mask array **/
	private byte[] mask;
	/** Set of indexes represented by this mask **/
	private Set<Integer> indexes;
	/** Hash value of this object **/
	private int hash;

	/**
	 * Constructor.
	 *
	 * @param index
	 *            index of a product that is to be represented by this mask
	 * @param numberOfVariables
	 *            number of variables
	 * @param dontCare
	 *            'dont care' flag
	 */
	public Mask(int index, int numberOfVariables, boolean dontCare) {
		if (index < 0 || numberOfVariables <= 0) {
			throw new IllegalArgumentException("Invalid argument given.");
		}

		initializeMask(Util.indexToByteArray(index, numberOfVariables), dontCare);
		indexes = new TreeSet<Integer>();
		indexes.add(index);
	}

	/**
	 * Constructor.
	 *
	 * @param values
	 *            array of bytes containing values that are to be represented by
	 *            this mask
	 * @param indexes
	 *            set of indexes that are to be represented by this mask
	 * @param dontCare
	 *            'dont care' flag
	 */
	public Mask(byte[] values, Set<Integer> indexes, boolean dontCare) {
		if (values == null || values.length == 0 || indexes == null || indexes.size() == 0) {
			throw new IllegalArgumentException("Invalid argument given.");
		}

		initializeMask(values, dontCare);
		this.indexes = new TreeSet<Integer>(indexes);
	}

	/**
	 * Initializes the mask.
	 *
	 * @param values
	 *            array of bytes containing values that are to be represented by
	 *            this mask
	 * @param dontCare
	 *            'dont care' flag
	 */
	private void initializeMask(byte[] values, boolean dontCare) {
		this.dontCare = dontCare;
		mask = values;
		hash = Arrays.hashCode(mask);
		combined = false;
	}

	/**
	 * Returns the state of 'combined' flag.
	 *
	 * @return state of 'combined' flag
	 */
	public boolean isCombined() {
		return combined;
	}

	/**
	 * Sets the 'combined' flag to the specified state.
	 *
	 * @param combined
	 *            new state of the 'combined' flag
	 */
	public void setCombined(boolean combined) {
		this.combined = combined;
	}

	/**
	 * Returns true if the 'dont care' flag is set to true.
	 *
	 * @return true if the 'dont care' flag is set to true
	 */
	public boolean isDontCare() {
		return dontCare;
	}

	/**
	 * Gets set of indexes that are represented by this mask.
	 *
	 * @return set of indexes that are represented by this mask
	 */
	public Set<Integer> getIndexes() {
		return new TreeSet<Integer>(indexes);
	}

	/**
	 * Returns the number of values 1 contained in this mask. For example, if
	 * this mask contains values {0,1,0,1,1}, this method will return number 3.
	 *
	 * @return number of values 1
	 */
	public int countOfOnes() {
		int n = 0;
		for (byte bit : mask) {
			if (bit == 1) {
				n++;
			}
		}
		return n;
	}

	/**
	 * Returns the size of this mask.
	 *
	 * @return size of this mask
	 */
	public int size() {
		return mask.length;
	}

	/**
	 * Gets the mask value at the specified index.
	 *
	 * @param position
	 *            index of value that is to be returned
	 * @return mask value at the specified index
	 */
	public byte getValueAt(int position) {
		if (position < 0 || position >= mask.length) {
			throw new IndexOutOfBoundsException("Specified index: " + position);
		}
		return mask[position];
	}

	/**
	 * Combines this mask with the specified other mask. If the masks are
	 * different in more than a one bit, the masks are not compatible for
	 * combining, so an empty object is returned. If the mask are not compatible
	 * due to different length, then an IllegalArgumentException is thrown.
	 *
	 * @param other
	 *            a mask that is to be combined with this mask
	 * @return optional object, containing combined mask if possible, otherwise
	 *         an empty optional object is returned
	 */
	public Optional<Mask> combineWith(Mask other) {
		if (other == null || other.mask.length != mask.length) {
			throw new IllegalArgumentException("Illegal argument given: " + other == null ? "null" : other.toString());
		}

		if (!canBeCombinedWith(other)) {
			return Optional.empty();
		}

		byte[] combinedValues = new byte[mask.length];
		for (int i = 0; i < mask.length; i++) {
			combinedValues[i] = mask[i] == other.mask[i] ? mask[i] : 2;
		}
		Set<Integer> indexes = new TreeSet<>(this.indexes);
		indexes.addAll(other.indexes);

		return Optional.of(new Mask(combinedValues, indexes, dontCare && other.dontCare));
	}

	/**
	 * Checks if this mask can be combined with the specified mask.
	 *
	 * @param other
	 *            other mask
	 * @return true if this mask can be combined with the specified mask
	 */
	private boolean canBeCombinedWith(Mask other) {
		if (other.mask.length != mask.length) {
			return false;
		}

		for (int i = 0, differences = 0; i < mask.length; i++) {
			if (mask[i] != other.mask[i]) {
				differences++;
			}
			if (differences == 2) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Mask)) {
			return false;
		}
		Mask other = (Mask) obj;
		if (!Arrays.equals(mask, other.mask)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder stringB = new StringBuilder();
		for (byte bit : mask) {
			stringB.append(bit == 2 ? '-' : Byte.toString(bit));
		}
		stringB.append(dontCare ? " D" : " .");
		stringB.append((combined ? " *" : "  ") + " [");

		boolean isFirst = true;
		for (Integer index : indexes) {
			stringB.append((isFirst ? "" : ", ") + index.toString());
			isFirst = false;
		}
		stringB.append(']');

		return stringB.toString();
	}

}
