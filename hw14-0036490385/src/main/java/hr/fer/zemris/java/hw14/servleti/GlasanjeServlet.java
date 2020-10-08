package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.polls.Poll;
import hr.fer.zemris.java.hw14.polls.PollOption;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * A web servlet that creates a html page offering the client to vote in the
 * specified poll. The poll is specified through url parameter pollID. If the
 * client gives invalid poll id (something that is not a number), or does not
 * give poll id, the servlet will send an error with code 400.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeServlet", urlPatterns = { "/servleti/glasanje" })
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollID;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
		} catch (NumberFormatException ex) {
			resp.sendError(400, "Invalid parameters given");
			return;
		}

		Set<PollOption> options = DAOProvider.getDao().getPollOptions(pollID);
		req.setAttribute("options", options);

		Poll poll = DAOProvider.getDao().getPoll(pollID);
		req.setAttribute("poll", poll);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
