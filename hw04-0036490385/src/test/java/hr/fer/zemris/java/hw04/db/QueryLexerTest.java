package hr.fer.zemris.java.hw04.db;

import org.junit.Assert;
import org.junit.Test;

public class QueryLexerTest {

	@Test
	public void emptyText() {
		QueryLexer lex = new QueryLexer("");
		Assert.assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test(expected = LexerException.class)
	public void toManyCallsForNextToken() {
		QueryLexer lex = new QueryLexer("jmbag");
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullAsText() {
		new QueryLexer(null);
	}

	@Test
	public void simpleString() {
		QueryLexer lex = new QueryLexer("\"rijec\"");
		Assert.assertEquals(TokenType.STRING, lex.nextToken().getType());
		Assert.assertEquals("rijec", lex.getToken().getValue());
	}

	@Test
	public void stringWithVariousSymbols() {
		QueryLexer lex = new QueryLexer("\"nickname: john123?*{\"");
		Assert.assertEquals(TokenType.STRING, lex.nextToken().getType());
		Assert.assertEquals("nickname: john123?*{", lex.getToken().getValue());
	}

	@Test
	public void simpleName() {
		QueryLexer lex = new QueryLexer("varijabla");
		Assert.assertEquals(TokenType.NAME, lex.nextToken().getType());
		Assert.assertEquals("varijabla", lex.getToken().getValue());
	}

	@Test
	public void simpleLogicalOperator() {
		QueryLexer lex = new QueryLexer("AnD");
		Assert.assertEquals(TokenType.LOGICAL_OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("AnD", lex.getToken().getValue());
	}

	@Test
	public void simpleOperatorGreaterOrEqual() {
		QueryLexer lex = new QueryLexer(">=");
		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals(">=", lex.getToken().getValue());
	}

	@Test
	public void simpleOperatorNotEqual() {
		QueryLexer lex = new QueryLexer("!=");
		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("!=", lex.getToken().getValue());
	}

	@Test
	public void simpleOperatorEqual() {
		QueryLexer lex = new QueryLexer("=");
		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("=", lex.getToken().getValue());
	}

	@Test
	public void allOperators() {
		QueryLexer lex = new QueryLexer("= != < > <= >= AND");
		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("=", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("!=", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("<", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals(">", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("<=", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals(">=", lex.getToken().getValue());

		Assert.assertEquals(TokenType.LOGICAL_OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("AND", lex.getToken().getValue());
	}

	@Test
	public void simpleOperatorLike() {
		QueryLexer lex = new QueryLexer("LIKE");
		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("LIKE", lex.getToken().getValue());
	}

	@Test
	public void somethingLikeOperatorLikeButWithWrongCase() {
		QueryLexer lex = new QueryLexer("like");
		Assert.assertEquals(TokenType.NAME, lex.nextToken().getType());
		Assert.assertEquals("like", lex.getToken().getValue());
	}

	@Test
	public void simpleExpressionWithoutSpaces() {
		QueryLexer lex = new QueryLexer("firstName>\"A\"");
		Assert.assertEquals(TokenType.NAME, lex.nextToken().getType());
		Assert.assertEquals("firstName", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals(">", lex.getToken().getValue());

		Assert.assertEquals(TokenType.STRING, lex.nextToken().getType());
		Assert.assertEquals("A", lex.getToken().getValue());

		Assert.assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void simpleExpressionWithSpacesAndTabs() {
		QueryLexer lex = new QueryLexer("  \t  firstName \t  >=\t  \t\"A\"");
		Assert.assertEquals(TokenType.NAME, lex.nextToken().getType());
		Assert.assertEquals("firstName", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals(">=", lex.getToken().getValue());

		Assert.assertEquals(TokenType.STRING, lex.nextToken().getType());
		Assert.assertEquals("A", lex.getToken().getValue());

		Assert.assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void complexExpressionWithoutSpaces() {
		QueryLexer lex = new QueryLexer("firstName>\"A\"and lastName LIKE\"B*ć\"");
		testComplexExpression(lex);
	}

	private void testComplexExpression(QueryLexer lex) {
		Assert.assertEquals(TokenType.NAME, lex.nextToken().getType());
		Assert.assertEquals("firstName", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals(">", lex.getToken().getValue());

		Assert.assertEquals(TokenType.STRING, lex.nextToken().getType());
		Assert.assertEquals("A", lex.getToken().getValue());

		Assert.assertEquals(TokenType.LOGICAL_OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("and", lex.getToken().getValue());

		Assert.assertEquals(TokenType.NAME, lex.nextToken().getType());
		Assert.assertEquals("lastName", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("LIKE", lex.getToken().getValue());

		Assert.assertEquals(TokenType.STRING, lex.nextToken().getType());
		Assert.assertEquals("B*ć", lex.getToken().getValue());

		Assert.assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void complexExpressionWithSpacesAndTabs() {
		QueryLexer lex = new QueryLexer("firstName  \t  > \"A\"  \tand \t lastName\t LIKE  \"B*ć\" \t \t");
		testComplexExpression(lex);
	}

	@Test
	public void indexQuery() {
		QueryLexer lex = new QueryLexer("jmbag=\"0000000003\"");
		Assert.assertEquals(TokenType.NAME, lex.nextToken().getType());
		Assert.assertEquals("jmbag", lex.getToken().getValue());

		Assert.assertEquals(TokenType.OPERATOR, lex.nextToken().getType());
		Assert.assertEquals("=", lex.getToken().getValue());

		Assert.assertEquals(TokenType.STRING, lex.nextToken().getType());
		Assert.assertEquals("0000000003", lex.getToken().getValue());
	}

	@Test(expected = LexerException.class)
	public void unsupportedSymbols() {
		QueryLexer lex = new QueryLexer("jmbag?* > \"1234567890\"");
		lex.nextToken();
		lex.nextToken();
	}

	@Test(expected = LexerException.class)
	public void invalidName() {
		QueryLexer lex = new QueryLexer("5urname");
		lex.nextToken();
	}

	@Test(expected = LexerException.class)
	public void stringWithoutEnding() {
		QueryLexer lex = new QueryLexer("name > \"Pero   \t\t    ");
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
	}
}
