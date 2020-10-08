package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Klasa koja predstavlja iznimku nastalu parsiranjem nekog koda.
 *
 * @author Alen Magdić
 *
 */
public class SmartScriptParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor.
	 *
	 */
	public SmartScriptParserException() {
		super();
	}

	/**
	 * Konstruktor. Prima poruku.
	 *
	 * @param message
	 *            poruka
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

	/**
	 * Konstruktor koji danu iznimku omata kao SmartScriptParserException.
	 *
	 * @param ex
	 *            iznimka koju je potrebno omotati
	 */
	public SmartScriptParserException(Exception ex) {
		super(ex);
	}
}
