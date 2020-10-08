package hr.fer.zemris.java.hw03.prob1;

/**
 * Iznimka nastala pri jezičnoj analizi nekog teksta.
 *
 * @author Alen Magdić
 *
 */
public class LexerException extends RuntimeException {

	/** Defaultni konstruktor. **/
	public LexerException() {
		super();
	};

	/** Konstruktor s porukom. **/
	public LexerException(String message) {
		super(message);
	}
}
