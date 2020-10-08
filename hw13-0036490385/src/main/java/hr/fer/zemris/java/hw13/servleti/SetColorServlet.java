package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A web servlet that sets the color of every page in this web application to
 * the color specified through url argument 'color'.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "setColorServlet", urlPatterns = { "/setColor" })
public class SetColorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = req.getParameter("color");
		req.getSession().setAttribute("pickedBgCol", color);
		// req.getRequestDispatcher("index.jsp").forward(req, resp);
		resp.sendRedirect(req.getContextPath() + "/index.jsp");
	}
}
