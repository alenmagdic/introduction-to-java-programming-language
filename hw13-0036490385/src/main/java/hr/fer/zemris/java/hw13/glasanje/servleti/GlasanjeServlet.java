package hr.fer.zemris.java.hw13.glasanje.servleti;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw13.glasanje.Band;
import hr.fer.zemris.java.hw13.util.PollDataIO;

/**
 * A webservlet that creates a html page offering the client to vote for his
 * favourite band among the given bands.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeServlet", urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Set<Band> bands = PollDataIO.loadVoteDataDefinition();
		req.getSession().setAttribute("bands", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
