package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Program koji demonstrira rad klase {@link SmartScriptEngine} i
 * {@link RequestContext}. Ispisuje izlaze četiri primjera skripti zajedno sa
 * odgovarajućim zaglavljima HTTP odgovora.
 *
 * @author Alen Magdić
 *
 */
public class ExecutionDemo {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumetni
	 * @throws IOException
	 *             ukoliko dođe do problema sa čitanjem skripti s diska
	 */
	public static void main(String[] args) throws IOException {
		demo1();
		demo2();
		demo3();
		demo4();
	}

	/**
	 * Demonstracija sa jednostavnom for petljom koja ispisuje poruku te sa
	 * računanjem sinusa zadanih vrijednosti.
	 *
	 * @throws IOException
	 *             ukoliko dođe do problema sa čitanjem skripte
	 */
	private static void demo1() throws IOException {
		String documentBody = readFromDisk("webroot/scripts/osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
	}

	/**
	 * Demonstracija sa jednostavnom zbrajanjem dvaju brojeva zadanih preko
	 * parametara.
	 *
	 * @throws IOException
	 *             ukoliko dođe do problema sa čitanjem skripte
	 */
	private static void demo2() throws IOException {
		String documentBody = readFromDisk("webroot/scripts/zbrajanje.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

		System.out.println("\n\n\n");
	}

	/**
	 * Demonstracija sa korištenjem trajnih parametara za ispis broja poziva
	 * skripte.
	 *
	 * @throws IOException
	 *             ukoliko dođe do problema sa čitanjem skripte
	 */
	private static void demo3() throws IOException {
		String documentBody = readFromDisk("webroot/scripts/brojPoziva.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		System.out.println("\nVrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));

		System.out.println("\n\n\n");
	}

	/**
	 * Demonstracija koja ispisuje prvih 10 Fibonnacijevih brojeva.
	 *
	 * @throws IOException
	 *             ukoliko dođe do problema sa čitanjem skripte
	 */
	private static void demo4() throws IOException {
		String documentBody = readFromDisk("webroot/scripts/fibonacci.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

	}

	/**
	 * Čita zadanu datoteku s diska.
	 *
	 * @param sourceFilePath
	 *            putanja do datoteke koju treba pročitati
	 * @return sadržaj zadane datoteke
	 * @throws IOException
	 *             ukoliko dođe do problema sa čitanjem zadane datoteke
	 */
	private static String readFromDisk(String sourceFilePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(sourceFilePath)), StandardCharsets.UTF_8);
	}
}
