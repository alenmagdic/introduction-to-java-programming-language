package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element koji predstavlja funkciju.
 *
 * @author Alen Magdić
 *
 */
public class ElementFunction extends Element {
	/** Ime funkcije **/
	private String name;

	/**
	 * Konstruktor. Prima ime funkcije.
	 *
	 * @param name
	 *            ime funkcije
	 */
	public ElementFunction(String name) {
		super();
		this.name = name;
	}

	@Override
	public String asText() {
		return name;
	}

}
