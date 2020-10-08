package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element koji predstavlja naziv varijable.
 *
 * @author Alen Magdić
 *
 */
public class ElementVariable extends Element {
	/** Naziv varijable **/
	private String name;

	/** Konstruktor. Prima referencu na naziv varijable. **/
	public ElementVariable(String name) {
		super();
		this.name = name;
	}

	@Override
	public String asText() {
		return name;
	}
}
