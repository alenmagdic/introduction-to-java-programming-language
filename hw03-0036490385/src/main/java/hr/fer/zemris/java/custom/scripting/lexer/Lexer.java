package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.hw03.prob1.LexerException;

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
			throw new IllegalArgumentException("Lexer ne može biti konstruiran sa argumentom null");
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
			throw new LexerException("Svi tokeni su već potrošeni.");
		}
		if (currentIndex == data.length) {
			return token = new Token(TokenType.EOF, null);
		}

		if (state == LexerState.OUT_TAG) {
			return token = textToken();
		}

		// pretpostavka o tipu ovog tokena, pretpostavlja se na temelju prvog
		// znaka, a kasnije se može promijeniti npr. iz DOUBLE_CONST u INT_CONST
		TokenType detectedTokenType = null;
		StringBuilder tokenSB = new StringBuilder();

		for (int i = currentIndex; i < data.length; i++, currentIndex++) {

			if (data[i] == ' ' || data[i] == '\r' || data[i] == '\n' || data[i] == '\t') {
				// razmak je na početku čitanja ignorabilan, inače predstavlja
				// kraj tokena
				if (tokenSB.toString().length() == 0) {
					continue;
				}
				break;
			}

			// detekcija tipa tokena na temelju prvog neignorabilnog znaka
			if (detectedTokenType == null) {
				detectedTokenType = detectTokenType(i);

				if (detectedTokenType == TokenType.STRING) {
					return token = stringToken();
				}

				// simbol je samo jedan znak -> dakle odma po nailasku se vraća
				// novi token
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

			// tagovi imaju najviše dva znaka
			if ((detectedTokenType == TokenType.OPEN_TAG || detectedTokenType == TokenType.CLOSE_TAG)
					&& tokenSB.toString().length() == 2) {
				currentIndex++;
				if (detectedTokenType == TokenType.CLOSE_TAG) {
					state = LexerState.OUT_TAG;
				}
				break;
			}

			// ako je sljedeći znak '$', to nagovještava da je dostignut kraj
			// konstrukcije novog tokena
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
			throw new LexerException("Funkcija mora imati naveden naziv.");
		}

		if (type == TokenType.DOUBLE_CONST) {
			try {
				// prvo provjeri može li se protumačiti kao integer
				value = Integer.parseInt(string);
				type = TokenType.INT_CONST;
			} catch (NumberFormatException ex) {
				try {
					value = Double.parseDouble(string);
				} catch (NumberFormatException e) {
					throw new LexerException("Izraz '" + string + "' nije ispravan.");
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
			throw new LexerException("Neispravno korištenje znaka '{'. Nakon njega mora slijediti '$', a ne " + ch);
		}
		if (type == TokenType.CLOSE_TAG && ch != '}') {
			throw new LexerException("Neispravno korištenje znaka '$'. Nakon njega mora slijediti '}', a ne " + ch);
		}
		if (type == TokenType.NAME || type == TokenType.FUNCTION) {
			if (!(Character.isDigit(ch) || Character.isLetter(ch) || ch == '_')) {
				throw new LexerException(
						"'" + sb + ch + "' je neispravan naziv. Nazivi mogu sadržavati slova, brojke ili '_'.");
			}
		}
		if (type == TokenType.FUNCTION) {
			if (sb.toString().length() == 1 && !Character.isLetter(ch)) {
				throw new LexerException("Naziv funkcije mora početi slovom, a ne znakom '" + ch + "'.");
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

		// predstavlja index na kojem je escape character zadnji put pronađen;
		// za početnu vrijednost je bitno da bude <= -2 kako bi detekcija
		// uspješno radila u svim slučajevima
		int lastEscapeCharIndex = -2;

		for (int i = currentIndex; i < data.length; i++, currentIndex++) {

			// detekcija escape charactera i provjera ispravnosti njegova
			// korištenja
			if (data[i] == '\\' && lastEscapeCharIndex < i - 1) {
				if (i + 1 == data.length) {
					// pri izlasku iz petlje bit će bačen exception
					break;
				}

				if (data[i + 1] == '\\' || data[i + 1] == '\"' || data[i + 1] == 'n' || data[i + 1] == 'r'
						|| data[i + 1] == 't') {
					lastEscapeCharIndex = i;
					continue;
				} else {
					throw new LexerException(
							"Nakon '\\' se očekuje neki od sljedećih znakova: \\,\",n,r,t. Umjesto njih pronađen je znak '"
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

			// detekcija kraja niza
			if (tokenSB.toString().length() > 1 && data[i] == '\"' && !(i > 0 && data[i - 1] == '\\')) {
				currentIndex++;
				break;
			}

			// detekcija ilegalnog prelijevanja stringa u novi red
			if (data[i] == '\n') {
				throw new LexerException("String se ne može protezati u više od jednog reda.");
			}
		}

		if (tokenSB.toString().toCharArray()[tokenSB.toString().length() - 1] != '\"'
				|| lastEscapeCharIndex == currentIndex - 2) {
			throw new LexerException("String mora biti završen znakom \".");
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

		// predstavlja index na kojem je escape character zadnji put pronađen;
		// za početnu vrijednost je bitno da bude <= -2 kako bi detekcija
		// uspješno radila u svim slučajevima
		int lastEscapeCharIndex = -2;

		for (int i = currentIndex; i < data.length; i++, currentIndex++) {

			// detekcija escape charactera i provjera ispravnosti njegova
			// korištenja
			if (data[i] == '\\' && lastEscapeCharIndex < i - 1) {
				if (i + 1 == data.length) {
					throw new LexerException(
							"'\\' ne može stajati na kraju niza već samo ispred znamenke ili samog znaka '\\'");
				}

				if (data[i + 1] == '{' || data[i + 1] == '\\') {
					lastEscapeCharIndex = i;
					continue;
				} else {
					throw new LexerException("Nakon '\\' se očekuje ili '\\' ili '{', a ne '" + data[i + 1] + "'");
				}
			}

			// detekcija kraja niza
			if (data[i] == '{' && !(i > 0 && data[i - 1] == '\\')) {
				break;
			}

			tokenSB.append(data[i]);
		}

		// automatski pripremi način rada za sljedeće generiranje tokena koje će
		// biti ili početak taga ili EOF
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
