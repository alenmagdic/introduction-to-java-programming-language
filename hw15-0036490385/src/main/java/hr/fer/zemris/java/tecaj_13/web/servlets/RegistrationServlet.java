package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.util.Util;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet koji provodi registraciju korisnika. Generira obrazac za unos
 * podataka korisnika, vrši provjeru ispravnosti i vrši pohranjivanje podataka o
 * novo-kreiranom korisniku.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "registrationServlet", urlPatterns = { "/servleti/register" })
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		boolean nickExists = DAOProvider.getDAO().getAuthorByNick(nick) != null;
		req.setAttribute("nickErrorMsg", nickExists ? "Unavailable nickname!" : "");

		if (nickExists) {
			req.setAttribute("nick", nick);
			req.setAttribute("firstName", firstName);
			req.setAttribute("lastName", lastName);
			req.setAttribute("email", email);
			doGet(req, resp);
		} else {
			DAOProvider.getDAO()
					.addNewUser(new BlogUser(firstName, lastName, nick, email, Util.calculateHash(password)));
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
		}
	}
}
