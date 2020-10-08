package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Node koji sadrži varijabilni broj elemenata.
 *
 * @author Alen Magdić
 *
 */
public class EchoNode extends Node {
	/** Polje elemenata **/
	private Element[] elements;

	/**
	 * Konstruktor. Prima referencu na polje elemenata.
	 *
	 * @param elements
	 *            referenca na polje elemenata
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}

	/**
	 * Vraća polje elemenata.
	 *
	 * @return polje elemenata
	 */
	public Element[] getElements() {
		return elements;
	}
}
