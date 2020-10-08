package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element koji predstavlja String.
 *
 * @author Alen Magdić
 *
 */
public class ElementString extends Element {
	/** Referenca na string kojeg ovaj element predstavlja **/
	private String value;

	/**
	 * Konstruktor. Prima referencu na string kojeg će predstavljati.
	 *
	 * @param value
	 *            referenca na string
	 */
	public ElementString(String value) {
		super();
		this.value = value;
	}

	@Override
	public String asText() {
		return value;
	}
}
