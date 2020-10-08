package hr.fer.zemris.java.hw04.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents a collection which stores keys and their associated
 * values in the collection's hash table. The class provides methods for adding
 * new values to the collection, removing values, checking if there is a key or
 * a value in the class and toString() method which prints all keys with
 * associated values in the collection.
 *
 * @author Alen Magdić
 *
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	/**
	 * Number of entries stored in the collection.
	 *
	 */
	private int size;
	/**
	 * Hash table.
	 */
	private TableEntry<K, V>[] table;
	/**
	 * If table capacity is not specified, it is set to this default value.
	 */
	private static final int DEFAULT_TABLE_CAPACITY = 16;
	/**
	 * If table density reaches this level of density, the capacity of the table
	 * is doubled.
	 */
	private static final double MAX_DENSITY = 0.75;
	/**
	 * Number of modifications done to the collection.
	 */
	private int modificationCount;

	/**
	 * Constructor.
	 *
	 * @param capacity
	 *            number of slots in the collection's hash table
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("A capacity can not be lower than 1. Given value: " + capacity);
		}

		int c;
		for (c = 1; c < capacity; c *= 2) {
		}
		capacity = c;

		table = (TableEntry<K, V>[]) new TableEntry[capacity];
	}

	/**
	 * Default constructor. Generates a hash table with default capacity.
	 *
	 */
	public SimpleHashtable() {
		this(DEFAULT_TABLE_CAPACITY);
	}

	/**
	 * Gets a table slot from the specified key.
	 *
	 * @param key
	 *            a key whose slot is to be retrieved
	 * @return slot of the specified key
	 */
	private int getSlotFromKey(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	/**
	 * Puts the specified entry in the collection. If there already is an entry
	 * with the given key, the given value overrides the old one. Otherwise, a
	 * new table entry is added to the collection.
	 *
	 * @param key
	 *            key of an entry that is to be added to the collection
	 * @param value
	 *            value of an entry that is to be added to the collection
	 */
	public void put(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("A key can not be null.");
		}
		int table_slot = getSlotFromKey(key);

		if (table[table_slot] == null) {
			table[table_slot] = new TableEntry<K, V>(key, value, null);
		} else {
			TableEntry<K, V> tableEntry = table[table_slot];
			while (tableEntry.next != null && !tableEntry.getKey().equals(key)) {
				tableEntry = tableEntry.next;
			}

			if (tableEntry.getKey().equals(key)) {
				tableEntry.setValue(value);
				return;
			} else {
				tableEntry.next = new TableEntry<K, V>(key, value, null);
			}
		}

		size++;
		modificationCount++;

		if ((double) size / table.length >= MAX_DENSITY) {
			doubleCapacity();
		}
	}

	/**
	 * Doubles the capacity of the hash table.
	 */
	@SuppressWarnings("unchecked")
	private void doubleCapacity() {
		TableEntry<K, V> oldTable[] = table;
		table = (TableEntry<K, V>[]) new TableEntry[oldTable.length * 2];
		size = 0;

		for (int i = 0; i < oldTable.length; i++) {
			TableEntry<K, V> tableEntry = oldTable[i];

			while (tableEntry != null) {
				put(tableEntry.key, tableEntry.value);
				tableEntry = tableEntry.next;
			}
		}
	}

	/**
	 * Gets the value associated with the specified key.
	 *
	 * @param key
	 *            key whose value is to be returned
	 * @return value associated with the specified key
	 */
	public V get(Object key) {
		if (key == null) {
			return null;
		}

		int table_slot = getSlotFromKey(key);
		TableEntry<K, V> tableEntry = table[table_slot];

		if (tableEntry == null) {
			return null;
		}

		while (!tableEntry.getKey().equals(key)) {
			tableEntry = tableEntry.next;
			if (tableEntry == null) {
				return null;
			}
		}

		return tableEntry.getValue();
	}

	/**
	 * Returns the number of table entries in the collection.
	 *
	 * @return number of table entries in the collection
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if there is an entry with the specified key in the collection.
	 *
	 * @param key
	 *            key whose existence is to be checked
	 * @return true if there is an entry with the specified key in the
	 *         collection, false otherwise
	 */
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}

		return get(key) != null;
	}

	/**
	 * Removes an entry specified by it's key from the collection. If the
	 * specified key does not exist in collection, nothing happens.
	 *
	 * @param key
	 *            key whose entry is to be removed from the collection
	 */
	public void remove(Object key) {
		if (key == null) {
			return;
		}

		int table_slot = getSlotFromKey(key);
		TableEntry<K, V> tableEntry = table[table_slot];
		if (tableEntry == null) {
			return;
		}
		if (tableEntry.getKey().equals(key)) {
			table[table_slot] = tableEntry.next;
			size--;
			modificationCount++;
			return;
		} else if (tableEntry.next == null) {
			return;
		}

		while (!tableEntry.next.getKey().equals(key)) {
			tableEntry = tableEntry.next;
			if (tableEntry.next == null) {
				return;
			}
		}

		tableEntry.next = tableEntry.next.next;
		size--;
		modificationCount++;

	}

	/**
	 * Checks if there are any entries in the collection.
	 *
	 * @return return if there is any entry in the collection
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns a string representation of the collection. Example:
	 * "[key1=value1, key2=value2]"
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('[');

		boolean atLeastOneValueAdded = false;

		for (int i = 0; i < table.length; i++) {
			TableEntry<K, V> tableEntry = table[i];

			while (tableEntry != null) {
				if (atLeastOneValueAdded) {
					stringBuilder.append(", ");
				} else {
					atLeastOneValueAdded = true;
				}

				stringBuilder.append(tableEntry.toString());
				tableEntry = tableEntry.next;
			}
		}
		stringBuilder.append(']');

		return stringBuilder.toString();
	}

	/**
	 * Checks if there is an entry with the specified value in the collection.
	 *
	 * @param value
	 *            value whose existence is to be checked
	 * @return true if there is an entry with the specified value in the
	 *         collection, false otherwise
	 */
	public boolean containsValue(Object value) {
		TableEntry<K, V> tableEntry;

		for (int i = 0; i < table.length; i++) {
			tableEntry = table[i];

			while (tableEntry != null) {
				if (tableEntry.getValue().equals(value)) {
					return true;
				}
				tableEntry = tableEntry.next;
			}
		}
		return false;
	}

	/**
	 * Clears all the entries from the collection.
	 */
	public void clear() {
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		size = 0;
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * An implementation of iterator. Returns table entries in unspecified
	 * order.
	 *
	 * @author Alen Magdić
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		/**
		 * Modifications counter for detection of outside modifications.
		 */
		private int legalModifCount;
		/**
		 * An exception that is thrown when there has been an outside
		 * modification while iterating.
		 */
		private final RuntimeException modificationException = new ConcurrentModificationException(
				"There have been some outside modifications done while iterating.");
		/**
		 * Index of the slot where is the next entry located.
		 */
		private int nextSlot;
		/**
		 * The next table entry to be returned.
		 */
		private TableEntry<K, V> nextEntry;
		/**
		 * The current table entry.
		 */
		private TableEntry<K, V> currentEntry;

		/**
		 * Constructor.
		 *
		 */
		public IteratorImpl() {
			legalModifCount = modificationCount;
			findNextEntry();
		}

		/**
		 * Looks for the next table entry.
		 */
		private void findNextEntry() {
			if (nextEntry == null && nextSlot == 0) {
				nextEntry = table[0];
			} else {
				nextEntry = nextEntry.next;
			}

			while (nextEntry == null) {
				nextSlot++;
				if (nextSlot == table.length) {
					nextEntry = null;
					return;
				}
				nextEntry = table[nextSlot];
			}
		}

		@Override
		public boolean hasNext() {
			if (modificationCount > legalModifCount) {
				throw modificationException;
			}
			return nextEntry != null;
		}

		@Override
		public SimpleHashtable.TableEntry<K, V> next() {
			if (modificationCount > legalModifCount) {
				throw modificationException;
			}
			if (!hasNext()) {
				throw new NoSuchElementException("There are no more elements in the collection.");
			}

			currentEntry = nextEntry;
			findNextEntry();
			return currentEntry;
		}

		@Override
		public void remove() {
			if (modificationCount > legalModifCount) {
				throw modificationException;
			}
			if (!SimpleHashtable.this.containsKey(currentEntry.key)) {
				throw new IllegalStateException("The iterator can not remove an entry that has already been removed.");
			}

			SimpleHashtable.this.remove(currentEntry.key);
			legalModifCount++;
		}
	}

	/**
	 * This is a public static class that represents a table entry. It contains
	 * a key, a value and a reference to the next entry in the same table slot.
	 *
	 * @author Alen Magdić
	 *
	 * @param <K>
	 *            type of key
	 * @param <V>
	 *            type of value
	 */
	public static class TableEntry<K, V> {
		/**
		 * Entry key.
		 */
		private K key;
		/**
		 * Entry value.
		 */
		private V value;
		/**
		 * A reference to the next entry in the same hash table slot.
		 *
		 */
		private TableEntry<K, V> next;

		/**
		 * Constructor.
		 *
		 * @param key
		 *            key
		 * @param value
		 *            value
		 * @param next
		 *            a reference to the next entry in the same hash table slot
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			if (key == null) {
				throw new IllegalArgumentException("Ključ ne može biti null.");
			}
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Gets key.
		 *
		 * @return key
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Gets value.
		 *
		 * @return value
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets value.
		 *
		 * @param value
		 *            value
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns a string representation of this object. Example: 'key=value'
		 *
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}
	}

}
