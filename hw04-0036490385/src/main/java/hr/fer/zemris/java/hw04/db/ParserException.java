package hr.fer.zemris.java.hw04.db;

/**
 * This exception is meant to be used by Parser when it finds an invalid piece
 * of code that can not be parsed.
 *
 * @author Alen Magdić
 *
 */
public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 *
	 */
	public ParserException() {
		super();
	}

	/**
	 * Constructor with a message.
	 *
	 * @param message
	 *            a message
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Constructor with a message and an exception that caused this exception.
	 *
	 * @param message
	 *            a message
	 * @param ex
	 *            an exception that caused this exception
	 */
	public ParserException(String message, Exception ex) {
		super(message, ex);
	}
}
