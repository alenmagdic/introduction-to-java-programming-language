package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.polls.PollOption;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * A web servlet that generates a html page containing poll results for the
 * specified poll (specified through url parameter pollID). This page contains a
 * table representation of poll results, a graphical representation of poll
 * results, a link for downloading poll results in a form of an xls document,
 * and links to some information of the poll winning option/s.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeRezultatiServlet", urlPatterns = { "/servleti/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {
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

		Set<PollOption> results = DAOProvider.getDao().getPollResults(pollID);
		Set<PollOption> winners = getWinners(results);
		req.setAttribute("voteresults", results);
		req.setAttribute("winners", winners);
		req.setAttribute("pollID", pollID);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Gets the set of poll winners from the specified poll results. This method
	 * expects that the specified results have already been appropriately
	 * sorted, but since it is a private method it doesn't strictly expect an
	 * instance of {@link TreeSet} as parameter so that the caller has a freedom
	 * of giving an instance of {@link LinkedHashSet} which contains sorted
	 * data.
	 *
	 * @param results
	 *            poll results
	 * @return set of poll winners
	 */
	private Set<PollOption> getWinners(Set<PollOption> results) {
		Set<PollOption> winners = new LinkedHashSet<>();
		if (results.size() == 0) {
			return winners;
		}

		int maxVotes = results.iterator().next().getVotesCount();

		for (PollOption opt : results) {
			if (opt.getVotesCount() < maxVotes) {
				break;
			}
			winners.add(opt);
		}
		return winners;
	}
}
