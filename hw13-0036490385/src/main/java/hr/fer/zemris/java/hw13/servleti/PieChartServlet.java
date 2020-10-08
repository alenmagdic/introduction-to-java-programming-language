package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw13.util.PieChartGenerator;

/**
 * A web servlet that creates a simple pie chart and sends it to the client. The
 * pie chart represents usage of operating systems.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "pieChartServlet", urlPatterns = { "/reportimage" })
public class PieChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		PieChartGenerator.generateChart(null, "OS usage", resp.getOutputStream());
	}

}
