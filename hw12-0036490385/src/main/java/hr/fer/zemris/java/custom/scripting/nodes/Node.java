package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Općeniti model čvora stabla koje može sadržati varijabilni broj djece. Klasa
 * sadrži metode za dodavanje djece čvoru kao i za dohvaćenje istih.
 *
 * @author Alen Magdić
 *
 */
public abstract class Node {
	/**
	 * Kolekcija u koju se pohranjuju djeca čvora.
	 */
	private ArrayIndexedCollection children;

	/**
	 * Evidentira zadani čvor kao dijete ovoga čvora.
	 *
	 * @param child
	 *            čvor kojeg je potrebno evidentirati kao dijete ovoga čvora
	 */
	public void addChildNode(Node child) {
		if (child == null) {
			throw new IllegalArgumentException("Argument given: null");
		}

		if (children == null) {
			children = new ArrayIndexedCollection();
		}

		children.add(child);
	}

	/**
	 * Vraća broj djece ovog čvora.
	 *
	 * @return broj djece ovog čvora
	 */
	public int numberOfChildren() {
		return children == null ? 0 : children.size();
	}

	/**
	 * Vraća dijete sa zadanim indexom.
	 *
	 * @param index
	 *            index dijeteta koje je potrebno dohvatiti
	 * @return dijete sa zadanim indexom
	 */
	public Node getChild(int index) {
		if (index < 0 || index >= numberOfChildren()) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}

		return (Node) children.get(index);
	}

	/**
	 * Prihvaća zadani posjetitelj na način da poziva odgovarajuću metodu
	 * posjetitelja.
	 *
	 * @param visitor
	 *            posjetitelj stabla
	 */
	public abstract void accept(INodeVisitor visitor);
}
