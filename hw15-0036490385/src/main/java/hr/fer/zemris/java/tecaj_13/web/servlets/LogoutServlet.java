package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji provodi odlogiranje korisnika. Nakon odlogiranja, šalje
 * korisnika na početnu stranicu.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "logoutServlet", urlPatterns = { "/servleti/logout" })
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();
		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
}
