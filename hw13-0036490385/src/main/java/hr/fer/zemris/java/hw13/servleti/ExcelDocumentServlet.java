package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * A web servlet that generates an xls document and sends it to client. In order
 * to work, this servlet expects three url arguments: a,b,n. It creates an xls
 * document containing pages, each page contains a table with two columns: the
 * first column contains numbers in range [a,b] and the second column contains
 * x^i value where x is a number from the column, and i is page number. Number
 * of pages can be in range [1,5] while arguments a and b can be in range
 * [-100,100].
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "excelDocumentServlet", urlPatterns = { "/powers" })
public class ExcelDocumentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a, b, n;
		resp.setHeader("Content-Disposition", "inline; filename=\"powers.xls\"");
		resp.setContentType("application/excel; name=\"powers.xls\"");

		try {
			a = Integer.valueOf(req.getParameter("a"));
		} catch (Exception ignorable) {
			forwardToInvalidParametersPage(req, resp);
			return;
		}

		try {
			b = Integer.valueOf(req.getParameter("b"));
		} catch (Exception ignorable) {
			forwardToInvalidParametersPage(req, resp);
			return;
		}

		try {
			n = Integer.valueOf(req.getParameter("n"));
		} catch (Exception ignorable) {
			forwardToInvalidParametersPage(req, resp);
			return;
		}

		if (n < 1 || n > 5 || a < -100 || a > 100 || b < -100 || b > 100) {
			forwardToInvalidParametersPage(req, resp);
			return;
		}

		HSSFWorkbook workbook = createExcelDocument(a, b, n);
		workbook.write(resp.getOutputStream());

		resp.getOutputStream().flush();
	}

	/**
	 * Creates a model of xls document (as {@link HSSFWorkbook} object) using
	 * url arguments a,b and n. It creates an xls document containing pages,
	 * each page contains a table with two columns: the first column contains
	 * numbers in range [a,b] and the second column contains x^i value where x
	 * is a number from the column, and i is page number. Number of pages can be
	 * in range [1,5] while arguments a and b can be in range [-100,100].
	 *
	 * @param a
	 *            url argument representing the range start
	 * @param b
	 *            url argument representing the range end
	 * @param n
	 *            url argument representing number of pages to be created
	 * @return
	 */
	private HSSFWorkbook createExcelDocument(Integer a, Integer b, Integer n) {
		HSSFWorkbook hwb = new HSSFWorkbook();

		for (int i = 1; i <= n; i++) {
			HSSFSheet sheet = hwb.createSheet("page " + i);

			HSSFRow rowhead = sheet.createRow(0);
			rowhead.createCell(0).setCellValue("x");
			rowhead.createCell(1).setCellValue("x^" + i);

			for (int j = a; j <= b; j++) {
				HSSFRow row = sheet.createRow(j - a + 1);
				row.createCell(0).setCellValue(j);
				row.createCell(1).setCellValue(Math.pow(j, i));
			}
		}
		return hwb;
	}

	/**
	 * Forwards the request to a jsp page that sends the client a message that
	 * the invalid parameters were given.
	 *
	 * @param req
	 *            http servlet request
	 * @param resp
	 *            http servlet response
	 * @throws ServletException
	 *             if the target resource throws this exception *
	 * @throws IOException
	 *             if the target resource throws this exception
	 *
	 */
	private void forwardToInvalidParametersPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/excelDocInvParams.jsp").forward(req, resp);
	}
}
