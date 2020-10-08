package hr.fer.zemris.java.hw04.db;

/**
 * An enum that represents various types of tokens.
 *
 * @author Alen Magdić
 *
 */
public enum TokenType {
	/**
	 * A token representing an attribute name.
	 */
	NAME,
	/**
	 * A token representing an operator.
	 */
	OPERATOR,
	/**
	 * A token representing a logical operator.
	 */
	LOGICAL_OPERATOR,
	/**
	 * A token representing a string.
	 */
	STRING,
	/**
	 * A token representing an end of file.
	 */
	EOF;
}
