package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A web servlet that is activated when the client calls the homepage of the
 * application (/index.html). It simply redirects client to homepage at url with
 * pattern /servleti/index.html.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "homepageRedirectServlet", urlPatterns = { "/index.html" })
public class HomepageRedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(req.getContextPath() + "/servleti/index.html");
	}
}
