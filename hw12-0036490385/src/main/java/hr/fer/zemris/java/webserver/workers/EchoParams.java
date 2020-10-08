package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Set;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik {@link IWebWorker} koji klijentu šalje tablicu sa parametrima koje je
 * klijent u zahtjevu poslao.
 *
 * @author Alen Magdić
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		Set<String> params = context.getParameterNames();
		try {
			context.write("<html><body>");
			context.write("<h1>Parameters</h1>");
			context.write("<table border=\"1\">");
			for (String par : params) {
				context.write("<tr> <td>" + par + "</td>" + "<td>" + context.getParameter(par) + "</td></tr>");
			}
			context.write("</table>");
			context.write("</body></html>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
