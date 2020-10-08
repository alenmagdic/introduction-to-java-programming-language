package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet za dodavanje novog ili uređivanje postojećeg zapisa.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "editEntryServlet", urlPatterns = { "/servleti/author/editentry" })
public class EditEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getSession().getAttribute("current.user.id") == null) {
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
			return;
		}

		String entryId = (String) req.getAttribute("entry_id");
		if (entryId == null) {
			req.setAttribute("blogEntry", null);
		} else {
			Long id = null;
			try {
				id = Long.valueOf(entryId);
			} catch (NumberFormatException ex) {
				resp.sendError(400);
			}

			BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
			if (blogEntry != null) {
				req.setAttribute("blogEntry", blogEntry);
			} else {
				resp.sendError(404);
			}
		}

		req.getRequestDispatcher("/WEB-INF/pages/editEntry.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getSession().getAttribute("current.user.id") == null) {
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
			return;
		}

		String entId = req.getParameter("entry_id");
		if (entId == null) {

			BlogEntry blogEntry = new BlogEntry();
			blogEntry.setCreatedAt(new Date());
			blogEntry.setLastModifiedAt(blogEntry.getCreatedAt());
			blogEntry.setTitle(req.getParameter("title"));
			blogEntry.setText(req.getParameter("text"));
			BlogUser creator = DAOProvider.getDAO()
					.getAuthorByNick((String) req.getSession().getAttribute("current.user.nick"));
			blogEntry.setCreator(creator);

			DAOProvider.getDAO().addNewEntry(blogEntry);
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + creator.getNick());
			return;
		}

		Long id;
		try {
			id = Long.valueOf(entId);
		} catch (NumberFormatException ex) {
			resp.sendError(400);
			return;
		}

		BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
		blogEntry.setLastModifiedAt(new Date());
		blogEntry.setTitle(req.getParameter("title"));
		blogEntry.setText(req.getParameter("text"));

		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + blogEntry.getCreator().getNick());

	}
}
