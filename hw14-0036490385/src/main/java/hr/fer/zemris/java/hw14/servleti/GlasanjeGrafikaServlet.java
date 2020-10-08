package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;

import hr.fer.zemris.java.hw14.polls.PollOption;
import hr.fer.zemris.java.hw14.util.PieChartGenerator;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * A web servlet that generates a graphical representation of vote results for
 * the specified poll. The poll is specified through url parameter pollID. If
 * the client gives invalid poll id (something that is not a number), or does
 * not give poll id, the servlet will send an error with code 400.
 *
 * Results are represented by a pie chart.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeGrafikaServlet", urlPatterns = { "/servleti/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {
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
		if (DAOProvider.getDao().getPoll(pollID) == null) {
			resp.sendError(400, "Invalid parameters given");
			return;
		}

		resp.setContentType("image/png");

		Set<PollOption> results = DAOProvider.getDao().getPollResults(pollID);

		Map<String, Integer> resultsMap = new HashedMap<>();
		for (PollOption opt : results) {
			resultsMap.put(opt.getTitle(), opt.getVotesCount());
		}

		String title = DAOProvider.getDao().getPoll(pollID).getTitle();
		PieChartGenerator.generateChart(resultsMap, title, resp.getOutputStream());
	}
}
