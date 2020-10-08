package hr.fer.zemris.java.hw18.galerija.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Web servlet koji učitava popis svih tagova te ih šalje kao listu u json
 * formatu.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "tagsLoadingServlet", urlPatterns = { "/servlets/tagsGetter" })
public class TagsLoadingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> tags = getTags(req.getServletContext());

		resp.setContentType("application/json;charset=UTF-8");

		Gson gson = new Gson();
		String jsonText = gson.toJson(tags);

		resp.getWriter().write(jsonText);

		resp.getWriter().flush();
	}

	/**
	 * Učitava sve tagove iz opisnika te ih vraća kao listu.
	 *
	 * @param sc
	 *            kontekst servleta
	 * @return lista svih tagova
	 * @throws IOException
	 *             ako dođe do problema sa čitanjem opisnika
	 */
	private List<String> getTags(ServletContext sc) throws IOException {
		Set<String> tags = new HashSet<>();

		Path path = Paths.get(sc.getRealPath("/WEB-INF/opisnik.txt"));
		List<String> descriptions = Files.readAllLines(path);

		for (int i = 0, n = descriptions.size(); i < n; i++) {
			if (i % 3 != 2) {
				continue;
			}

			String[] tagsArr = descriptions.get(i).split(",");
			for (String tag : tagsArr) {
				tags.add(tag.trim());
			}
		}
		return new ArrayList<>(tags);
	}
}
