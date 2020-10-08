package hr.fer.zemris.java.custom.collections;

/**
 * Klasa koja predstavlja implementaciju stoga. Sadrži metode za upravljanje
 * sadržajem stoga.
 *
 * @author Alen Magdić
 *
 */
public class ObjectStack {
	/**
	 * Kolekcija u koju se pohranjuju elementi na stogu.
	 */
	private ArrayIndexedCollection collection;

	/**
	 * Defaultni konstruktor.
	 */
	public ObjectStack() {
		collection = new ArrayIndexedCollection();
	}

	/**
	 * Vraća true ako je stog prazan, odnosno ako nema niti jednog elementa
	 * pohranjenog na stogu.
	 *
	 * @return true ako je stog prazan
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	/**
	 * Vraća broj elemenata pohranjenih na stogu.
	 *
	 * @return broj elemenata pohranjenih na stogu
	 */
	public int size() {
		return collection.size();
	}

	/**
	 * Stavlja dani objekt na stog.
	 *
	 * @param value
	 *            objekt kojeg je potrebno staviti na stog
	 */
	public void push(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("This collection does not support null values.");
		}
		collection.add(value);
	}

	/**
	 * Skida zadnje pohranjeni element sa stoga te ga vraća kao rezultat metode.
	 * Ako je stog prazan, baca se EmptyStackException.
	 *
	 * @return zadnje pohranjeni element na stogu
	 */
	public Object pop() {
		int size = collection.size();
		if (size == 0) {
			throw new EmptyStackException();
		}

		Object element = collection.get(size - 1);
		collection.remove(size - 1);
		return element;
	}

	/**
	 * Dohvaća zadnje pohranjeni element sa stoga, ali ga ne skida sa stoga. Ako
	 * je stog prazan, baca se EmptyStackException.
	 *
	 * @return zadnje pohranjeni element na stogu
	 */
	public Object peek() {
		int size = collection.size();
		if (size == 0) {
			throw new EmptyStackException();
		}
		return collection.get(size - 1);
	}

	/**
	 * Briše cjelokupni sadržaj stoga, odnosno sve pohranjene elemente.
	 */
	public void clear() {
		collection.clear();
	}

}
