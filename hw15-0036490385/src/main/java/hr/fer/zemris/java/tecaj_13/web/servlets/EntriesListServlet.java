package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;

/**
 * Servlet koji ispisuje popis svih zapisa nekog korisnika.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "entriesListServlet", urlPatterns = { "/servleti/author/entries" })
public class EntriesListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = (String) req.getAttribute("nick");
		List<BlogEntry> entries = DAOProvider.getDAO().getUserEntries(nick);
		req.setAttribute("entries", entries);
		req.setAttribute("noEntries", entries.isEmpty());
		req.setAttribute("nick", nick);
		req.getRequestDispatcher("/WEB-INF/pages/userEntries.jsp").forward(req, resp);
	}
}
