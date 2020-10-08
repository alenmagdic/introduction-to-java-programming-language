package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A web servlet that creates a html page containing a table of numbers
 * (representing angles in degrees) and their sinus and cosinus values. The
 * servlet expects two url arguments a and b, representing the range of integer
 * numbers that are to be included in the table. The numbers represent angles in
 * degrees. The maximum number of numbers that can be included in the table is
 * 720.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "trigonometricServlet", urlPatterns = { "/trigonometric" })
public class TrigonometricServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * The maximum number of numbers that can be included in the table.
	 **/
	private static final int MAX_NUM_OF_NUMS = 720;

	/**
	 * Class that represents a number (representing angle in degrees) and his
	 * sinus and cosinus value.
	 *
	 * @author Alen Magdić
	 *
	 */
	public static class NumberAndHisSinCosValue {
		/**
		 * A number, i.e. an angle in degrees.
		 */
		private int number;
		/**
		 * Sinus value of the number.
		 */
		private double sinValue;
		/**
		 * Cosinus value of the number.
		 */
		private double cosValue;

		/**
		 * Constructor.
		 *
		 * @param number
		 *            a number, i.e. an angle in degrees
		 * @param sinValue
		 *            sinus value of the specified number
		 * @param cosValue
		 *            cosinus value of the specified number
		 */
		public NumberAndHisSinCosValue(int number, double sinValue, double cosValue) {
			this.number = number;
			this.sinValue = sinValue;
			this.cosValue = cosValue;
		}

		/**
		 * Gets the number represented by this object.
		 *
		 * @return the number represented by this object
		 */
		public int getNumber() {
			return number;
		}

		/**
		 * Gets the sinus value of the number represented by this object.
		 *
		 * @return the sinus value of the number represented by this object
		 */
		public double getSinValue() {
			return sinValue;
		}

		/**
		 * Gets the cosinus value of the number represented by this object.
		 *
		 * @return the cosinus value of the number represented by this object
		 */
		public double getCosValue() {
			return cosValue;
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = 0, b = 360;

		try {
			a = Integer.valueOf(req.getParameter("a"));
		} catch (Exception ignorable) {
		}

		try {
			b = Integer.valueOf(req.getParameter("b"));
		} catch (Exception ignorable) {
		}

		if (a > b) {
			Integer tmp = a;
			a = b;
			b = tmp;
		}

		if (b > a + MAX_NUM_OF_NUMS) {
			b = a + MAX_NUM_OF_NUMS;
		}

		List<NumberAndHisSinCosValue> results = new ArrayList<>();
		for (int i = a; i <= b; i++) {
			results.add(new NumberAndHisSinCosValue(i, Math.sin(Math.toRadians(i)), Math.cos(Math.toRadians(i))));
		}

		req.getSession().setAttribute("listOfResults", results);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}

}
