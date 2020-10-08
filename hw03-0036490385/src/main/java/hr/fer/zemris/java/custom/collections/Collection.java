package hr.fer.zemris.java.custom.collections;

/**
 * Klasa koja sadrži metode za pohranu i upravljanje kolekcijom podataka. Nije
 * ju moguće instancirati, već ju je samo moguće naslijediti.
 *
 * @author Alen Magdić
 *
 */
public class Collection {

	/**
	 * Defaultni konstruktor. S obzirom da klasu nije moguće izravno
	 * instancirati, ovaj se konstruktor poziva samo iz konstruktora razreda
	 * koji je izveden iz ovoga razreda.
	 */
	protected Collection() {

	}

	/**
	 * Vraća true ako je kolekcija prazna, inače vraća false.
	 *
	 * @return true ako je kolekcija prazna, inače false
	 */
	public boolean isEmpty() {
		return size() == 0;

	}

	/**
	 * Vraća broj pohranjenih elemenata u kolekciji.
	 *
	 * @return broj pohranjenih elemenata u kolekciji
	 */
	public int size() {
		return 0;
	}

	/**
	 * Dodaje zadani objekt u kolekciju.
	 *
	 * @param value
	 *            objekt koji je potrebno dodati u kolekciju
	 */
	public void add(Object value) {

	}

	/**
	 * Vraća true ako je zadani objekt sadržan u kolekciji. Inače vraća false.
	 *
	 * @param value
	 *            objekt čiju je prisutnost u kolekciji potrebno provjeriti
	 * @return true ako je zadani objekt sadržan u kolekciji, inače false
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Uklanja zadani objekt iz kolekcije. Ukoliko zadani objekt ne bude
	 * pronađen, odnosno nije prisutan u kolekciji, tada metoda vraća false. U
	 * slučaju pronalska i uklanjan vraća true.
	 *
	 * @param value
	 *            objekt kojeg je potrebno ukloniti
	 * @return true ako je objekt pronađen i uklonjen, false ako predmet nije
	 *         pronađen
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Vraća polje elemenata pohranjenih u kolekciji.
	 *
	 * @return polje elemenata pohranjenih u kolekciji
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Procesira sve elemente kolekcije kroz dani procesor.
	 *
	 * @param processor
	 *            procesor koji se koristi za procesiranje elemenata kolekcije
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Dodaje sve elemente iz zadane kolekcije u ovu kolekciju.
	 *
	 * @param other
	 *            kolekcija čije je elemente potrebno dodati u ovu kolekciju
	 */
	public void addAll(Collection other) {
		class LocalProcessor extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}

		other.forEach(new LocalProcessor());
	}

	/**
	 * Briše sve elemente iz kolekcije.
	 */
	public void clear() {

	}
}
