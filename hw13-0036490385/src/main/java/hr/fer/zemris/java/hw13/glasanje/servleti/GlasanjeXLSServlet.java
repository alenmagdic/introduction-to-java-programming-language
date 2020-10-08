package hr.fer.zemris.java.hw13.glasanje.servleti;

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

import hr.fer.zemris.java.hw13.glasanje.Band;
import hr.fer.zemris.java.hw13.glasanje.PollResult;
import hr.fer.zemris.java.hw13.util.PollDataIO;

/**
 * A web servlet that creates a representation of favourite band poll results in
 * a form of xls document. The created document contains band info and number of
 * votes for every band.
 *
 * @author Alen Magdić
 *
 */

@WebServlet(name = "glasanjeXLSServlet", urlPatterns = { "/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Content-Disposition", "inline; filename=\"voteResults.xls\"");
		resp.setContentType("application/excel; name=\"voteResults.xls\"");

		Set<PollResult> results = PollDataIO.loadResults();

		HSSFWorkbook workbook = createExcelDocument(results);
		workbook.write(resp.getOutputStream());

		resp.getOutputStream().flush();
	}

	/**
	 * Creates a {@link HSSFWorkbook} object representing the specified poll
	 * results.
	 *
	 * @param results
	 *            favourite band poll results
	 * @return a {@link HSSFWorkbook} object representing the specified poll
	 *         results
	 */
	private HSSFWorkbook createExcelDocument(Set<PollResult> results) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("Favourite band - vote results");

		HSSFRow rowhead = sheet.createRow(0);
		rowhead.createCell(0).setCellValue("#");
		rowhead.createCell(1).setCellValue("Band name");
		rowhead.createCell(2).setCellValue("Band song example");
		rowhead.createCell(3).setCellValue("Band id");
		rowhead.createCell(4).setCellValue("Votes");

		int index = 1;
		for (PollResult res : results) {
			Band band = res.getBand();

			HSSFRow row = sheet.createRow(index);
			row.createCell(0).setCellValue(index);
			row.createCell(1).setCellValue(band.getName());
			row.createCell(2).setCellValue(band.getSongLink());
			row.createCell(3).setCellValue(band.getId());
			row.createCell(4).setCellValue(res.getVotes());

			index++;
		}

		return hwb;
	}
}
