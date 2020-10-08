package hr.fer.zemris.java.hw04.db;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

	@Test
	public void directQuery() {
		QueryParser parser = new QueryParser("jmbag = \"0036490385\"");

		Assert.assertEquals(true, parser.isDirectQuery());
		Assert.assertEquals("0036490385", parser.getQueriedJMBAG());
	}

	@Test
	public void testDirectQueryStructure() {
		QueryParser parser = new QueryParser("jmbag = \"0036490385\"");

		ConditionalExpression expr = parser.getQuery().get(0);
		assertConditonalExpressionsAreEqual(
				new ConditionalExpression(FieldValueGetters.JMBAG, "0036490385", ComparisonOperators.EQUALS), expr);
	}

	@Test
	public void indirectQuery() {
		QueryParser parser = new QueryParser("firstName = \"John\"");

		Assert.assertEquals(false, parser.isDirectQuery());
	}

	@Test
	public void indirectComplexQuery() {
		QueryParser parser = new QueryParser("jmbag = \"12345\" AND firstName = \"John\"");

		Assert.assertEquals(false, parser.isDirectQuery());
	}

	@Test(expected = IllegalStateException.class)
	public void gettingQueriedJmbagFromIndirectQuery() {
		QueryParser parser = new QueryParser("firstName = \"John\"");
		parser.getQueriedJMBAG();
	}

	@Test
	public void complexQuery() {
		QueryParser parser = new QueryParser(
				" firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");
		List<ConditionalExpression> query = parser.getQuery();
		Assert.assertEquals(4, query.size());

		ConditionalExpression expr = query.get(0);
		assertConditonalExpressionsAreEqual(
				new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER), expr);

		expr = query.get(1);
		assertConditonalExpressionsAreEqual(
				new ConditionalExpression(FieldValueGetters.FIRST_NAME, "C", ComparisonOperators.LESS), expr);

		expr = query.get(2);
		assertConditonalExpressionsAreEqual(
				new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE), expr);

		expr = query.get(3);
		assertConditonalExpressionsAreEqual(
				new ConditionalExpression(FieldValueGetters.JMBAG, "0000000002", ComparisonOperators.GREATER), expr);
	}

	private void assertConditonalExpressionsAreEqual(ConditionalExpression expected, ConditionalExpression actual) {
		Assert.assertEquals(expected.getComparisonOperator(), actual.getComparisonOperator());
		Assert.assertEquals(expected.getFieldGetter(), actual.getFieldGetter());
		Assert.assertEquals(expected.getStringLiteral(), actual.getStringLiteral());
	}

	@Test(expected = ParserException.class)
	public void uncompleteQuery() {
		new QueryParser("jmbag =");
	}

	@Test(expected = ParserException.class)
	public void uncompleteComplexQuery() {
		new QueryParser("jmbag = \"34567\" and");
	}

	@Test(expected = ParserException.class)
	public void unsupportedLogicalOperator() {
		new QueryParser("jmbag = \"34567\" or firstName = \"George\"");
	}

	@Test(expected = ParserException.class)
	public void nonsenseQuery() {
		new QueryParser("jmbag firstName");
	}

	@Test(expected = ParserException.class)
	public void incompleteStringQuery() {
		new QueryParser("jmbag = \"1234567890");
	}

	@Test(expected = ParserException.class)
	public void invalidQuery() {
		new QueryParser("query jmbag = \"1234567890\"");
	}

	@Test(expected = ParserException.class)
	public void unexistingAttributeNameQuery() {
		new QueryParser("Name = \"John\"");
	}

	@Test(expected = ParserException.class)
	public void invalidPattern() {
		new QueryParser("firstName LIKE \"I*v*\"");
	}

	@Test(expected = ParserException.class)
	public void invalidOrderQuery() {
		new QueryParser("\"John\" = firstName");
	}

	@Test(expected = ParserException.class)
	public void attributesAtBothSidesQuery() {
		new QueryParser("lastName = firstName");
	}

	@Test(expected = ParserException.class)
	public void unsupportedTypeOfQuotationMarks() {
		new QueryParser("lastName = 'Madrid'");
	}

}
