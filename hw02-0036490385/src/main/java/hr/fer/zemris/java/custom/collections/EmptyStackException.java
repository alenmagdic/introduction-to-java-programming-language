package hr.fer.zemris.java.custom.collections;

/**
 * Iznimka koja se baca kada se pokuša dohvatiti element sa praznog stoga.
 *
 * @author Alen Magdić
 *
 */
@SuppressWarnings("serial")
public class EmptyStackException extends RuntimeException {

	/**
	 * Defaultni konstruktor.
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Konstruktor koji prima poruku kao argument.
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
