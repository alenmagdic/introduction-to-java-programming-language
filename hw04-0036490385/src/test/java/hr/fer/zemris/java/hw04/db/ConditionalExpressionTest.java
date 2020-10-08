package hr.fer.zemris.java.hw04.db;

import org.junit.Assert;
import org.junit.Test;

public class ConditionalExpressionTest {

	@Test
	public void testExpressionWithLastNameAndLikeOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Br*",
				ComparisonOperators.LIKE);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithLastNameAndLikeOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "P*",
				ComparisonOperators.LIKE);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithFirstNameAndLikeOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Jus*s",
				ComparisonOperators.LIKE);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithFirstNameAndLikeOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "P*",
				ComparisonOperators.LIKE);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithJmbagAndLikeOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "*5", ComparisonOperators.LIKE);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithJmbagAndLikeOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "*3", ComparisonOperators.LIKE);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithLastNameAndGreaterOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bakamović",
				ComparisonOperators.GREATER);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithLastNameAndGreaterOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Glumac",
				ComparisonOperators.GREATER);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithFirstNameAndGreaterOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Alen",
				ComparisonOperators.GREATER);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithFirstNameAndGreaterOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Pero",
				ComparisonOperators.GREATER);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithJmbagAndGreaterOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000004",
				ComparisonOperators.GREATER);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithJmbagAndGreaterOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000006",
				ComparisonOperators.GREATER);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithLastNameAndEqualsOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Brezović",
				ComparisonOperators.EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithLastNameAndEqualsOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Glumac",
				ComparisonOperators.EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithFirstNameAndEqualsOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Jusufadis",
				ComparisonOperators.EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithFirstNameAndEqualsOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Pero",
				ComparisonOperators.EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithJmbagAndEqualsOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000005",
				ComparisonOperators.EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithJmbagAndEqualsOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000004",
				ComparisonOperators.EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithLastNameAndNotEqualsOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Glumac",
				ComparisonOperators.NOT_EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithLastNameAndNotEqualsOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Brezović",
				ComparisonOperators.NOT_EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithFirstNameAndNotEqualsOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Ivan",
				ComparisonOperators.NOT_EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithFirstNameAndNotEqualsOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Jusufadis",
				ComparisonOperators.NOT_EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

	@Test
	public void testExpressionWithJmbagAndNotEqualsOperatorForTrueCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000004",
				ComparisonOperators.NOT_EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(true, result);
	}

	@Test
	public void testExpressionWithJmbagAndNotEqualsOperatorForFalseCondition() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000005",
				ComparisonOperators.NOT_EQUALS);
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		boolean result = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		Assert.assertEquals(false, result);
	}

}
