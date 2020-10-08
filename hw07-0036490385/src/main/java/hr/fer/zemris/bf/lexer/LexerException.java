package hr.fer.zemris.bf.lexer;

/**
 * This exception is meant to be used by Lexer when it finds an invalid piece of
 * text data that can not be tokenized.
 *
 * @author Alen Magdić
 *
 */
public class LexerException extends RuntimeException {

	/** Serial version UID **/
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LexerException() {
		super();
	}

	/**
	 * Constructor with a message.
	 *
	 * @param message
	 *            a message
	 */
	public LexerException(String message) {
		super(message);
	}
}
