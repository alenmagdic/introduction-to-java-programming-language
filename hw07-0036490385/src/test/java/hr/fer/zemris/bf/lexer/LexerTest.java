package hr.fer.zemris.bf.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LexerTest {

	@Test
	public void testNotNull() {
		Lexer lexer = new Lexer("");
		assertNotNull(lexer.nextToken());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullInput() {
		new Lexer(null);
	}

	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test(expected = LexerException.class)
	public void testRadAfterEOF() {
		Lexer lexer = new Lexer("");
		lexer.nextToken();
		lexer.nextToken();
	}

	@Test
	public void testNoActualContent() {
		Lexer lexer = new Lexer("   \r\n\t    ");
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testSingleIntConstant() {
		Lexer lexer = new Lexer(" 0 ");

		Token token = lexer.nextToken();
		assertEquals(TokenType.CONSTANT, token.getTokenType());
		assertEquals(false, token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testSingleNameConstant() {
		Lexer lexer = new Lexer(" tRue ");
		Token token = lexer.nextToken();
		assertEquals(TokenType.CONSTANT, token.getTokenType());
		assertEquals(true, token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testOperatorNot() {
		Lexer lexer = new Lexer(" nOt a ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("not", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testOperatorAnd() {
		Lexer lexer = new Lexer(" A aNd b ");

		Token token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("and", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testOperatorOr() {
		Lexer lexer = new Lexer(" a OR b ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testOperatorXor() {
		Lexer lexer = new Lexer(" a xOR b ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("xor", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testOperatorAndAsSymbol() {
		Lexer lexer = new Lexer(" A and b * c ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("and", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("and", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("C", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testOperatorXorAsSymbol() {
		Lexer lexer = new Lexer(" A xor b :+: c ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("xor", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("xor", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("C", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testOperatorOrAsSymbol() {
		Lexer lexer = new Lexer(" A or b + c ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("C", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testCombinationOfOperators() {
		Lexer lexer = new Lexer(" a or b xor c and d ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("xor", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("C", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("and", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("D", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testCombinationOfOperators2() {
		Lexer lexer = new Lexer(" a or b xor c or d ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("xor", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("C", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("D", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testExpressionWithBrackets() {
		Lexer lexer = new Lexer(" (a + b) xor (c or d) ");
		Token token = lexer.nextToken();

		assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.CLOSED_BRACKET, token.getTokenType());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("xor", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("C", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("D", token.getTokenValue());

		assertEquals(TokenType.CLOSED_BRACKET, lexer.nextToken().getTokenType());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test
	public void testBrackets() {
		Lexer lexer = new Lexer(" a and (b or ");
		Token token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("A", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("and", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());

		token = lexer.nextToken();
		assertEquals(TokenType.VARIABLE, token.getTokenType());
		assertEquals("B", token.getTokenValue());

		token = lexer.nextToken();
		assertEquals(TokenType.OPERATOR, token.getTokenType());
		assertEquals("or", token.getTokenValue());

		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

	@Test(expected = LexerException.class)
	public void testExpressionWithInvalidConstant() {
		Lexer lexer = new Lexer(" a and 10");
		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();
	}
}
