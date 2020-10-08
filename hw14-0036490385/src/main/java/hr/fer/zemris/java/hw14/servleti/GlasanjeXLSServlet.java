package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.hw14.polls.PollOption;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * A web servlet that creates a representation of poll results for the specified
 * poll, in a form of xls document. The created document contains option info
 * and the number of votes for every option.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeXLSServlet", urlPatterns = { "/servleti/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {
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

		resp.setHeader("Content-Disposition", "inline; filename=\"pollResults.xls\"");
		resp.setContentType("application/excel; name=\"pollResults.xls\"");

		Set<PollOption> results = DAOProvider.getDao().getPollResults(pollID);

		HSSFWorkbook workbook = createExcelDocument(results);
		workbook.write(resp.getOutputStream());

		resp.getOutputStream().flush();
	}

	/**
	 * Creates a {@link HSSFWorkbook} object representing the specified poll
	 * results.
	 *
	 * @param results
	 *            poll results
	 * @return a {@link HSSFWorkbook} object representing the specified poll
	 *         results
	 */
	private HSSFWorkbook createExcelDocument(Set<PollOption> results) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("Poll results");

		HSSFRow rowhead = sheet.createRow(0);
		rowhead.createCell(0).setCellValue("#");
		rowhead.createCell(1).setCellValue("Option");
		rowhead.createCell(2).setCellValue("Option link");
		rowhead.createCell(3).setCellValue("Votes");

		int index = 1;
		for (PollOption opt : results) {
			HSSFRow row = sheet.createRow(index);
			row.createCell(0).setCellValue(index);
			row.createCell(1).setCellValue(opt.getTitle());
			row.createCell(2).setCellValue(opt.getLink());
			row.createCell(3).setCellValue(opt.getVotesCount());

			index++;
		}

		return hwb;
	}
}
