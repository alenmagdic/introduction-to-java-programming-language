package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.util.Util;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Glavna, odnosno početna stranica bloga. Sadrži obrazac za prijavu (ako već
 * nije prijavljen), link za registraciju, te popis svih autora.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "mainPageServlet", urlPatterns = { "/servleti/main" })
public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> authors = DAOProvider.getDAO().getAuthors();
		req.setAttribute("authors", authors);
		req.getRequestDispatcher("/WEB-INF/pages/mainPage.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		String pass = req.getParameter("password");

		BlogUser user = DAOProvider.getDAO().getAuthorByNick(nick);
		if (user != null) {
			if (user.getPasswordHash().equals(Util.calculateHash(pass))) {
				req.getSession().setAttribute("current.user.id", user.getId());
				req.getSession().setAttribute("current.user.fn", user.getFirstName());
				req.getSession().setAttribute("current.user.ln", user.getLastName());
				req.getSession().setAttribute("current.user.nick", user.getNick());
				resp.sendRedirect(req.getContextPath() + "/servleti/main");
				return;
			}
		}

		req.setAttribute("invalidDataMsg", "Wrong username or password!");
		req.setAttribute("nick", nick);
		doGet(req, resp);
	}
}
