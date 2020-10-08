package hr.fer.zemris.java.hw04.db;

import java.util.Arrays;

/**
 * This lexer splits a given query to tokens. There are five types of tokens:
 * NAME, OPERATOR, LOGICAL_OPERATOR, STRING, EOF. The NAME token represents an
 * attribute name. The OPERATOR represents the following operators: LIKE, >=,
 * <=, >, <, !=, =. There is only one LOGICAL_OPERATOR, an AND which is not case
 * sensitive, so the lexer recognize 'AND','and','And', etc. as a
 * LOGICAL_OPERATOR. The STRING token is a piece of text surrounded by the
 * quotation marks. EOF token represents the end of file. If there is any piece
 * of text that can not be a part of any supported tokens, a LexerException
 * exception is thrown.
 *
 * @author Alen Magdić
 *
 */
public class QueryLexer {
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
	 * An array of supported operators.
	 */
	private static final String[] SUPPORTED_OPERATORS;

	static {
		SUPPORTED_OPERATORS = new String[] { "LIKE", ">=", "<=", ">", "<", "!=", "=" };
	}

	/**
	 * Constructor.
	 *
	 * @param text
	 *            textual data that is to be tokenized
	 */
	public QueryLexer(String text) {
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
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("There are no any more tokens.");
		}

		skipBlanks();

		if (!indexInBounds(currentIndex, data)) {
			return token = new Token(TokenType.EOF, null);
		}

		TokenType detectedTokenType = detectTokenTypeFromCharAtIndex(currentIndex);

		if (detectedTokenType == TokenType.NAME) {
			return token = createNameToken();
		} else if (detectedTokenType == TokenType.STRING) {
			return token = createStringToken();
		} else if (detectedTokenType == TokenType.LOGICAL_OPERATOR) {
			return token = createLogicalOperatorToken();
		} else {
			return token = createOperatorToken();
		}
	}

	/**
	 * Creates and returns a LOGICAL_OPERATOR token.
	 *
	 * @return LOGICAL_OPERATOR token
	 */
	private Token createLogicalOperatorToken() {
		String tokenValue = createStringFromDataInRange(currentIndex, currentIndex + "and".length());
		currentIndex += "and".length();
		return new Token(TokenType.LOGICAL_OPERATOR, tokenValue);
	}

	/**
	 * Creates and returns an OPERATOR token.
	 *
	 * @return OPERATOR token
	 */
	private Token createOperatorToken() {
		String operator = getOperatorAtIndex(currentIndex);
		currentIndex += operator.length();
		return new Token(TokenType.OPERATOR, operator);
	}

	/**
	 * Creates and returns a name token.
	 *
	 * @return NAME token
	 */
	private Token createNameToken() {
		int tokenLength = 0;
		for (int i = currentIndex; i < data.length; i++, currentIndex++) {
			if (!charCanBePartOfName(data[i])) {
				break;
			}
			tokenLength++;
		}
		return new Token(TokenType.NAME, createStringFromDataInRange(currentIndex - tokenLength, currentIndex));
	}

	/**
	 * A private method that creates a STRING token. The method expects that
	 * before it is called, the first unprocessed character is a quotation mark.
	 * The method will automatically skip that character assuming that there
	 * really is a quotation mark (not checking is ok since the method is
	 * private). If there are no another quotation marks that should represent
	 * the end of the string, the LexerException exception is thrown.
	 *
	 * @return STRING token
	 */
	private Token createStringToken() {
		int tokenLength = 0;
		currentIndex++;
		for (int i = currentIndex; i < data.length; i++, currentIndex++) {
			if (i == data.length - 1 && data[i] != '\"') {
				throw new LexerException("There is no quotation mark that would represent the end of the string.");
			}

			if (data[i] == '\"') {
				currentIndex++;
				break;
			}
			tokenLength++;
		}

		return new Token(TokenType.STRING,
				createStringFromDataInRange(currentIndex - tokenLength - 1, currentIndex - 1));
	}

	/**
	 * Detects the type of the next token whose starting point is at the
	 * specified index.
	 *
	 * @param index
	 *            the starting point of the token whose type is to be detected
	 * @return detected token type
	 */
	private TokenType detectTokenTypeFromCharAtIndex(int index) {
		if (data[index] == '\"') {
			return TokenType.STRING;
		} else if (isKeywordAtIndex("and", index, CaseSensitivity.INSENSITIVE)) {
			return TokenType.LOGICAL_OPERATOR;
		} else if (isAnyOperatorAtIndex(index)) {
			return TokenType.OPERATOR;
		} else if (Character.isLetter(data[index])) {
			return TokenType.NAME;
		} else {
			throw new LexerException("Invalid expression!");
		}
	}

	/**
	 * The method checks if there is any operator at the specified index.
	 *
	 * @param index
	 *            the starting position of the check
	 * @return true if there is an operator at specified index
	 *
	 */
	private boolean isAnyOperatorAtIndex(int index) {
		return getOperatorAtIndex(index) != null;
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
		for (String op : SUPPORTED_OPERATORS) {
			if (isKeywordAtIndex(op, index, CaseSensitivity.SENSITIVE)) {
				return createStringFromDataInRange(index, index + op.length());
			}
		}
		return null;
	}

	/**
	 * The method checks if there is a specified keyword at the specified index.
	 * The check is done using the specified CaseSensitivity.
	 *
	 *
	 * @param keyword
	 *            a keyword whose existence at the specified index is checked
	 * @param index
	 *            the starting position of the check
	 * @param caseSensitivity
	 *            a type of case sensitivity that is to be used for checking if
	 *            there is a specified keyword at the specified index
	 * @return true if there is a specified keyword at the specified index
	 *         taking into consideration the specified case sensitivity
	 */
	private boolean isKeywordAtIndex(String keyword, int index, CaseSensitivity caseSensitivity) {
		if (data.length - index < keyword.length()) {
			return false;
		}

		int indexAfterPossibleKeyword = index + keyword.length();
		if (indexInBounds(indexAfterPossibleKeyword, data) && !isBlankChar(data[indexAfterPossibleKeyword])
				&& data[indexAfterPossibleKeyword] != '\"') {
			return false;
		}

		String dataSubstring = createStringFromDataInRange(index, index + keyword.length());

		return caseSensitivity == CaseSensitivity.SENSITIVE ? dataSubstring.equals(keyword)
				: dataSubstring.toLowerCase().equals(keyword);
	}

	/**
	 * A method that generates a String from the specified range of the data
	 * array. The first included character will be the character at index
	 * 'from', while the last included character will be the character at index
	 * 'to'-1.
	 *
	 * @param from
	 *            index of the first character that is to be included in the
	 *            string
	 * @param to
	 *            index of the first character that is after the last character
	 *            to be included in the string
	 * @return generated string
	 */
	private String createStringFromDataInRange(int from, int to) {
		char[] subArray = Arrays.copyOfRange(data, from, to);
		return new String(subArray);
	}

	/**
	 * Checks if the specified character can be a part of an attribute name. A
	 * character can be part of an attribute name if it is a letter, a digit or
	 * '_'. There can be only a letter at the start of an attribute name.
	 *
	 * @param c
	 *            character that is to be checked
	 * @return true if a specified character can be a part of an attribute name
	 */
	private boolean charCanBePartOfName(char c) {
		return Character.isLetter(c) || Character.isDigit(c) || c == '_';
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
	 * An enum that represents a case sensitivity.
	 *
	 * @author Alen Magdić
	 *
	 */
	private static enum CaseSensitivity {
		/** Sensitive case **/
		SENSITIVE,
		/** Insensitive case **/
		INSENSITIVE;
	}

	/**
	 * Returns the last generated token.
	 *
	 * @return the last generated token
	 */
	public Token getToken() {
		return token;
	}

}
