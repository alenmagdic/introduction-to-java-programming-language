package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * A web servlet that executes the action of voting in the specified poll for
 * specified options. Both poll id and option id are specified through url
 * parameters (parameters pollID and optionID). If the client gives invalid poll
 * id or option id (something that is not a number), or does not give poll id or
 * option id, the servlet will send an error with code 400.
 *
 * The servlet updates and saves the poll results and redirects client to poll
 * results page.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeGlasajServlet", urlPatterns = { "/servleti/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollID, optionID;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
			optionID = Long.parseLong(req.getParameter("optionID"));
		} catch (NumberFormatException ex) {
			resp.sendError(400, "Invalid parameters given");
			return;
		}

		try {
			DAOProvider.getDao().updateVotes(pollID, optionID);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollID);
	}
}
