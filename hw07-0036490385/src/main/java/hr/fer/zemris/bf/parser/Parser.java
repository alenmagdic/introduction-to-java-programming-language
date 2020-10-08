package hr.fer.zemris.bf.parser;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * This parser parses logical expressions. If the given expression is not valid,
 * the parsers will throw a {@link ParserException}. The parser generates a tree
 * that can be visited with an implementation of a {@link NodeVisitor}.
 *
 * @author Alen Magdić
 *
 */
public class Parser {
	/** Expression that is to be parsed **/
	private Node expression;
	/** Lexer used for parsing **/
	private Lexer lexer;
	/** Current token **/
	private Token currentToken;

	/**
	 * Constructor.
	 *
	 * @param expression
	 *            an expression that is to be parsed
	 */
	public Parser(String expression) {
		if (expression == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		lexer = new Lexer(expression);
		parse();
	}

	/**
	 * Parses the expression specified in the constructor.
	 */
	private void parse() {
		currentToken = lexer.nextToken();

		try {
			expression = parseE1();
		} catch (LexerException ex) {
			throw new ParserException("Lexer has thrown exception: " + ex.getMessage());
		}

		try {
			if (lexer.nextToken().getTokenType() != TokenType.EOF) {
				throw new ParserException("Unexpected token: " + currentToken);
			}
		} catch (LexerException ex) {
			// if this happens, it means that the parser already spent reached
			// the EOF, so it's mean that the parsed expression is valid
		}
	}

	/**
	 * Parses a nonterminal symbol E1. There is only one production rule for
	 * this symbol: E1 -> E2 (OR E2)*
	 *
	 * @return a node created by parsing the expression
	 */
	private Node parseE1() {
		List<Node> children = new ArrayList<>();
		children.add(parseE2());

		while (true) {
			if (currentToken.getTokenType() == TokenType.OPERATOR && currentToken.getTokenValue().equals("or")) {
				currentToken = lexer.nextToken();
				children.add(parseE2());
			} else {
				break;
			}
		}

		if (children.size() == 1) {
			return children.get(0);
		}
		return new BinaryOperatorNode("or", children, (op1, op2) -> op1 || op2);
	}

	/**
	 * Parses a nonterminal symbol E2. There is only one production rule for
	 * this symbol: E2 -> E3 (XOR E3)*
	 *
	 * @return a node created by parsing the expression
	 */
	private Node parseE2() {
		List<Node> children = new ArrayList<>();
		children.add(parseE3());

		while (true) {
			if (currentToken.getTokenType() == TokenType.OPERATOR && currentToken.getTokenValue().equals("xor")) {
				currentToken = lexer.nextToken();
				children.add(parseE3());
			} else {
				break;
			}
		}

		if (children.size() == 1) {
			return children.get(0);
		}
		return new BinaryOperatorNode("xor", children, (op1, op2) -> op1 && !op2 || !op1 && op2);

	}

	/**
	 * Parses a nonterminal symbol E3. There is only one production rule for
	 * this symbol: E3 -> E4 (AND E4)*
	 *
	 *
	 * @return a node created by parsing the expression
	 */
	private Node parseE3() {
		List<Node> children = new ArrayList<>();
		children.add(parseE4());

		while (true) {
			if (currentToken.getTokenType() == TokenType.OPERATOR && currentToken.getTokenValue().equals("and")) {
				currentToken = lexer.nextToken();
				children.add(parseE4());
			} else {
				break;
			}
		}

		if (children.size() == 1) {
			return children.get(0);
		}
		return new BinaryOperatorNode("and", children, (op1, op2) -> op1 && op2);
	}

	/**
	 * Parses a nonterminal symbol E4. There is only one production rule for
	 * this symbol: E4 -> NOT E4 | E5
	 *
	 * @return a node created by parsing the expression
	 */
	private Node parseE4() {
		if (currentToken.getTokenType() == TokenType.OPERATOR && currentToken.getTokenValue().equals("not")) {
			currentToken = lexer.nextToken();
			return new UnaryOperatorNode("not", parseE4(), (op) -> !op);
		}
		return parseE5();
	}

	/**
	 * Parses a nonterminal symbol E1. There is only one production rule for
	 * this symbol: E5 -> VARIABLE | CONSTANT | '(' E1 ')'
	 *
	 * @return a node created by parsing the expression
	 */
	private Node parseE5() {
		if (currentToken.getTokenType() == TokenType.VARIABLE) {
			String varName = (String) currentToken.getTokenValue();
			currentToken = lexer.nextToken();
			return new VariableNode(varName);
		} else if (currentToken.getTokenType() == TokenType.CONSTANT) {
			Boolean constValue = (Boolean) currentToken.getTokenValue();
			currentToken = lexer.nextToken();
			return new ConstantNode(constValue);
		} else if (currentToken.getTokenType() == TokenType.OPEN_BRACKET) {
			currentToken = lexer.nextToken();
			Node node = parseE1();

			if (currentToken.getTokenType() != TokenType.CLOSED_BRACKET) {
				throw new ParserException("Expected ')' but found " + currentToken.getTokenType());
			}
			currentToken = lexer.nextToken();
			return node;
		} else {
			throw new ParserException("Unexpected token found: {" + currentToken + "}");
		}
	}

	/**
	 * Gets the generated tree.
	 *
	 * @return generated tree
	 */
	public Node getExpression() {
		return expression;
	}
}
