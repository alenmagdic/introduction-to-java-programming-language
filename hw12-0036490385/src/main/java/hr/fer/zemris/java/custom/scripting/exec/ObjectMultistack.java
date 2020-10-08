package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa predstavlja multi-stog. To je zapravo posebna vrsta mape čiji su
 * ključevi imena stogova, a vrijednosti stogovi. Sadrže stadardne metode za
 * dohvat i postavljanje objekata na stog sa zadanim imenom.
 *
 * @author Alen Magdić
 *
 */
public class ObjectMultistack {
	/** Mapa za povezivanje imena stoga sa samim stogom. **/
	private Map<String, MultistackEntry> map;

	/**
	 * Defaultni konstruktor.
	 */
	public ObjectMultistack() {
		map = new HashMap<>();
	}

	/**
	 * Stavlja zadani objekt na stog sa zadanim imenom.
	 *
	 * @param name
	 *            ime stoga
	 * @param valueWrapper
	 *            omotač brojevne vrijednosti
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (name == null || valueWrapper == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		MultistackEntry stack = map.get(name);
		MultistackEntry newEntry = new MultistackEntry(stack, valueWrapper);
		map.put(name, newEntry);
	}

	/**
	 * Skida element sa stoga sa zadanim imenom.
	 *
	 * @param name
	 *            ime stoga
	 * @return element sa vrha zadanog stoga
	 */
	public ValueWrapper pop(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		MultistackEntry stack = peekEntry(name);
		map.put(name, stack.next);
		return stack.valueWrapper;
	}

	/**
	 * Dohvaća element sa vrha zadanog stoga, ali ga ne skida sa stoga.
	 *
	 * @param name
	 *            ime stoga
	 * @return element sa vrha zadanog stoga
	 */
	public ValueWrapper peek(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}
		return peekEntry(name).valueWrapper;
	}

	/**
	 * Metoda koja dohvaća {@link MultistackEntry} objekt sa vrha zadanog stoga.
	 *
	 * @param name
	 *            ime stoga
	 * @return {link MultistackEntry} objekt sa vrha zadanog stoga
	 */
	private MultistackEntry peekEntry(String name) {
		MultistackEntry stack = map.get(name);
		if (stack == null) {
			throw new EmptyStackException(
					"There is no any element associated with the specified key in the collection. The specified key: "
							+ name);
		}
		return stack;
	}

	/**
	 * Vraća true ako je stog sa zadanim imenom prazan.
	 *
	 * @param name
	 *            ime stoga
	 * @return true ako je stog sa zadanim imenom prazan
	 */
	public boolean isEmpty(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		return map.get(name) == null;
	}

	/**
	 * Klasa koja predstavlja jedan unos na stog. Sadrži objekt dodan na stog
	 * (objekt tipa {@link ValueWrapper}) te referencu na sljedeći unos na stogu
	 * (gledano s vrha stoga prema dolje).
	 *
	 * @author Alen Magdić
	 *
	 */
	private static class MultistackEntry {
		/**
		 * Referenca na sljedeći unos na stogu (gledano s vrha stoga prema
		 * dolje).
		 */
		private MultistackEntry next;
		/**
		 * Element dodan na stog.
		 */
		private ValueWrapper valueWrapper;

		/**
		 * Konstruktor.
		 *
		 * @param next
		 *            referenca na sljedeći unos na stogu (gledano s vrha stoga
		 *            prema dolje)
		 * @param valueWrapper
		 *            element koji se dodaje na stog
		 */
		public MultistackEntry(MultistackEntry next, ValueWrapper valueWrapper) {
			this.next = next;
			this.valueWrapper = valueWrapper;
		}
	}

}
