package hr.fer.zemris.java.hw18.galerija.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw18.galerija.models.ImageData;

/**
 * Web servlet koji dohvaća podatke o zadanoj slici i podatke šalje kao odgovor
 * u json formatu. Slika se zadaje preko parametra thmbsrc, u kojem se nalazi
 * adresa thumbnaila,a servlet sam na temelju tog podatka određuje pripadnu
 * sliku za koju treba dohvatiti podatke.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "imageDataServlet", urlPatterns = { "/servlets/image" })
public class ImageDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ImageData imgData = getImageData(req);

		resp.setContentType("application/json;charset=UTF-8");

		Gson gson = new Gson();
		String jsonText = gson.toJson(imgData);

		resp.getWriter().write(jsonText);

		resp.getWriter().flush();
	}

	/**
	 * Dohvaća podatke o zadanoj slici. Iz danog zahtjeva čita parametar thmbsrc
	 * te iz njega određuje o kojoj je slici riječ te generira i vraća
	 * {@link ImageData} koji sadrži podatke o slici.
	 *
	 * @param req
	 *            http zahtjev
	 * @return objekt koji sadrži podatke o slici
	 * @throws IOException
	 *             ako dođe do problema sa čitanjem s diska
	 */
	private ImageData getImageData(HttpServletRequest req) throws IOException {
		String thmbSrc = req.getParameter("thmbsrc");
		String picName = thmbSrc.substring(thmbSrc.lastIndexOf("?name=") + 6);

		Path path = Paths.get(req.getServletContext().getRealPath("/WEB-INF/opisnik.txt"));
		List<String> descriptions = Files.readAllLines(path);

		ImageData imgData = new ImageData();
		for (int i = 0, n = descriptions.size(); i < n; i++) {
			if (!descriptions.get(i).equals(picName)) {
				continue;
			}

			imgData.setSrc("/galerija/servlets/imgfile?name=" + descriptions.get(i).trim());
			imgData.setName(descriptions.get(i + 1).trim());
			imgData.setTags(descriptions.get(i + 2).trim());
			break;
		}
		return imgData;
	}
}
