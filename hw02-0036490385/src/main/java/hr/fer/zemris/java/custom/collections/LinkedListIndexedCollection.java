package hr.fer.zemris.java.custom.collections;

/**
 * Kolekcija koja koristi dvostruko povezanu listu za pohranu podataka. Sadrži
 * razne metode za upravljanje sadržajem kolekcije. Kolekcija dozvoljava pohranu
 * duplikata, ali null vrijednosti nisu dozvoljene stoga metoda u pokušaju
 * dodavanja dodavanja null baca iznimku IllegalArgumentException.
 *
 * @author Alen Magdić
 *
 */
public class LinkedListIndexedCollection extends Collection {
	/** Broj elemenata trenutno pohranjenih u kolekciju. */
	private int size;
	/** Referenca na prvi objekt u kolekciji, odnosno na onaj sa indexom 0. */
	private ListNode first;
	/**
	 * Referenca na zadnji objekt u kolekciji, odnosno onaj sa indexom
	 * brojElemenata-1.
	 */
	private ListNode last;

	/**
	 * Pomoćna statička klasa koja predstavlja atom liste. Sadrži reference na
	 * lijevi i desni atom te pohranjeni element kolekcije.
	 */
	private static class ListNode {
		/**
		 * Referenca na prethodni atom u listi. Ako je ovaj atom smješten na
		 * početku liste, tada ova referenca ima vrijednost null.
		 */
		private ListNode previousEl;
		/**
		 * Referenca na sljedeći atom u listi. Ako je ovaj atom smješten na
		 * posljednjem mjestu u listi, tada ova referenca ima vrijednost null.
		 */
		private ListNode nextEl;
		/**
		 * Referenca na objekt pohranjen u ovom atomu.
		 */
		private Object value;

		/**
		 * Konstruktor. Prihvaća reference na prethodni atom liste, na sljedeći
		 * atom te na vrijednost koja se pohranjuje u samom atomu.
		 *
		 * @param previousEl
		 *            referenca na prethodni atom u listi
		 * @param nextEl
		 *            referenca na sljedeći atom u listi
		 * @param value
		 *            referenca na vrijednost koju ovaj atom pohranjuje
		 */
		public ListNode(ListNode previousEl, ListNode nextEl, Object value) {
			this.previousEl = previousEl;
			this.nextEl = nextEl;
			this.value = value;
		}
	}

	/** Defaultni konstruktor. Inicijalizira praznu listu. */
	public LinkedListIndexedCollection() {
		first = null;
		last = null;
	}

	/**
	 * Konstruktor. Prima referencu na drugu kolekciju iz koje preuzima sve
	 * pohranjene vrijednosti.
	 *
	 * @param collection
	 *            kolekcija iz koje se preuzimaju pohranjene vrijednosti
	 */
	public LinkedListIndexedCollection(Collection collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Argument null nije prihvatljiv.");
		}
		addAll(collection);
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
			throw new IllegalArgumentException("Kolekcija ne podržava pohranu null vrijednosti.");
		}

		ListNode newNode = new ListNode(last, null, value);
		if (first == null) {
			first = newNode;
		} else {
			newNode.previousEl.nextEl = newNode;
		}
		last = newNode;
		size++;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index == -1) {
			return false;
		}
		remove(index);
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

		ListNode node = getNode(index); // dohvat node-a koji se briše
		if (index == 0) {
			first = node.nextEl;
		} else {
			node.previousEl.nextEl = node.nextEl;
		}
		if (index == size - 1) {
			last = node.previousEl;
		}
		node.nextEl.previousEl = node.previousEl;
		size--;
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		ListNode node = first;
		for (int i = 0; i < size; i++) {
			array[i] = node.value;
			node = node.nextEl;
		}
		return array;
	}

	@Override
	public void forEach(Processor processor) {
		ListNode node = first;
		for (int i = 0; i < size; i++) {
			processor.process(node.value);
			node = node.nextEl;
		}
	}

	@Override
	public void clear() {
		first = null;
		last = null;
		size = 0;
	}

	/**
	 * Vraća objekt sa zadanim indexom. Index mora biti u rasponu
	 * [0,brojElemenata-1]. Ako zadani indeks nije u dozvoljenom rasponu, biti
	 * će bačena iznimka IndexOutOfBoundsException. Prosječna složenost metode
	 * je n.
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
		return getNode(index).value;
	}

	/**
	 * Dohvaća atom sa zadanim indexom.
	 *
	 * @param index
	 *            index atoma kojeg je potrebno dohvatiti
	 * @return atom sa zadanim indexom
	 */
	private ListNode getNode(int index) {
		// ako je index bliže početku liste, traži od početka
		if (index < size / 2) {
			ListNode node = first;
			for (int i = 0; i < index; i++) {
				node = node.nextEl;
			}
			return node;
		}

		// inače traži od kraja
		ListNode node = last;
		for (int i = size - 1; i > index; i--) {
			node = node.previousEl;
		}
		return node;
	}

	/**
	 * Umeće zadani objekt na zadanu poziciju u kolekciji. Pozicija mora biti u
	 * rasponu [0, brojElemenata]. Ako zadani indeks nije u dozvoljenom rasponu,
	 * biti će bačena iznimka IndexOutOfBoundsException. Složenost metode je
	 * konstanta.
	 *
	 * @param value
	 *            objekt koji je potrebno dodati u kolekciju
	 * @param position
	 *            pozicija na koju je potrebno dodati objekt, broj u rasponu [0,
	 *            brojElemenata]
	 */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException("Kolekcija ne podržava pohranu null vrijednosti.");
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException(
					"Pozicija elementa kojeg je potrebno umetnuti treba biti broj od 0 do trenutne veličine kolekcije.");
		}
		if (position == size) {
			add(value);
			return;
		}

		ListNode node = getNode(position); // dohvat node-a koji se nalazi na
		// mjestu na koje se umeće novi
		// element
		ListNode newNode = new ListNode(node.previousEl, node, value);
		if (position == 0) {
			first = newNode;
		} else {
			newNode.previousEl.nextEl = newNode;
		}
		size++;
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
		ListNode node = first;
		for (int i = 0; i < size; i++) {
			if (node.value.equals(value)) {
				return i;
			}
			node = node.nextEl;
		}
		return -1;
	}

}
