package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji obrađuje sve zahtjeve sa patternom /servleti/author/, odnosno
 * analizira ostatak putanje i šalje odgovarajućem servletu na obradu.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "authorRedirectionServlet", urlPatterns = { "/servleti/author/*" })
public class AuthorRedirectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] pathParts = req.getPathInfo().substring(1).split("/");

		if (pathParts.length == 1) {
			req.setAttribute("nick", pathParts[0]);
			req.getRequestDispatcher("/servleti/author/entries").forward(req, resp);
			return;
		}

		if (pathParts.length != 2) {
			resp.sendError(404);
			return;
		}

		if (pathParts[1].equals("new") || pathParts[1].equals("edit")) {
			String entryId = req.getParameter("entry_id");
			req.setAttribute("entry_id", entryId);
			req.getRequestDispatcher("/servleti/author/editentry").forward(req, resp);
		} else {
			req.setAttribute("entry_id", pathParts[1]);
			req.setAttribute("authorNick", pathParts[0]);
			req.getRequestDispatcher("/servleti/blog_entry").forward(req, resp);
		}
	}
}
