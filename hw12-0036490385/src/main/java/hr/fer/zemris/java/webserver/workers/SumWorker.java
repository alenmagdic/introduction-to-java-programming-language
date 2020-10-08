package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik {@link IWebWorker} koji šalje klijentu zbroj dvaju brojeva koju je
 * klijent poslao kao parametre zahtjeva.
 *
 * @author Alen Magdić
 *
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String paramA = context.getParameter("a");
		String paramB = context.getParameter("b");
		int a = getParamAsInt(paramA, 1);
		int b = getParamAsInt(paramB, 2);
		context.setTemporaryParameter("zbroj", Integer.toString(a + b));
		context.setTemporaryParameter("a", Integer.toString(a));
		context.setTemporaryParameter("b", Integer.toString(b));
		context.getDispatcher().dispatchRequest("/private/calc.smscr");
	}

	/**
	 * Metoda koja dani parametar vraća u obliku int vrijednosti. Ako se zadani
	 * parametar ne može protumačiti kao cijeli broj, vraća zadanu defaultnu
	 * vrijednost.
	 *
	 * @param param
	 *            parametar
	 * @param defaultValue
	 *            defaultna vrijednost ukoliko parametar nije cijeli brojs
	 * @return int reprezentacija zadanog parametra
	 */
	public int getParamAsInt(String param, Integer defaultValue) {
		int p;
		try {
			p = Integer.parseInt(param);
		} catch (NumberFormatException ex) {
			p = defaultValue;
		}
		return p;
	}

}
