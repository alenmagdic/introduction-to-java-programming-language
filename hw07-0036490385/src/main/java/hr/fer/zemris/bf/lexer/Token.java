package hr.fer.zemris.bf.lexer;

/**
 * A class that represents a token.
 *
 * @author Alen Magdić
 *
 */
public class Token {
	/** Token type **/
	private TokenType tokenType;
	/** Value of token **/
	private Object tokenValue;

	/**
	 * Constructor.
	 *
	 * @param tokenType
	 *            token type
	 * @param tokenValue
	 *            value of token
	 */
	public Token(TokenType tokenType, Object tokenValue) {
		if (tokenType == null) {
			throw new IllegalArgumentException("Token type can not be null.");
		}

		this.tokenType = tokenType;
		this.tokenValue = tokenValue;
	}

	/**
	 * Gets the value of token.
	 *
	 * @return value of token
	 */
	public Object getTokenValue() {
		return tokenValue;
	}

	/**
	 * Gets the token type.
	 *
	 * @return token type
	 */
	public TokenType getTokenType() {
		return tokenType;
	}

	@Override
	public String toString() {
		if (tokenValue == null) {
			return "Type: " + tokenType + ", Value: null";
		}
		return "Type: " + tokenType + ", Value: " + tokenValue + ", Value is instance of: "
				+ tokenValue.getClass().getName();
	}

}
