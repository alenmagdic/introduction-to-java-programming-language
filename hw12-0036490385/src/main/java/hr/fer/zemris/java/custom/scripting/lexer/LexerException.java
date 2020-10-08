package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Iznimka nastala pri jezičnoj analizi nekog teksta.
 *
 * @author Alen Magdić
 *
 */
public class LexerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/** Defaultni konstruktor. **/
	public LexerException() {
		super();
	};

	/** Konstruktor s porukom. **/
	public LexerException(String message) {
		super(message);
	}
}
