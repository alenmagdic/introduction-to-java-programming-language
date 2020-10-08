package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

/**
 * A parser that parses database queries.
 *
 * @author Alen Magdić
 *
 */
public class QueryParser {
	/**
	 * A lexer used to parse a query.
	 */
	private QueryLexer lexer;
	/**
	 * A list of conditional expression created as a result of parsing a query.
	 */
	private List<ConditionalExpression> query;
	/**
	 * A buffer that should, when filled, contain three tokens in this order:
	 * NAME, OPERATOR, STRING.
	 */
	private List<Token> tokenBuffer;
	/**
	 * Size of the token buffer.
	 */
	private static final int TOKEN_BUFFER_SIZE = 3;
	/**
	 * A map used to store all supported attribute names as keys and field value
	 * getters as values.
	 */
	private static SimpleHashtable<String, IFieldValueGetter> MAP_OF_SUPPORTED_ATTRIBUTES;
	/**
	 * A map used to store strings of all supported operators as keys and
	 * comparison operators as values.
	 */
	private static SimpleHashtable<String, IComparisonOperator> MAP_OF_SUPPORTED_OPERATORS;

	static {
		MAP_OF_SUPPORTED_ATTRIBUTES = new SimpleHashtable<>();
		MAP_OF_SUPPORTED_ATTRIBUTES.put("firstName", FieldValueGetters.FIRST_NAME);
		MAP_OF_SUPPORTED_ATTRIBUTES.put("lastName", FieldValueGetters.LAST_NAME);
		MAP_OF_SUPPORTED_ATTRIBUTES.put("jmbag", FieldValueGetters.JMBAG);

		MAP_OF_SUPPORTED_OPERATORS = new SimpleHashtable<>();
		MAP_OF_SUPPORTED_OPERATORS.put("<", ComparisonOperators.LESS);
		MAP_OF_SUPPORTED_OPERATORS.put("<=", ComparisonOperators.LESS_OR_EQUALS);
		MAP_OF_SUPPORTED_OPERATORS.put(">", ComparisonOperators.GREATER);
		MAP_OF_SUPPORTED_OPERATORS.put(">=", ComparisonOperators.GREATER_OR_EQUALS);
		MAP_OF_SUPPORTED_OPERATORS.put("=", ComparisonOperators.EQUALS);
		MAP_OF_SUPPORTED_OPERATORS.put("!=", ComparisonOperators.NOT_EQUALS);
		MAP_OF_SUPPORTED_OPERATORS.put("LIKE", ComparisonOperators.LIKE);
	}

	/**
	 * Constructor.
	 *
	 * @param text
	 *            text of a query that is to be parsed
	 */
	public QueryParser(String text) {
		if (text == null) {
			throw new ParserException("Argument null is not legal.");
		}

		lexer = new QueryLexer(text);
		query = new ArrayList<>();
		tokenBuffer = new ArrayList<>(TOKEN_BUFFER_SIZE);

		parse();
	}

	/**
	 * Returns the next token from the lexer. Throws ParserException if lexer
	 * throws LexerException.
	 *
	 * @return the next token
	 */
	private Token nextToken() {
		try {
			return lexer.nextToken();
		} catch (LexerException ex) {
			throw new ParserException(ex.getMessage(), ex);
		}
	}

	/**
	 * Checks if the parsed query is a direct query.
	 *
	 * @return true if the parsed query is a direct query
	 */
	public boolean isDirectQuery() {
		if (query.size() != 1) {
			return false;
		}

		ConditionalExpression expr = query.get(0);
		if (expr.getComparisonOperator() != ComparisonOperators.EQUALS) {
			return false;
		}
		if (expr.getFieldGetter() != FieldValueGetters.JMBAG) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the Jmbag if the parsed query is direct. Otherwise, throws an
	 * IllegalStateException.
	 *
	 * @return the Jmbag of the parsed query which should be a direct query
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("The parsed query is not a direct query.");
		}

		return query.get(0).getStringLiteral();
	}

	/**
	 * Gets the list of conditional expressions creted as a result of parsing a
	 * query.
	 *
	 * @return list of conditional expression
	 */
	public List<ConditionalExpression> getQuery() {
		return new ArrayList<>(query);
	}

	/**
	 * Parses a query. If the query is not valid, throws a ParserException
	 * exception.
	 */
	private void parse() {
		while (true) {
			refillTokenBuffer();
			validateTokenTypesInBuffer();
			addNewConditionalExpressionFromBuffer();

			if (nextToken().getType() == TokenType.EOF) {
				break;
			} else if (lexer.getToken().getType() != TokenType.LOGICAL_OPERATOR) {
				throw new ParserException(
						"There has to be a logical operator AND between every pair of conditional expressions.");
			}

		}
	}

	/**
	 * Generates a new conditional expression using the data from the token
	 * buffer and adds it to the list of conditional expressions.
	 */
	private void addNewConditionalExpressionFromBuffer() {
		IFieldValueGetter valueGetter = MAP_OF_SUPPORTED_ATTRIBUTES.get(tokenBuffer.get(0).getValue());
		IComparisonOperator comparOperator = MAP_OF_SUPPORTED_OPERATORS.get(tokenBuffer.get(1).getValue());
		String string = (String) tokenBuffer.get(2).getValue();

		ConditionalExpression expr;
		try {
			expr = new ConditionalExpression(valueGetter, string, comparOperator);
		} catch (IllegalArgumentException ex) {
			throw new ParserException(ex.getMessage(), ex);
		}
		query.add(expr);
	}

	/**
	 * Checks if token types in token buffer are valid. If they are not valid,
	 * it throws a ParserException exception. The first token in the buffer has
	 * to be the NAME token, the second has to be the OPERATOR token and the
	 * third has to be the STRING token.
	 */
	private void validateTokenTypesInBuffer() {
		String excMessage = "Invalid query. A regular query consists of \"attributeName-operator-string\", in that order.";

		if (tokenBuffer.get(0).getType() == TokenType.NAME) {
			Token token = tokenBuffer.get(0);
			if (!MAP_OF_SUPPORTED_ATTRIBUTES.containsKey(token.getValue())) {
				throw new ParserException("There is no an attribute with the name '" + token.getValue()
						+ "', or it is not allowed to be a part of query.");
			}
		} else {
			throw new ParserException(excMessage);
		}

		if (tokenBuffer.get(1).getType() != TokenType.OPERATOR) {
			throw new ParserException(excMessage);
		}
		if (tokenBuffer.get(2).getType() != TokenType.STRING) {
			throw new ParserException(excMessage);
		}

	}

	/**
	 * Refills the token buffer with three tokens. If there are no enough tokens
	 * (excluding the EOF token), that means that the query is incomplete so a
	 * ParserException exception is thrown.
	 */
	private void refillTokenBuffer() {
		tokenBuffer.clear();
		for (int i = 0; i < TOKEN_BUFFER_SIZE; i++) {
			Token token = nextToken();
			if (token.getType() == TokenType.EOF) {
				throw new ParserException("Incomplete query!");
			}

			tokenBuffer.add(token);
		}
	}
}
