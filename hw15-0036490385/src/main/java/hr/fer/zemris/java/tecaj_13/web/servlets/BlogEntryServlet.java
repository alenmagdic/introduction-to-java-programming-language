package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet koji generira stranicu blog zapisa. Na njoj ispisuje sadržaj zapisa,
 * podatke o zapisu, komentare zapisa te obrazac za dodavanje novih komentara.
 * Također provodi dodavanje komentara preko metode POST.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "blogEntryServlet", urlPatterns = { "/servleti/blog_entry" })
public class BlogEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = (String) req.getAttribute("authorNick");
		if (nick == null) {
			resp.sendError(400);
			return;
		}

		Long id;
		try {
			id = Long.valueOf((String) req.getAttribute("entry_id"));
		} catch (NumberFormatException ex) {
			resp.sendError(400);
			return;
		}

		BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
		if (blogEntry == null) {
			resp.sendError(404);
			return;
		}

		BlogUser author = DAOProvider.getDAO().getAuthorByNick(nick);
		if (author.getId() != blogEntry.getCreator().getId()) {
			resp.sendError(400);
			return;
		}

		req.setAttribute("blogEntry", blogEntry);
		req.setAttribute("authorNick", author.getNick());

		req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String entId = req.getParameter("entry_id");
		if (entId == null) {
			resp.sendError(400);
		}

		Long id;
		try {
			id = Long.valueOf(entId);
		} catch (NumberFormatException ex) {
			resp.sendError(400);
			return;
		}

		BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);

		BlogComment blogComment = new BlogComment();
		blogComment.setUsersEMail(req.getParameter("email"));
		blogComment.setPostedOn(new Date());
		blogComment.setMessage(req.getParameter("text"));
		blogComment.setBlogEntry(blogEntry);

		DAOProvider.getDAO().addNewComment(blogComment);

		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + blogEntry.getCreator().getNick() + "/"
				+ blogEntry.getId());
	}
}
