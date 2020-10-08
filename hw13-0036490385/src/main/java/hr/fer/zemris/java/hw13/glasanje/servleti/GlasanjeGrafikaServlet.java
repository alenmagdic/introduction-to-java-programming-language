package hr.fer.zemris.java.hw13.glasanje.servleti;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;

import hr.fer.zemris.java.hw13.glasanje.PollResult;
import hr.fer.zemris.java.hw13.util.PieChartGenerator;
import hr.fer.zemris.java.hw13.util.PollDataIO;

/**
 * A web servlet that generates a graphical representation of vote results.
 * Results are represented by a pie chart.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeGrafikaServlet", urlPatterns = { "/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");

		Set<PollResult> results = PollDataIO.loadResults();
		Map<String, Integer> resultsMap = new HashedMap<>();
		for (PollResult res : results) {
			resultsMap.put(res.getBand().getName(), res.getVotes());
		}

		PieChartGenerator.generateChart(resultsMap, "Favourite band", resp.getOutputStream());
	}
}
