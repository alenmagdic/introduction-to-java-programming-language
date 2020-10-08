package hr.fer.zemris.java.hw13.glasanje.servleti;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw13.glasanje.Band;
import hr.fer.zemris.java.hw13.glasanje.PollResult;
import hr.fer.zemris.java.hw13.util.PollDataIO;

/**
 * A web servlet that generates a html page containing poll results. This page
 * contains a table representation of poll results, a graphical representation
 * of poll results, a link for downloading poll results in a form of an xls
 * document, and links to some song/s of the poll winning band/s.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeRezultatiServlet", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Set<PollResult> results = PollDataIO.loadResults();
		Set<Band> winners = getWinners(results);
		req.setAttribute("voteresults", results);
		req.setAttribute("winners", winners);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Gets the set of poll winners from the specified poll results. This method
	 * expects that the specified results have already been appropriately
	 * sorted.
	 *
	 * @param results
	 *            poll results
	 * @return set of poll winners
	 */
	private Set<Band> getWinners(Set<PollResult> results) {
		Set<Band> winners = new LinkedHashSet<>();
		if (results.size() == 0) {
			return winners;
		}

		int maxVotes = results.iterator().next().getVotes();

		for (PollResult res : results) {
			if (res.getVotes() < maxVotes) {
				break;
			}
			winners.add(res.getBand());
		}
		return winners;
	}
}
