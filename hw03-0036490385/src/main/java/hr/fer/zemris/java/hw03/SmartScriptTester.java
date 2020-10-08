package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program koji testira rad SmartScriptParsera. Program prima jedan argument,
 * putanju do datoteke sa izvornim kodom kojeg je potrebno parsirati. U slučaju
 * pogreške biti će ispisana odgovarajuća poruka. Ako parsiranje bude uspješno,
 * program će ispisati rekreirani originalni kod na temelju dokumentnog stabla
 * nastalog parsiranjem.
 *
 * @author Alen Magdić
 *
 */
public class SmartScriptTester {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Neispravan broj argumenata. Očekuje se točno jedan argument. Dani broj argumenata: "
					+ args.length);
			return;
		}

		String docBody;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.out.println("Došlo je do pogreške u čitanju datoteke.");
			return;
		}

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody);

	}

	/**
	 * Metoda koja rekreira izvorni kod na temelju zadanog dokumentnog stabla.
	 *
	 * @param document
	 *            čvor na dokumentno stablo
	 * @return rekreirani izvorni kod zadanog dokumentnog stabla
	 */
	public static String createOriginalDocumentBody(Node document) {
		if (document == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (document instanceof TextNode) {
			// dodavanje escape charactera gdje je potrebno
			char[] data = ((TextNode) document).getText().toCharArray();
			for (char c : data) {
				if (c == '{') {
					sb.append("\\{");
				} else if (c == '\\') {
					sb.append("\\\\");
				} else {
					sb.append(c);
				}
			}

			return sb.toString();
		} else if (document instanceof EchoNode) {
			sb.append("{$");
			Element[] elements = ((EchoNode) document).getElements();
			for (Element el : elements) {
				sb.append(getElementSourceCode(el));
			}
			sb.append("$}");
		} else if (document instanceof ForLoopNode) {
			ForLoopNode forNode = (ForLoopNode) document;

			sb.append("{$ FOR ");
			sb.append(getElementSourceCode(forNode.getVariable()));
			sb.append(getElementSourceCode(forNode.getStartExpression()));
			sb.append(getElementSourceCode(forNode.getEndExpression()));
			sb.append(getElementSourceCode(forNode.getStepExpression()));
			sb.append("$}");
		}

		for (int i = 0, n = document.numberOfChildren(); i < n; i++) {
			sb.append(createOriginalDocumentBody(document.getChild(i)));
		}

		if (document instanceof ForLoopNode) {
			sb.append("{$END$}");
		}

		return sb.toString();
	}

	/**
	 * Metoda vraća string koji predstavlja zapis danog elementa u source kodu.
	 * Odnosno, za ElementString vraća njegov tekst okružen navodnicima, za
	 * ElementFunction ime funkcije s znakom '@' na početku.
	 *
	 * @param el
	 * @return
	 */
	private static String getElementSourceCode(Element el) {
		if (el == null) {
			return "";
		}
		if (el instanceof ElementString) {
			// dodaj escape karaktere di je potrebno
			StringBuilder sb = new StringBuilder();
			char[] data = el.asText().toCharArray();
			for (char c : data) {
				if (c == '\n') {
					sb.append("\\n");
				} else if (c == '\r') {
					sb.append("\\r");
				} else if (c == '\t') {
					sb.append("\\t");
				} else if (c == '\\') {
					sb.append("\\\\");
				} else if (c == '\"') {
					sb.append("\\\"");
				} else {
					sb.append(c);
				}
			}
			return "\"" + sb.toString() + "\" ";
		} else if (el instanceof ElementFunction) {
			return "@" + el.asText() + " ";
		}
		return el.asText() + " ";

	}
}
