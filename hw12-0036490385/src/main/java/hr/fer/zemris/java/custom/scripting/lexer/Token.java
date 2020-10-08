package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Klasa koja predstavlja token.
 *
 * @author Alen Magdić
 *
 */
public class Token {
	/** Tip tokena **/
	private TokenType type;
	/** Vrijednost pohranjena u tokenu **/
	private Object value;

	/**
	 * Konstruktor. Prima tip tokena te vrijednost koju treba pohraniti.
	 *
	 * @param type
	 *            tip tokena
	 * @param value
	 *            vrijednost koju token treba pohraniti
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Vraća vrijednost pohranjenu u tokenu.
	 *
	 * @return vrijednost pohranjena u tokenu
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Vraća tip tokena.
	 *
	 * @return tip tokena
	 */
	public TokenType getType() {
		return type;
	}

	@Override
	public String toString() {
		if (value != null) {
			return "(" + type.toString() + ", " + value.toString() + ")";
		}
		return "(" + type.toString() + ", null)";
	}
}
