package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Ova iznimka označava nemogućnost dohvaćanja podatka sa stoga s obzirom da je
 * stog prazan.
 *
 * @author Alen Magdić
 *
 */
public class EmptyStackException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor.
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Konstruktor s zadanom porukom.
	 *
	 * @param message
	 *            poruka
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
