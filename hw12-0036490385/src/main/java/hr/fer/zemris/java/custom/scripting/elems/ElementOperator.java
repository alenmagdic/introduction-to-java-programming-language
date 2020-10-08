package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element koji predstavlja matematički operator.
 *
 * @author Alen Magdić
 *
 */
public class ElementOperator extends Element {
	/** Simbol operatora **/
	private String symbol;

	/**
	 * Konstruktor. Prima simbol operatora.
	 *
	 * @param symbol
	 *            simbol operatora
	 */
	public ElementOperator(String symbol) {
		super();
		this.symbol = symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}
}
