package hr.fer.zemris.java.hw18.galerija.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Web servlet koji dohvaća popis thumbnailova slika sa zadanim tagom. Tag se
 * zadaje preko parametra "tag". Kao odgovor šalje listu adresa thumbnailova u
 * json formatu.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "thumbnailsLoadingServlet", urlPatterns = { "/servlets/thumbnails" })
public class ThumbnailsLoadingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> thumbnails = getThumbnails(req.getParameter("tag"), req.getServletContext());

		resp.setContentType("application/json;charset=UTF-8");

		Gson gson = new Gson();
		String jsonText = gson.toJson(thumbnails);

		resp.getWriter().write(jsonText);

		resp.getWriter().flush();
	}

	/**
	 * Pretražuje opisnik i dohvaća listu thumbnailova koji sadrže zadani tag.
	 * Lista sadrži adrese thumbnailova.
	 *
	 * @param tag
	 *            tag za koji se traže thumbnailovi
	 * @param sc
	 *            kontekst servleta
	 * @return lista adresa odgovarajućih thumbnailova
	 * @throws IOException
	 *             ako dođe do problema sa čitanjem opisnika
	 */
	private List<String> getThumbnails(String tag, ServletContext sc) throws IOException {
		List<String> thumbs = new ArrayList<>();

		Path path = Paths.get(sc.getRealPath("/WEB-INF/opisnik.txt"));
		List<String> descriptions = Files.readAllLines(path);

		for (int i = 0, n = descriptions.size(); i < n; i++) {
			if (i % 3 != 0) {
				continue;
			}

			String[] tags = descriptions.get(i + 2).split(",");
			List<String> tagList = new ArrayList<>();
			for (String tg : tags) {
				tagList.add(tg.trim());
			}

			if (tagList.contains(tag)) {
				thumbs.add("/galerija/servlets/thmb?name=" + descriptions.get(i));
			}
		}
		return thumbs;
	}

}
