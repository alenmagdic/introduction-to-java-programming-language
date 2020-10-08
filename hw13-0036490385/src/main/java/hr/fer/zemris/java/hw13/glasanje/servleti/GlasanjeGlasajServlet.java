package hr.fer.zemris.java.hw13.glasanje.servleti;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw13.listeneri.ResourcePathsArchiver;
import hr.fer.zemris.java.hw13.util.PollDataIO;

/**
 * A web servlet that executes the action of voting for a band defined by url
 * parameter. It updates and saves the poll results and redirects client to poll
 * results page.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeGlasajServlet", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> voteResults;
		if (!Files.exists(ResourcePathsArchiver.POLL_RESULTS_FILE_PATH)) {
			voteResults = PollDataIO.createFileWithEmptyResults();
		} else {
			voteResults = Files.readAllLines(ResourcePathsArchiver.POLL_RESULTS_FILE_PATH);
		}
		updateResults(voteResults, req.getParameter("id"));

		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	/**
	 * Updates the poll results.
	 *
	 * @param currentResults
	 *            current content of the file containing the poll results
	 * @param voteId
	 *            id of band that has just been choosen by a client (i.e. a
	 *            client whose request caused this servlet to be called)
	 * @throws IOException
	 *             if there is a problem writing the updated results to disk
	 */
	private void updateResults(List<String> currentResults, String voteId) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(ResourcePathsArchiver.POLL_RESULTS_FILE_PATH);

		try {
			for (String line : currentResults) {
				String[] data = line.split("\t");
				int id = Integer.parseInt(data[0].trim());
				int votes = Integer.parseInt(data[1].trim());
				int newVote = voteId.equals(data[0]) ? 1 : 0;

				writer.write(id + "\t" + Integer.toString(votes + newVote) + "\r\n");
			}
		} finally {
			writer.close();
		}
	}
}
