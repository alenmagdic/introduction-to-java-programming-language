package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.hw03.prob1.LexerException;

/**
 * Parsira dani source kod. U slučaju nailaska na bilo kakvu pogrešku u kodu,
 * baca iznimku SmartScriptParserException. Klasa pri parsiranju konstruira
 * dokumentno stablo koje je moguće dohvatiti javnim getterom. Stablo je
 * modelirano izvedenicama razreda Node.
 *
 * @author Alen Magdić
 *
 */
public class SmartScriptParser {
	/** Lexer koji se koristi pri parsiranju **/
	private Lexer lexer;
	/** Dokumentno stablo koje se konstruira parsiranjem koda **/
	private DocumentNode docNode;
	/**
	 * Stog na kojeg se pohranjuje svaki primjerak Node koji ima djecu. Time
	 * omogućuje izgradnju dokumentnog stabla.
	 **/
	private ObjectStack stack;

	/**
	 * Konstruktor. Prima referencu na sadržaj koda kojeg je potrebno parsirati.
	 * Konstruktor ujedno i pokreće parsiranje dokumenta.
	 *
	 * @param document
	 *            referenca na sadržaj koda kojeg je potrebno parsirati
	 */
	public SmartScriptParser(String document) {
		if (document == null) {
			throw new IllegalArgumentException("Parser ne može biti konstruiran sa argumentom null");
		}

		stack = new ObjectStack();
		lexer = new Lexer(document);
		parse();
	}

	/**
	 * Metoda koja izvodi parsiranje.
	 */
	private void parse() {
		docNode = new DocumentNode();
		stack.push(docNode);

		while (true) {
			Token token = nextToken();

			if (token.getType() == TokenType.TEXT) {
				((Node) stack.peek()).addChildNode(new TextNode((String) token.getValue()));
			} else if (token.getType() == TokenType.OPEN_TAG) {
				token = nextToken();

				if (token.getType() == TokenType.NAME && ((String) token.getValue()).toLowerCase().equals("for")) {
					createForNode();
				} else if (token.getType() == TokenType.NAME
						&& ((String) token.getValue()).toLowerCase().equals("end")) {
					parseEndTag();
				} else {
					createEchoNode();
				}
			} else if (token.getType() == TokenType.EOF) {
				break;
			}
		}
	}

	/**
	 * Pomoćna metoda koja se poziva pri nailasku na END tag. Metoda provjerava
	 * je li tag ispravno zatvoren te skida jedan node sa stoga. Metoda će
	 * potrošiti točno jedan token.
	 *
	 */
	private void parseEndTag() {
		Token token = nextToken();

		if (token.getType() != TokenType.CLOSE_TAG) {
			throw new SmartScriptParserException("Neispravno korištenje END taga. END tag ne prima nikakve argumente.");
		}

		try {
			stack.pop();
		} catch (EmptyStackException ex) {
			throw new SmartScriptParserException("Kod sadrži višak END tagova.");
		}
	}

	/**
	 * Metoda stvara ForNode i dodaje ga na odgovarajuće mjesto u stablo te ga
	 * stavlja na stog. Metoda će istrošiti tokene sve do nailaska na zatvoreni
	 * tag.
	 */
	private void createForNode() {
		ElementVariable var = null;
		Element startExpr = null;
		Element endExpr = null;
		Element stepExpr = null;

		for (int i = 1; i <= 5; i++) {
			Token token = nextToken();

			if (token.getType() == TokenType.CLOSE_TAG) {
				if (i >= 4) {
					break;
				} else {
					throw new SmartScriptParserException(
							"Nakon naredbe FOR moraju slijediti najmanje tri argumenta, a ne samo " + (i - 1));
				}
			} else if (token.getType() == TokenType.EOF) {
				throw new SmartScriptParserException("Dokument je nepotpun. Naredba FOR nije do kraja napisana.");
			} else if (token.getType() == TokenType.OPEN_TAG) {
				throw new SmartScriptParserException("Unutar taga se ne smije nalaziti drugi tag.");
			}

			if (i == 1) {
				if (token.getType() != TokenType.NAME) {
					throw new SmartScriptParserException(
							"Nakon naredbe FOR mora slijediti ime varijable po kojoj se iterira, a ne '"
									+ token.getValue() + "'");
				}
				var = new ElementVariable((String) token.getValue());
			} else {

				if (i == 5) {
					throw new SmartScriptParserException(
							"Nakon naredbe FOR moraju slijediti najviše četiri argumenta.");
				}

				if (token.getType() != TokenType.NAME && token.getType() != TokenType.STRING
						&& token.getType() != TokenType.INT_CONST && token.getType() != TokenType.DOUBLE_CONST) {
					throw new SmartScriptParserException(
							"Neispravan argument za naredbu FOR. Argument: " + token.getValue());
				}

				Element el = elementFromToken(token);
				if (i == 2) {
					startExpr = el;
				} else if (i == 3) {
					endExpr = el;
				} else {
					stepExpr = el;
				}

			}
		}

		ForLoopNode node = new ForLoopNode(var, startExpr, endExpr, stepExpr);
		((Node) stack.peek()).addChildNode(node);

		stack.push(node);
	}

	/**
	 * Pomoćna metoda koja prima token u kojem se mora nalaziti neki od
	 * podržanih tipova argumenata naredbe FOR te vraća generirani odgovarajući
	 * element iz danog tokena.
	 *
	 * @param token
	 *            token iz kojeg se generira element
	 * @return element generiran na temelju danog tokena
	 */
	private Element elementFromToken(Token token) {
		if (token.getType() == TokenType.NAME) {
			return new ElementVariable((String) token.getValue());
		}
		if (token.getType() == TokenType.STRING) {
			return elementStringFromToken(token);
		}
		if (token.getType() == TokenType.INT_CONST) {
			return new ElementConstantInteger((Integer) token.getValue());
		}
		if (token.getType() == TokenType.DOUBLE_CONST) {
			return new ElementConstantDouble((Double) token.getValue());
		}

		return null;
	}

	/**
	 * Pomoćna metoda koja prima token tipa STRING te na temelju njega generira
	 * i vraća ElementString.
	 *
	 * @param token
	 *            token iz kojeg se generira element
	 * @return element generiran na temelju danog tokena
	 */
	private ElementString elementStringFromToken(Token token) {
		// lexer će string vratiti zajedno sa navodnicima pa ih treba
		// odrezati
		String string = ((String) token.getValue()).substring(1);
		string = string.substring(0, string.length() - 1);
		return new ElementString(string);
	}

	/**
	 * Metoda stvara EchoNode i dodaje ga na odgovarajuće mjesto u stablo
	 * nodova. Metoda će istrošiti tokene sve do nailaska na zatvoreni tag te će
	 * on biti zadnji potrošeni tag.
	 *
	 */
	private void createEchoNode() {
		ArrayIndexedCollection elements = new ArrayIndexedCollection();

		while (true) {
			Token token;

			if (elements.size() == 0) {
				// provjera imena taga - ime je već spremljeno u zadnjem
				// generiranom tokenu
				token = lexer.getToken();
				if (token.getType() == TokenType.SYMBOL) {

					// radi jednostavnosti parsiranja, tag nazvan simbolom '='
					// se prevodi u novi token tipa NAME
					if ((Character) token.getValue() == '=') {
						token = new Token(TokenType.NAME, String.valueOf('='));
					} else {
						throw new SmartScriptParserException(
								"Jedini simbol koji može predstavljati ime taga jest '=', a ne " + token.getValue());
					}
				}
			} else {
				token = nextToken();
			}

			Element el;
			if (token.getType() == TokenType.NAME) {
				el = new ElementVariable((String) token.getValue());
			} else if (token.getType() == TokenType.SYMBOL) {
				Character symbol = (Character) token.getValue();

				if (symbol == '_') {
					throw new SmartScriptParserException(
							"Simbol '_' je podržan jedino kao dio imena varijable, ali ne kao početni znak u imenu varijable.");
				} else if (symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/' || symbol == '^') {
					el = new ElementOperator(String.valueOf(symbol));
				} else {
					throw new SmartScriptParserException("Simbol '" + symbol + "' nije podržan.");
				}
			} else if (token.getType() == TokenType.FUNCTION) {

				// na početku tokena funkcije se uvijek nalazi @ pa njega treba
				// ukloniti kako bi se dodalo čisto ime u ElementFunction
				String functionName = ((String) token.getValue()).substring(1);
				el = new ElementFunction(functionName);

			} else if (token.getType() == TokenType.STRING) {
				el = elementStringFromToken(token);
			} else if (token.getType() == TokenType.INT_CONST) {
				el = new ElementConstantInteger((Integer) token.getValue());
			} else if (token.getType() == TokenType.DOUBLE_CONST) {
				el = new ElementConstantDouble((Double) token.getValue());
			} else if (token.getType() == TokenType.EOF) {
				throw new SmartScriptParserException("Neki od tagova nisu ispravno zatvoreni.");
			} else if (token.getType() == TokenType.OPEN_TAG) {
				throw new SmartScriptParserException("Tag unutar taga nije dozvoljen.");
			} else {
				// ovdje dolazi samo kad je token CLOSE_TAG
				break;
			}

			elements.add(el);
		}

		Element[] elementsArray = new Element[elements.size()];
		for (int i = 0, size = elements.size(); i < size; i++) {
			elementsArray[i] = (Element) elements.get(i);
		}

		((Node) stack.peek()).addChildNode(new EchoNode(elementsArray));
	}

	/**
	 * Vraća konstruirano dokumentno stablo.
	 *
	 * @return dokumentno stablo
	 */
	public DocumentNode getDocumentNode() {
		return docNode;
	}

	/**
	 * Vraća sljedeći token. U slučaju bilo kakve pogreške u sadržaju dokumenta
	 * koji se parsira, baca SmartScriptParserException.
	 *
	 * @return sljedeći token
	 */
	private Token nextToken() {
		try {
			return lexer.nextToken();
		} catch (LexerException ex) {
			throw new SmartScriptParserException(ex);
		}
	}

}
