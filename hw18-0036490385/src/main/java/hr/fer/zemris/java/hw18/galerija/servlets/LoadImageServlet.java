package hr.fer.zemris.java.hw18.galerija.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web servlet koji učitava zadanu sliku i šalje ju kao odgovor. Slika se zadaje
 * preko parametra "name" koji određuje ime slike.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "loadImageServlet", urlPatterns = { "/servlets/imgfile" })
public class LoadImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String picName = req.getParameter("name");
		Path path = Paths.get(req.getServletContext().getRealPath("/WEB-INF/slike/" + picName));
		BufferedImage img = ImageIO.read(path.toFile());

		resp.setContentType("image/jpeg");

		ImageIO.write(img, "jpg", resp.getOutputStream());
		resp.getOutputStream().flush();
	}
}
