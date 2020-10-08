package hr.fer.zemris.java.hw06.shell;

/**
 * An exception thrown when there is a problem with the receiving user input or
 * making an output in the {@link MyShell} program.
 *
 * @author Alen Magdić
 *
 */
public class ShellIOException extends RuntimeException {
	/** Serial version UID **/
	private static final long serialVersionUID = 1L;

	/** Default constructor. **/
	public ShellIOException() {
		super();
	}

	/**
	 * Constructor with a message.
	 *
	 * @param message
	 *            a message
	 */
	public ShellIOException(String message) {
		super(message);
	}

	/**
	 * Constructor with a message and an exception that caused this exception to
	 * be thrown.
	 *
	 * @param message
	 *            a message
	 * @param ex
	 *            an exception that caused this exception to be thrown
	 */
	public ShellIOException(String message, Exception ex) {
		super(message, ex);
	}
}
