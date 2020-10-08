package hr.fer.zemris.java.custom.scripting.exec;

/**
 * This exception represents that there are no elements on the stack, i.e. it is
 * suppossed to be thrown when someone tries to pop or peek an element from
 * empty stack.
 *
 * @author Alen Magdić
 *
 */
public class EmptyStackException extends RuntimeException {

	/** Serial version **/
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Constructor that takes a message as a parameter.
	 *
	 * @param message
	 *            exception message
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
