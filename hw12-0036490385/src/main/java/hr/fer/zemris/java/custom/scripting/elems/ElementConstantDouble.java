package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element koji predstavlja realnu konstantu.
 *
 * @author Alen Magdić
 *
 */
public class ElementConstantDouble extends Element {
	/** Vrijednost realne konstante **/
	private double value;

	/**
	 * Konstruktor. Prima vrijednost realne konstante.
	 *
	 * @param num
	 *            vrijednost realne konstante
	 */
	public ElementConstantDouble(double num) {
		super();
		this.value = num;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}
}
