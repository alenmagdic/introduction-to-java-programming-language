package hr.fer.zemris.java.custom.scripting.demo;

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
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program koji prima putanju do datoteke kao argument, parsira skriptu zapisanu
 * u toj datoteci te iz dobivenog stabla ponovno rekreira originalni zapis
 * skripte.
 *
 * @author Alen Magdić
 *
 */
public class TreeWriter {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Unexpected number of arguments. Expected only one argument. Number of arguments given: "
					+ args.length);
			return;
		}

		String docBody;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.out.println("There has been a problem reading the specified document.");
			return;
		}

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		}

		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}

	/**
	 * Implementacija {@link INodeVisitor} sučelja koja ispisuje rekreiranu
	 * verziju parsirane skripte.
	 *
	 * @author Alen Magdić
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			StringBuilder sb = new StringBuilder();
			char[] data = node.getText().toCharArray();
			for (char c : data) {
				if (c == '{') {
					sb.append("\\{");
				} else if (c == '\\') {
					sb.append("\\\\");
				} else {
					sb.append(c);
				}
			}

			System.out.print(sb.toString());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			StringBuilder sb = new StringBuilder();
			sb.append("{$ FOR ");
			sb.append(getElementSourceCode(node.getVariable()));
			sb.append(getElementSourceCode(node.getStartExpression()));
			sb.append(getElementSourceCode(node.getEndExpression()));
			sb.append(getElementSourceCode(node.getStepExpression()));
			sb.append("$}");

			System.out.print(sb.toString());

			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}

			System.out.print("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			StringBuilder sb = new StringBuilder();
			sb.append("{$");
			Element[] elements = node.getElements();
			for (Element el : elements) {
				sb.append(getElementSourceCode(el));
			}
			sb.append("$}");

			System.out.print(sb.toString());

			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

		/**
		 * Rekreira zapis zadanog elementa. Konkretno, metoda je namjenjena za
		 * {@link ElementString} i za {@link ElementFunction}. Za
		 * {@link ElementString} će dodati navodnike i rekreirati korištenje
		 * escape karaktera, a za {@link ElementFunction} će samo dodati znak
		 * '@' ispred imena funkcije.
		 *
		 * @param el
		 *            element čiji zapis treba rekreirati; {@link ElementString}
		 *            ili {@link ElementFunction}
		 * @return rekreirani zapis zadanog elementa
		 */
		private Object getElementSourceCode(Element el) {
			if (el == null) {
				return "";
			}
			if (el instanceof ElementString) {
				// add escape characters where needed
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
}
