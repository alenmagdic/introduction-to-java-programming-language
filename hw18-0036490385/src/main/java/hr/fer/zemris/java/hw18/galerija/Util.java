package hr.fer.zemris.java.hw18.galerija;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;

/**
 * Klasa koja sadrži pomoćne metode za galeriju slika. Konkretno, sadrži samo
 * jednu metodu koja čita opisnik i vraća popis linija iz opisnika.
 *
 * @author Alen Magdić
 *
 */
public class Util {

	/**
	 * Čita opisnik s diska i dohvaća listu pročitanih linija.
	 *
	 * @param sc
	 *            servlet context
	 * @return lista pročitanih linija opisnika
	 * @throws IOException
	 *             ako dođe do problema u čitanju opisnika s diska
	 */
	public static List<String> getDescriptionerLines(ServletContext sc) throws IOException {
		Path path = Paths.get(sc.getRealPath("/WEB-INF/opisnik.txt"));
		return Files.readAllLines(path);
	}
}
