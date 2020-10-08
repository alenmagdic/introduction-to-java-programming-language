package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Demonstracija rada klase {@link RequestContext}. Program generira tri
 * tekstualne datoteke čiji je sadržaj nastao ispisivanjem pomoću metode write
 * klase {@link RequestContext}.
 *
 * @author Alen Magdić
 *
 */
public class DemoRequestContext {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 * @throws IOException
	 *             ukoliko dođe do problema sa zapisivanjem datoteka na disk
	 */
	public static void main(String[] args) throws IOException {
		demo1("primjer1.txt", "ISO-8859-2");
		demo1("primjer2.txt", "UTF-8");
		demo2("primjer3.txt", "UTF-8");
	}

	/**
	 * Osnovna demonstracija sa statusnim tekstom i statusnim kodom te porukom
	 * sa dijakritičkim znakovima.
	 *
	 * @param filePath
	 *            putanja koja predstavlja datoteku koja će biti kreirana
	 * @param encoding
	 *            encoding koji će biti korišten za kreiranje zadane datoteke
	 * @throws IOException
	 *             ukoliko dođe do problema sa zapisom datoteke na disk
	 */
	private static void demo1(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");

		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
	}

	/**
	 * Demonstracija sa korištenjem kolačića.
	 *
	 * @param filePath
	 *            putanja koja predstavlja datoteku koja će biti kreirana
	 * @param encoding
	 *            encoding koji će biti korišten za kreiranje zadane datoteke
	 * @throws IOException
	 *             ukoliko dođe do problema sa zapisom datoteke na disk
	 */
	private static void demo2(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1", "/"));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/"));

		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
	}
}
