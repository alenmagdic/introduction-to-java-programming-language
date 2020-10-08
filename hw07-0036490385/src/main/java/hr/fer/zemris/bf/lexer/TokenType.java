package hr.fer.zemris.bf.lexer;

/**
 * An enumerator representing token types.
 *
 * @author Alen Magdić
 *
 */
public enum TokenType {
	/** End of file **/
	EOF,
	/** Variable name **/
	VARIABLE,
	/** A constant, true or false **/
	CONSTANT,
	/** A logical operator **/
	OPERATOR,
	/** Open bracket **/
	OPEN_BRACKET,
	/** Closed bracket **/
	CLOSED_BRACKET;
}
