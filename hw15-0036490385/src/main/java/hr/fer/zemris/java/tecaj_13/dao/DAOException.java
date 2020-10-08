package hr.fer.zemris.java.tecaj_13.dao;

/**
 * Iznimka nastali pri dohvatu ili zapisu podataka.
 *
 * @author Alen Magdić
 *
 */
public class DAOException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 *
	 * @param message
	 *            poruka iznimke
	 * @param cause
	 *            uzrok iznimke
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor.
	 *
	 * @param message
	 *            poruka iznimke
	 */
	public DAOException(String message) {
		super(message);
	}
}