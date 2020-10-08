package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.polls.Poll;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * A webservlet that shows a list of available polls for voting. Every poll is
 * linked to a page that will enable the client to vote in the selected poll.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "pollsServlet", urlPatterns = { "/servleti/index.html" })
public class PollsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Poll> polls = DAOProvider.getDao().getPolls();
		req.setAttribute("polls", polls);
		req.getRequestDispatcher("/WEB-INF/pages/pollList.jsp").forward(req, resp);
	}
}
