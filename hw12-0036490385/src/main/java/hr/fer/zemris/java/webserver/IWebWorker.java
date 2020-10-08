package hr.fer.zemris.java.webserver;

/**
 * Sučelje koje predstavlja "radnika" koji na sebi određen način procesira
 * zahtjev opisan objektom {@link RequestContext}.
 *
 * @author Alen Magdić
 *
 */
public interface IWebWorker {
	/**
	 * Procesira zahtjev sa zadanim kontekstom.
	 *
	 * @param context
	 *            kontekst zahtjeva
	 * @throws Exception
	 *             ako se pojavi problem u procesiranju zahtjeva
	 */
	public void processRequest(RequestContext context) throws Exception;
}
