package hr.fer.zemris.java.hw04.db;

/**
 * A class that represents a token.
 *
 * @author Alen Magdić
 *
 */
public class Token {
	/** Token type **/
	private TokenType type;
	/** Value of token **/
	private Object value;

	/**
	 * Constructor.
	 *
	 * @param type
	 *            token type
	 * @param value
	 *            value of token
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Gets the value of token.
	 *
	 * @return value of token
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the token type.
	 *
	 * @return token type
	 */
	public TokenType getType() {
		return type;
	}

}
