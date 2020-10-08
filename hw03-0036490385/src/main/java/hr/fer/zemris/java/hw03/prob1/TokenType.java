package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumerator koji predstavlja tip tokena. Mogući tipovi su EOF, koji
 * predstavlja završetak teksta, WORD, NUMBER te SYMBOL.
 *
 * @author Alen Magdić
 *
 */
public enum TokenType {
	/** Predstavlja tip tokena koji predstavlja završetak datoteke. **/
	EOF("EOF"),
	/**
	 * Predstavlja tip tokena koji sadrži riječ.
	 */
	WORD("WORD"),
	/** Predstavlja tip tokena koji sadrži broj. **/
	NUMBER("NUMBER"),
	/** Predstavlja tip tokena koji sadrži jedan simbol **/
	SYMBOL("SYMBOL");
	/** String reprezentacija tipa tokena. **/
	private String string;

	/**
	 * Konstruktor. Prima string reprezentaciju tipa tokena.
	 *
	 * @param string
	 *            string reprezentacija tipa tokena
	 */
	private TokenType(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}
}
