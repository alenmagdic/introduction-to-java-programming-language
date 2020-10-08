package hr.fer.zemris.java.webserver;

/**
 * Sučelje koje sadrži metodu za obradu zahtjeva sa zadanom url putanjom.
 *
 * @author Alen Magdić
 *
 */
public interface IDispatcher {
	/**
	 * Obrađuje zahtjev sa zadanom url putanjom.
	 *
	 * @param urlPath
	 *            url putanja zahtjeva
	 * @throws Exception
	 *             ako se pojavi problem u obradi zahtjeva
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
