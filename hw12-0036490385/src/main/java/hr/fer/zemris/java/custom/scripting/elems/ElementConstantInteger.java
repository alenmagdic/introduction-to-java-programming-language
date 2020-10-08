package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element koji predstavlja cjelobrojnu konstantu.
 *
 * @author Alen Magdić
 *
 */
public class ElementConstantInteger extends Element {
	/** Vrijednost cjelobrojne konstante **/
	private int value;

	/**
	 * Konstruktor. Prima vrijednost cjelobrojne konstante.
	 *
	 * @param value
	 *            vrijednost cjelobrojne konstante
	 */
	public ElementConstantInteger(int value) {
		super();
		this.value = value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
