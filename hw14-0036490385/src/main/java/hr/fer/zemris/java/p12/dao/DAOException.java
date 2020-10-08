package hr.fer.zemris.java.p12.dao;

/**
 * An exception that is thrown when there is a problem with accessing some data.
 *
 * @author Alen Magdić
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public DAOException() {
	}

	/**
	 * Constructor.
	 *
	 * @param message
	 *            exception message
	 * @param cause
	 *            cause of exception
	 * @param enableSuppression
	 *            whether or not suppression is enabled or disabled
	 * @param writableStackTrace
	 *            whether or not the stack trace should be writable
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor.
	 *
	 * @param message
	 *            exception message
	 * @param cause
	 *            cause of exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 *
	 * @param message
	 *            exception message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @param cause
	 *            cause of exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}