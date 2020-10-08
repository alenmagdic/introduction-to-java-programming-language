package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Klasa koja predstavlja Lexer. Lexer se konstruira iz danog teksta te
 * uzastopnim pozivima metode nextToken generira i vraća tokene iz danog teksta.
 * Postoji devet tipova tokena s kojima ovaj Lexer radi, a modelirani su
 * enumeratorom TokenType. Lexer će u slučaju bilo kakve pogreške baciti iznimku
 * LexerException.
 *
 * Lexer ima dva stanja između kojih se sam automatski izmjenjuje. Početno je u
 * stanju OUT_TAG. Pri prvom pozivu nextToken će generirati TEXT token koji će
 * sadržavati tekst iz zadanog koda sve do nailaska na znak '{' koji predstavlja
 * početak taga. Ako je taj znak odmah na početku, prvi token će biti prazan
 * string tipa TEXT. Nakon svakog povratka tokena TEXT, Lexer se automatski
 * prebacuje u stanje IN_TAG jer se podrazumijeva da slijedi početak taga ili
 * kraj teksta. Unutar tagova se čitaju tokeni tipa OPEN_TAG i CLOSE_TAG koji
 * predstavljaju početak i kraj taga, tokeni tipa INT_CONST, DOUBLE_CONST koji
 * predstavljaju brojevne konstante, tokeni tipa STRING koji se prepoznaju po
 * tome što su zapisani unutar dvostrukih navodnika. Također se može iščitati
 * token tipa funkcija koji se prepoznaje po početnom simbolu '@'.
 *
 * Escape karakter '\' unutar stringova omogućuje zapis navodnika i samog znaka
 * '\', te znakova '\n','\r','\t', dok izvan tagova omogućuje zapis '{' i samog
 * znaka '\'.
 *
 * @author Alen Magdić
 *
 */
public class Lexer {
	/** Polje karaktera koje pohranjuje source kod koji se analizira **/
	private char[] data;
	/** Zadnje generirani token **/
	private Token token;
	/** Pozicija prvog neobrađenog elementa u polju karaktera data **/
	private int currentIndex;
	/** Trenutno stanje Lexera **/
	private LexerState state;

	/** Konstruktor. Prima referencu na izvorni kod kojeg treba analizirati. **/
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Lexer can not be created with null as argument.");
		}

		this.data = text.toCharArray();
		state = LexerState.OUT_TAG;
	}

	/**
	 * Generira i vraća sljedeći token. Ako se pronađe bilo kakva pogreška u
	 * kodu, baca iznimku LexerException.
	 *
	 * @return sljedeći token
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("There are no any more tokens.");
		}
		if (currentIndex == data.length) {
			return token = new Token(TokenType.EOF, null);
		}

		if (state == LexerState.OUT_TAG) {
			return token = textToken();
		}

		TokenType detectedTokenType = null;
		StringBuilder tokenSB = new StringBuilder();

		for (int i = currentIndex; i < data.length; i++, currentIndex++) {

			if (data[i] == ' ' || data[i] == '\r' || data[i] == '\n' || data[i] == '\t') {
				if (tokenSB.toString().length() == 0) {
					continue;
				}
				break;
			}

			if (detectedTokenType == null) {
				detectedTokenType = detectTokenType(i);

				if (detectedTokenType == TokenType.STRING) {
					return token = stringToken();
				}

				if (detectedTokenType == TokenType.SYMBOL) {
					currentIndex++;
					return token = new Token(TokenType.SYMBOL, data[i]);
				}
			}

			if (detectTokenType(i) == TokenType.SYMBOL && detectedTokenType != TokenType.CLOSE_TAG
					&& !(detectedTokenType == TokenType.DOUBLE_CONST && data[i] == '.')
					&& !(detectedTokenType == TokenType.NAME && data[i] == '_')) {
				break;
			}

			if (tokenSB.toString().length() >= 1) {
				checkForInvalidExpressions(detectedTokenType, data[i], tokenSB);
			}

			tokenSB.append(data[i]);

			if ((detectedTokenType == TokenType.OPEN_TAG || detectedTokenType == TokenType.CLOSE_TAG)
					&& tokenSB.toString().length() == 2) {
				currentIndex++;
				if (detectedTokenType == TokenType.CLOSE_TAG) {
					state = LexerState.OUT_TAG;
				}
				break;
			}

			if (detectedTokenType != TokenType.OPEN_TAG && detectedTokenType != TokenType.CLOSE_TAG
					&& i + 1 < data.length && data[i + 1] == '$') {
				currentIndex++;
				break;
			}
		}

		token = newTokenFromSB(tokenSB, detectedTokenType);
		return token;
	}

	/**
	 * Generira i vraća novi token na temelju podataka iz String Buildera i
	 * predloženog tipa. Jedini slučaj kada će metoda vratiti token koji nije
	 * predloženog tipa je ako je predloženi tip DOUBLE_CONST, a metoda
	 * procijeni da se podatak u String Builderu može protumačiti kao INT_CONST
	 * (odnosno ne sadrži decimalnu točku). Metoda u slučaju da je predloženi
	 * tip DOUBLE_CONST, ali se podatak u String Builderu ne može parsirati u
	 * double, baca iznimku LexerException. Metoda očekuje samo neke od
	 * sljedećih tipova: FUNCTION,NAME,DOUBLE_CONST.
	 *
	 * @param tokenSB
	 *            String Builder iz kojeg se čitaju podaci koje treba pohraniti
	 *            u token
	 * @param type
	 *            predloženi tip novog tokena
	 * @return novi token konstruiran iz danih podataka
	 */
	private Token newTokenFromSB(StringBuilder tokenSB, TokenType type) {
		String string = tokenSB.toString();
		Object value;

		if (type == TokenType.FUNCTION && string.length() == 1) {
			throw new LexerException("Function has to have its name.");
		}

		if (type == TokenType.DOUBLE_CONST) {
			try {
				value = Integer.parseInt(string);
				type = TokenType.INT_CONST;
			} catch (NumberFormatException ex) {
				try {
					value = Double.parseDouble(string);
				} catch (NumberFormatException e) {
					throw new LexerException("Expression '" + string + "' is not valid.");
				}
			}
		} else {
			value = string;
		}

		return new Token(type, value);
	}

	/**
	 * Metoda na temelju danih podataka provjerava hoće li dani znak dodavanjem
	 * u dani string builder tvoriti ispravan token tipa type. Ako se ustanovi
	 * da to nije moguće, baciti će iznimku LexerException uz odgovarajuću
	 * poruku pojašnjenja.
	 *
	 * @param type
	 *            tip tokena
	 * @param ch
	 *            znak koji se treba dodati u string builder
	 * @param sb
	 *            string builder u kojem se već nalazi barem jedan znak
	 */
	private void checkForInvalidExpressions(TokenType type, char ch, StringBuilder sb) {
		if (type == TokenType.OPEN_TAG && ch != '$') {
			throw new LexerException(
					"Invalid use of character '{'. Next to that character should be '$', and not " + ch);
		}
		if (type == TokenType.CLOSE_TAG && ch != '}') {
			throw new LexerException(
					"Invalid use of character '$'. Next to that character should be '}', and not " + ch);
		}
		if (type == TokenType.NAME || type == TokenType.FUNCTION) {
			if (!(Character.isDigit(ch) || Character.isLetter(ch) || ch == '_')) {
				throw new LexerException(
						"'" + sb + ch + "' is not a valid name. Names can contain letters, digits and '_'.");
			}
		}
		if (type == TokenType.FUNCTION) {
			if (sb.toString().length() == 1 && !Character.isLetter(ch)) {
				throw new LexerException("A function name has to start with a letter, and not with '" + ch + "'.");
			}
		}
	}

	/**
	 * Metoda detektira tip tokena samo na temelju danog prvog znaka s
	 * pretpostavkom da je stanje lexera IN_TAG. Znak nije dan izravno, već
	 * preko njegovog indexa u polju znakova data.
	 *
	 * @return detektirani tip tokena
	 */
	private TokenType detectTokenType(int i) {

		if (data[i] == '{') {
			return TokenType.OPEN_TAG;
		}
		if (data[i] == '$') {
			return TokenType.CLOSE_TAG;
		}
		if (data[i] == '@') {
			return TokenType.FUNCTION;
		}
		if (data[i] == '\"') {
			return TokenType.STRING;
		}
		if (Character.isDigit(data[i]) || data[i] == '-' && i + 1 < data.length && Character.isDigit(data[i + 1])) {
			return TokenType.DOUBLE_CONST;
		}
		if (Character.isLetter(data[i])) {
			return TokenType.NAME;
		}
		return TokenType.SYMBOL;

	}

	/**
	 * Pomoćna metoda koja generira token tipa STRING. Ako string nije ispravan,
	 * baca iznimku LexerException.
	 *
	 * @return token tipa STRING
	 */
	private Token stringToken() {
		StringBuilder tokenSB = new StringBuilder();
		int lastEscapeCharIndex = -2;

		for (int i = currentIndex; i < data.length; i++, currentIndex++) {
			if (data[i] == '\\' && lastEscapeCharIndex < i - 1) {
				if (i + 1 == data.length) {
					break;
				}

				if (data[i + 1] == '\\' || data[i + 1] == '\"' || data[i + 1] == 'n' || data[i + 1] == 'r'
						|| data[i + 1] == 't') {
					lastEscapeCharIndex = i;
					continue;
				} else {
					throw new LexerException(
							"The character '\\' should be followed by some of the following characters: \\,\",n,r,t. Character found: '"
									+ data[i + 1] + "'");
				}
			}

			if ((data[i] == 'n' || data[i] == 'r' || data[i] == 't') && lastEscapeCharIndex == i - 1) {
				char c;
				if (data[i] == 'n') {
					c = '\n';
				} else if (data[i] == 'r') {
					c = '\r';
				} else {
					c = '\t';
				}
				tokenSB.append(c);
			} else {
				tokenSB.append(data[i]);
			}

			if (tokenSB.toString().length() > 1 && data[i] == '\"' && !(i > 0 && data[i - 1] == '\\')) {
				currentIndex++;
				break;
			}

			if (data[i] == '\n') {
				throw new LexerException("A string can not be stretched in more than a one row.");
			}
		}

		if (tokenSB.toString().toCharArray()[tokenSB.toString().length() - 1] != '\"'
				|| lastEscapeCharIndex == currentIndex - 2) {
			throw new LexerException("String has to be closed by character \".");
		}

		return token = new Token(TokenType.STRING, tokenSB.toString());
	}

	/**
	 * Pomoćna metoda koja vraća token tipa TEXT. Metoda će iščitati tekst sve
	 * do nailaska na početak taga, odnosno do nailaska na znak '{' - iznimka je
	 * podniz "\{" koji se ne tumači kao početak taga već kao znak '{'. Metoda
	 * na svom kraju izvođenja automatski prebacuje stanje Lexera u IN_TAG jer
	 * se podrazumijeva da će sljedeći token biti ili EOF ili početak taga
	 *
	 * @return token tipa TEXT
	 */
	private Token textToken() {
		StringBuilder tokenSB = new StringBuilder();
		int lastEscapeCharIndex = -2;

		for (int i = currentIndex; i < data.length; i++, currentIndex++) {
			if (data[i] == '\\' && lastEscapeCharIndex < i - 1) {
				if (i + 1 == data.length) {
					throw new LexerException("'\\' should be positioned only to the left of a digit or another '\\'.");
				}

				if (data[i + 1] == '{' || data[i + 1] == '\\') {
					lastEscapeCharIndex = i;
					continue;
				} else {
					throw new LexerException(
							"The character '\\' should be followed by '\\' or '{', and not by '" + data[i + 1] + "'");
				}
			}

			if (data[i] == '{' && !(i > 0 && data[i - 1] == '\\')) {
				break;
			}

			tokenSB.append(data[i]);
		}

		state = LexerState.IN_TAG;

		return token = new Token(TokenType.TEXT, tokenSB.toString());
	}

	/**
	 * Vraća zadnje generirani token.
	 *
	 * @return zadnje generirani token
	 */
	public Token getToken() {
		return token;
	}
}
