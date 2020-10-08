package hr.fer.zemris.bf.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * This lexer splits a given logical expression to tokens. There are six types
 * of tokens: VARIABLE, OPERATOR, CONSTANT, OPEN_BRACKET, CLOSED_BRACKET, EOF.
 * The VARIABLE token represents a variable name. The OPERATOR represents the
 * following operators: or(+), and(*), xor(:+:), not. The CONSTANT token
 * represents a constant value, true(1) or false(0). There are OPEN_BRACKET and
 * CLOSED_BRACKET tokens that represents the brackets. The EOF token represents
 * the end of file. If there is any piece of text that can not be a part of any
 * supported tokens, a LexerException exception is thrown.
 *
 * @author Alen Magdić
 *
 */

public class Lexer {
	/**
	 * The data that is tokenized.
	 */
	private char[] data;
	/**
	 * The last generated token.
	 */
	private Token token;
	/**
	 * Index of the first unprocessed character.
	 */
	private int currentIndex;
	/**
	 * A map of operators with operator symbols as keys and operator names as
	 * values.
	 */
	private static final Map<String, String> OPERATORS_MAP;
	/**
	 * A collection of operator names.
	 */
	private static final Collection<String> OPERATORS;
	/**
	 * A collection of operator symbols.
	 */
	private static final Collection<String> OPERATOR_SYMBOLS;
	/**
	 * A list of constant names.
	 */
	private static final List<String> CONSTANTS;

	static {
		OPERATORS_MAP = new HashMap<>();
		OPERATORS_MAP.put("*", "and");
		OPERATORS_MAP.put("+", "or");
		OPERATORS_MAP.put("!", "not");
		OPERATORS_MAP.put(":+:", "xor");
		OPERATORS = OPERATORS_MAP.values();
		OPERATOR_SYMBOLS = OPERATORS_MAP.keySet();

		CONSTANTS = new ArrayList<String>(2);
		CONSTANTS.add("true");
		CONSTANTS.add("false");
	}

	/**
	 * Constructor.
	 *
	 * @param text
	 *            textual data that is to be tokenized
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Argument null is not legal.");
		}

		this.data = text.toCharArray();
	}

	/**
	 * Returns the next token.
	 *
	 * @return the next token
	 */
	public Token nextToken() {
		if (token != null && token.getTokenType() == TokenType.EOF) {
			throw new LexerException("There are no any more tokens.");
		}

		skipBlanks();

		if (!indexInBounds(currentIndex, data)) {
			return token = new Token(TokenType.EOF, null);
		}

		if (Character.isLetter(data[currentIndex])) {
			String identifier = extractIdentifier().toLowerCase();

			if (OPERATORS.contains(identifier)) {
				return token = new Token(TokenType.OPERATOR, identifier);
			} else if (identifier.equals("true") || identifier.equals("false")) {
				return token = new Token(TokenType.CONSTANT, identifier.equals("true") ? Boolean.TRUE : Boolean.FALSE);
			} else {
				return token = new Token(TokenType.VARIABLE, identifier.toUpperCase());
			}
		}

		if (Character.isDigit(data[currentIndex])) {
			String numericString = extractNumericString().toLowerCase();

			if (numericString.equals("0") || numericString.equals("1")) {
				return token = new Token(TokenType.CONSTANT, numericString.equals("1") ? Boolean.TRUE : Boolean.FALSE);
			} else {
				throw new LexerException("Unexpected number: " + numericString);
			}

		}

		if (data[currentIndex] == '(') {
			currentIndex++;
			return new Token(TokenType.OPEN_BRACKET, '(');
		} else if (data[currentIndex] == ')') {
			currentIndex++;
			return new Token(TokenType.CLOSED_BRACKET, ')');
		}

		String operatorSymbol = getOperatorAtIndex(currentIndex);
		if (operatorSymbol != null) {
			currentIndex += operatorSymbol.length();
			return new Token(TokenType.OPERATOR, OPERATORS_MAP.get(operatorSymbol));
		}

		throw new LexerException("Unable to parse. Invalid data given. Data: " + new String(data));
	}

	/**
	 * Extracts a numeric string (a string containing a number) that is supposed
	 * to be at the index of the first unprocessed character.
	 *
	 * @return a numeric string, i.e. a string containing a number
	 */
	private String extractNumericString() {
		StringBuilder stringB = new StringBuilder();
		for (int i = currentIndex; i < data.length; i++, currentIndex++) {
			if (Character.isDigit(data[i])) {
				stringB.append(data[i]);
			} else {
				break;
			}
		}
		return stringB.toString();
	}

	/**
	 * Extracts an indetifier that is supposed to be at the index of the first
	 * unprocessed character. An indetifier starts with a letter and contains
	 * letters, digits and '_'.
	 *
	 * @return an identifier
	 */
	private String extractIdentifier() {
		StringBuilder stringB = new StringBuilder();
		for (int i = currentIndex; i < data.length; i++, currentIndex++) {
			if (Character.isLetterOrDigit(data[i]) || data[i] == '_') {
				stringB.append(data[i]);
			} else {
				break;
			}
		}
		return stringB.toString();
	}

	/**
	 * The method returns the operator placed at the specified index. If there
	 * is no any supported operator, the null is returned.
	 *
	 * @param index
	 *            the starting position of the operator
	 * @return operator at the specified index
	 */
	private String getOperatorAtIndex(int index) {
		for (String op : OPERATOR_SYMBOLS) {
			if (isKeywordAtIndex(op, index)) {
				return op;
			}
		}
		return null;
	}

	/**
	 * The method checks if there is a specified keyword at the specified index.
	 *
	 * @param keyword
	 *            a keyword whose existence at the specified index is checked
	 * @param index
	 *            the starting position of the check
	 * @return true if there is a specified keyword at the specified index
	 */
	private boolean isKeywordAtIndex(String keyword, int index) {
		if (data.length - index < keyword.length()) {
			return false;
		}

		String dataSubstring = new String(Arrays.copyOfRange(data, index, index + keyword.length()));
		return dataSubstring.equals(keyword);
	}

	/**
	 * Returns true if the specified character is a blank character. A blank
	 * character is one of the following characters: ' ','\t','\n','\r'.
	 *
	 * @param c
	 *            a character that is to be checked
	 * @return true if the specified character is a blank character
	 */
	private boolean isBlankChar(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	/**
	 * Skips all blank characters.
	 */
	private void skipBlanks() {
		while (indexInBounds(currentIndex, data) && isBlankChar(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Checks if the specified index is in bounds of the specified array.
	 *
	 * @param index
	 *            index that is to be checked
	 * @param array
	 *            an array
	 * @return true if index is in bounds of the specified array
	 */
	private boolean indexInBounds(int index, char[] array) {
		return index < array.length;
	}
}
