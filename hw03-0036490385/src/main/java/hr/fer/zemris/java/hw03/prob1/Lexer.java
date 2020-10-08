package hr.fer.zemris.java.hw03.prob1;

/**
 * Klasa koja omogućuje leksičku analizu danog teksta. Lexer vraća tokene
 * modelirane razredom Token. Lexer ima dva stanja definirana enumeratorom
 * LexState.
 *
 * U stanju BASIC, Lexer sve nizove znamenaka tretira kao broj, odnosno
 * TokenType.NUMBER. Sve nizove slova tretira kao riječ, odnosno TokenType.WORD.
 * Sve ostale znakove, izuzevši ignorabilne (\n,\r,\t,razmak), tretira kao
 * simbol, odnosno TokenType.SYMBOL. Postoji i escape character '\' koji
 * označava da sljedeću znamenku treba tretirati kao dio riječi. Osim znamenke,
 * iza '\' se može nalaziti još jedan '\' pri čemu se on tretira kao taj sam
 * simbol. Ako se iza escape charactera nalazi bilo što drugo (uključujući i
 * kraj teksta), baca se iznimka LexerException.
 *
 * U stanju EXTENDED, Lexer sve nizove znakova tretira kao riječi, izuzevši
 * simbol '#' kojeg tretira kao simbol.
 *
 * Pozivatelju se omogućuje da postavlja stanje Lexera, pri čemu nije dozvoljeno
 * Lexer postaviti u stanje null.
 *
 * @author Alen Magdić
 *
 */
public class Lexer {
	/** Polje znakova u koje se pohranjuje tekst za analizu. **/
	private char[] data;
	/** Zadnje generirani token. **/
	private Token token;
	/** Index prvog sljedećeg neobrađenog znaka. **/
	private int currentIndex;
	/** Stanje Lexera. Može biti BASIC ili EXTENDED. **/
	private LexerState state;

	/**
	 * Konstruktor. Prima tekst koji je potrebno analizirati.
	 *
	 * @param text
	 *            tekst koji je potrebno analizirati
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Lexer ne može biti konstruiran sa argumentom null");
		}

		this.data = text.toCharArray();
		state = LexerState.BASIC;
	}

	/**
	 * Vraća sljedeći token iz niza. Ako se pozove nakon što je vraćen EOF, baca
	 * se iznimka LexerException. Ista iznimka se baca i kada se u stanju BASIC
	 * nakon escape znaka '\' nađe nešto što nije niti znamenka niti escape
	 * znak. Ako se naiđe na preveliki broj, također se baca ista iznimka.
	 *
	 * @return sljedeći token iz niza
	 * @throws LexerException
	 *             kada se pozove nakon što je vraćen EOF; ako nakon '\' slijedi
	 *             neočekivani znak; kada se pronađe preveliki broj
	 */
	public Token nextToken() throws LexerException {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Svi tokeni su već potrošeni.");
		}
		if (currentIndex == data.length) {
			return token = new Token(TokenType.EOF, null);
		}

		// prvo se detektira tip prvog sljedećeg znaka pa se od sljedećih
		// očekuje da budu tog istog tipa - pri nailasku na znak drugog tipa,
		// prekida se obrada i vraća se generirani token
		TokenType detectedTokenType = null;

		// pomoću ovoga se detektira predstavlja
		// li '\\' escape character ili stvarni
		// znak '\\', početna vrijednost ne smije biti veća od -2, odnosno smije
		// biti <=-2
		int lastEscapeCharIndex = -2;

		StringBuilder tokenSB = new StringBuilder();

		for (int i = currentIndex; i < data.length; i++, currentIndex++) {

			// detekcija ignorabilnih charactera
			if (data[i] == ' ' || data[i] == '\r' || data[i] == '\n' || data[i] == '\t') {
				// razmak je na početku čitanja ignorabilan, inače predstavlja
				// kraj tokena
				if (tokenSB.toString().length() == 0) {
					continue;
				}
				break;
			}

			// detekcija escape charactera i provjera ispravnosti njegova
			// korištenja
			if (state == LexerState.BASIC && data[i] == '\\' && lastEscapeCharIndex < i - 1) {
				if (i + 1 == data.length) {
					throw new LexerException(
							"'\\' ne može stajati na kraju niza već samo ispred znamenke ili samog znaka '\\'");
				}

				if (Character.isDigit(data[i + 1]) || data[i + 1] == '\\') {
					lastEscapeCharIndex = i;
					continue;
				} else {
					throw new LexerException("Nakon '\\' se očekuje znamenka ili '\\', a ne '" + data[i + 1] + "'");
				}
			}

			// detekcija tipa ovog tokena
			if (detectedTokenType == null) {
				detectedTokenType = detectTokenType(i);
				tokenSB.append(data[i]);
				if (detectedTokenType == TokenType.SYMBOL) {
					currentIndex++;
					break;
				}
				continue;
			}

			if (detectedTokenType != detectTokenType(i)) {
				break;
			}
			tokenSB.append(data[i]);
		}

		if (tokenSB.toString().length() == 0) {
			return token = new Token(TokenType.EOF, null);
		}
		token = newTokenFromSB(tokenSB, detectedTokenType);
		return token;
	}

	/**
	 * Detektira kojeg bi tipa trebao biti sljedeći token na temelju znaka na
	 * mjestu index. Metoda uzima u obzir i način rada Lexera te postojanje
	 * znaka '\' ispred zadanog indexa.
	 *
	 * @param index
	 *            pozicija znaka na temelju kojeg se detektira tip tokena
	 * @return tip tokena određen na temelju danog indexa
	 */
	private TokenType detectTokenType(int index) {
		if (state == LexerState.EXTENDED && data[index] == '#') {
			return TokenType.SYMBOL;
		}
		if (state == LexerState.EXTENDED || Character.isLetter(data[index])
				|| index > 0 && data[index - 1] == '\\' && (Character.isDigit(data[index]) || data[index] == '\\')) {
			return TokenType.WORD;
		}
		if (Character.isDigit(data[index])) {
			return TokenType.NUMBER;
		}

		return TokenType.SYMBOL;
	}

	/**
	 * Metoda konstruira i vraća token na temelju podataka pohranjenih u danom
	 * StringBuilderu. Tip tokena je zadan te metoda ne vrši provjeru je li dani
	 * tip u skladu s onim što je u StringBuilderu već se kontrola prepušta
	 * pozivatelju.
	 *
	 * @param sb
	 *            StringBuilder iz kojeg se iščitava vrijednost novog tokena
	 * @param type
	 *            tip kojeg treba biti novi token
	 * @return novi token zadanog tipa i vrijednosti iščitane iz danog
	 *         StringBuildera
	 */
	private Token newTokenFromSB(StringBuilder sb, TokenType type) {
		String string = sb.toString();
		Object value;

		if (type == TokenType.NUMBER) {
			try {
				value = Long.parseLong(string);
			} catch (NumberFormatException ex) {
				throw new LexerException("Broj " + string + " nije moguće pohraniti.");
			}
		} else if (type == TokenType.WORD) {
			value = string;
		} else {
			value = string.toCharArray()[0];
		}

		return new Token(type, value);
	}

	/**
	 * Vraća zadnje generirani token.
	 *
	 * @return zadnje generirani token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Postavlja stanje Lexera. Moguća stanja su BASIC i EXTENDED. Nije
	 * dozovljeno postaviti stanje null. Načini rada Lexera ovisno o stanju,
	 * detaljno su opisani u dokumentaciji samog razreda.
	 *
	 * @param state
	 *            novo stanje Lexera
	 */
	public void setState(LexerState state) {
		if (state == null) {
			throw new IllegalArgumentException("Lexer ne može imati stanje null");
		}
		this.state = state;
	}

}
