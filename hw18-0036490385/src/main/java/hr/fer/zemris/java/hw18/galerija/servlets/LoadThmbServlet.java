package hr.fer.zemris.java.hw18.galerija.servlets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web servlet koji učitava thumbnail za sliku sa zadnim imenom i šalje ga kao
 * odgovor. Ime slike se zadaje preko parametra "name". Ako thumbnail ne postoji
 * u memoriji, automatski se kreira te zatim šalje.
 *
 * @author Alen Magdić
 *
 */
@WebServlet(name = "loadThmbServlet", urlPatterns = { "/servlets/thmb" })
public class LoadThmbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String picName = req.getParameter("name");
		Path thumb = Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails/" + picName));

		BufferedImage img;
		if (!Files.exists(thumb)) {
			img = createThumbnail(picName, req.getServletContext());
		} else {
			img = ImageIO.read(thumb.toFile());
		}
		resp.setContentType("image/jpeg");

		ImageIO.write(img, "jpg", resp.getOutputStream());
		resp.getOutputStream().flush();
	}

	/**
	 * Stvara thumbnail slike sa zadanim imenom te ga sprema na disk.
	 *
	 * @param picName
	 *            ime slike za koju je potrebno napraviti thumbnail
	 * @param sc
	 *            kontekst servleta
	 * @return {@link BufferedImage} objekt koji predstavlja kreiranu sliku
	 * @throws IOException
	 *             ako dođe do problema pri zapisivanju ili učitavanju slika
	 */
	private BufferedImage createThumbnail(String picName, ServletContext sc) throws IOException {
		Path path = Paths.get(sc.getRealPath("/WEB-INF/slike/" + picName));
		BufferedImage img = ImageIO.read(path.toFile());

		if (!Files.exists(Paths.get(sc.getRealPath("/WEB-INF/thumbnails")))) {
			Files.createDirectory(Paths.get(sc.getRealPath("/WEB-INF/thumbnails")));
		}
		Path thmbPath = Paths.get(sc.getRealPath("/WEB-INF/thumbnails/" + picName));

		Image imgScaled = img.getScaledInstance(150, 150, BufferedImage.SCALE_SMOOTH);

		BufferedImage thmb = new BufferedImage(150, 150, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = thmb.createGraphics();
		g2d.drawImage(imgScaled, 0, 0, null);
		g2d.dispose();

		ImageIO.write(thmb, "jpg", Files.newOutputStream(thmbPath));
		return thmb;
	}
}
