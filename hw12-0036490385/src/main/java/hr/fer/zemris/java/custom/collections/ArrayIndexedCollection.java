package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;

/**
 * Kolekcija koja koristi polje za pohranu podataka. Sadrži razne metode za
 * upravljanje sadržajem kolekcije. Kolekcija dozvoljava pohranu duplikata, ali
 * null vrijednosti nisu dozvoljene te metoda u pokušaju dodavanja istih baca
 * iznimku IllegalArgumentException.
 *
 * @author Alen Magdić
 *
 */
public class ArrayIndexedCollection extends Collection {
	/** Broj elemenata trenutno pohranjenih u kolekciji */
	private int size;
	/** Broj trenutno alociranih mjesta za pohranu elemenata kolekcije. */
	private int capacity;
	/** Polje elemenata pohranjenih u kolekciji */
	private Object[] elements;
	/** Početni defaultni kapacitet kolekcije */
	private final static int DEFAULT_CAPACITY = 16;

	/**
	 * Defaultni konstruktor.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Konstruktor. Prima početni kapacitet kolekcije, odnosno broj mjesta koje
	 * je početno potrebno alocirati za pohranu podataka.
	 *
	 * @param initialCapacity
	 *            početni kapacitet kolekcije, odnosno broj mjesta koje je
	 *            početno potrebno alocirati za pohranu podataka
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Initial capacity has to be a positive number.");
		}
		capacity = initialCapacity;
		elements = new Object[capacity];
	}

	/**
	 * Konstruktor. Prima kolekciju iz koje preuzima sve elemente.
	 *
	 * @param collection
	 *            kolekcija iz koje se preuzimaju elementi
	 */
	public ArrayIndexedCollection(Collection collection) {
		this(collection, collection.size());
	}

	/**
	 * Konstruktor. Prima kolekciju iz koje se preuzimaju svi elementi te
	 * početni kapacitet kolekcije, odnosno broj mjesta koje je je početno
	 * potrebno alocirati za pohranu podataka.
	 *
	 * @param collection
	 *            kolekcija iz koje se preuzimaju elementi
	 * @param initialCapacity
	 *            početni kapacitet kolekcije, odnosno broj mjesta koje je
	 *            početno potrebno alocirati za pohranu podataka.
	 */
	public ArrayIndexedCollection(Collection collection, int initialCapacity) {
		if (initialCapacity < collection.size()) {
			throw new IllegalArgumentException(
					"Initial capacity has to be greater or equal than the size of the specified collection.");
		}
		capacity = initialCapacity;
		elements = Arrays.copyOf(collection.toArray(), capacity);
		size += collection.size();
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Dodaje zadani objekt u kolekciju. Prosječna složenost metode je
	 * konstanta.
	 *
	 * @param value
	 *            objekt koji je potrebno dodati u kolekciju
	 */
	@Override
	public void add(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("This collection does not support null values.");
		}

		realloc();

		elements[size] = value;
		size++;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}

	/**
	 * Vraća objekt sa zadanim indexom. Index mora biti u rasponu
	 * [0,brojElemenata-1]. Ako zadani indeks nije u dozvoljenom rasponu, biti
	 * će bačena iznimka IndexOutOfBoundsException. Prosječna složenost metode
	 * je konstanta.
	 *
	 * @param index
	 *            index objekta kojeg je potrebno dohvatiti, broj u rasponu [0,
	 *            brojElemenata-1]
	 * @return objekt sa zadanim indexom
	 */
	public Object get(int index) {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		return elements[index];
	}

	@SuppressWarnings("unused")
	@Override
	public void clear() {
		for (Object object : elements) {
			object = null;
		}
		size = 0;
	}

	/**
	 * Umeće zadani objekt na zadanu poziciju u kolekciji. Pozicija mora biti u
	 * rasponu [0, brojElemenata]. Ako zadani indeks nije u dozvoljenom rasponu,
	 * biti će bačena iznimka IndexOutOfBoundsException. Složenost metode je n.
	 *
	 * @param value
	 *            objekt koji je potrebno dodati u kolekciju
	 * @param position
	 *            pozicija na koju je potrebno dodati objekt, broj u rasponu [0,
	 *            brojElemenata]
	 */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException("This collection does not support null values.");
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException(
					"Current size of the collection: " + size + "; specified position: " + position);
		}

		realloc();

		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
		size++;
	}

	/**
	 * Ukoliko je polje elemenata popunjeno, poduplava kapacitet polja. Inače,
	 * ne radi ništa.
	 */
	private void realloc() {
		if (size == capacity) {
			capacity *= 2;
			elements = Arrays.copyOf(elements, capacity * 2);
		}
	}

	/**
	 * Vraća indeks zadanog objekta. Ako objekt nije prisutan u kolekciji,
	 * metoda vraća -1. Prosječna složenost metode je n.
	 *
	 * @param value
	 *            objekt čiji je index potrebno pronaći
	 * @return indeks zadanog objekta ukoliko je objekt prisutan u kolekciji,
	 *         inače -1
	 */
	public int indexOf(Object value) {
		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean remove(Object value) {
		if (!contains(value)) {
			return false;
		}
		remove(indexOf(value));
		return true;
	}

	/**
	 * Uklanja element sa zadanim indeksom iz kolekcije. Indeks treba biti broj
	 * u rasponu [0, brojElemenata-1]. Ako zadani indeks nije u dozvoljenom
	 * rasponu, biti će bačena iznimka IndexOutOfBoundsException.
	 *
	 * @param index
	 *            indeks elementa kojeg je potrebno ukloniti, broj u rasponu [0,
	 *            brojElemenata-1]
	 */
	public void remove(int index) {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}

		elements[size - 1] = null;
		size--;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}

}
