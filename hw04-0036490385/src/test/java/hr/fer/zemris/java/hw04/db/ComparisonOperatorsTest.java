package hr.fer.zemris.java.hw04.db;

import org.junit.Assert;
import org.junit.Test;

public class ComparisonOperatorsTest {

	@Test
	public void patternWithAsteriskAtTheBeginning() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(true, oper.satisfied("Hrvatska", "*ska"));
		Assert.assertEquals(false, oper.satisfied("Los Angeles", "*ska"));
	}

	@Test
	public void patternWithAsteriskInTheMiddle() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(true, oper.satisfied("Hrvatska", "Hr*ska"));
		Assert.assertEquals(false, oper.satisfied("Hrvatska", "Alj*ska"));
	}

	@Test
	public void patternWithAsteriskAtTheEnding() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(true, oper.satisfied("Hrvatska", "Hr*"));
		Assert.assertEquals(false, oper.satisfied("Hrvatska", "Am*"));
	}

	@Test
	public void patternWithEndingThatStartsLikeTheBeginning() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(false, oper.satisfied("Hrvatska", "Hr*Hrvatska"));
	}

	@Test
	public void patternWithBeginningThatStartsLikeTheEnding() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(false, oper.satisfied("Hrvatska", "Hrvatska*ska"));
	}

	@Test
	public void patternWithoutAnAsterisk() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(true, oper.satisfied("Hrvatska", "Hrvatska"));
	}

	@Test
	public void likeOperatorWithEmptyStringAndNonEmptyPattern() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(false, oper.satisfied("", "*ska"));
		Assert.assertEquals(false, oper.satisfied("", "Hrv*ska"));
		Assert.assertEquals(false, oper.satisfied("", "Hrv*"));
	}

	@Test
	public void likeOperatorWithNonEmptyStringAndEmptyPattern() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(false, oper.satisfied("Madrid", ""));
	}

	@Test
	public void likeOperatorWithEmptyStringAndEmptyPattern() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(true, oper.satisfied("", ""));
	}

	@Test
	public void patternThatConsistOnlyOfAnAsterisk() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		Assert.assertEquals(true, oper.satisfied("San Francisco", "*"));
	}

	@Test
	public void operatorLessWithLowerValue() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		Assert.assertEquals(true, oper.satisfied("Angola", "Hrvatska"));
	}

	@Test
	public void operatorLessWithEqualValue() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		Assert.assertEquals(false, oper.satisfied("Angola", "Angola"));
	}

	@Test
	public void operatorLessWithHigherValue() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		Assert.assertEquals(false, oper.satisfied("Hrvatska", "Angola"));
	}

	@Test
	public void operatorLessOrEqualsWithLowerValue() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		Assert.assertEquals(true, oper.satisfied("Angola", "Hrvatska"));
	}

	@Test
	public void operatorLessOrEqualsWithEqualValue() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		Assert.assertEquals(true, oper.satisfied("Angola", "Angola"));
	}

	@Test
	public void operatorLessOrEqualsWithHigherValue() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		Assert.assertEquals(false, oper.satisfied("Hrvatska", "Angola"));
	}

	@Test
	public void operatorGreaterWithLowerValue() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		Assert.assertEquals(false, oper.satisfied("Angola", "Hrvatska"));
	}

	@Test
	public void operatorGreaterWithEqualValue() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		Assert.assertEquals(false, oper.satisfied("Angola", "Angola"));
	}

	@Test
	public void operatorGreaterWithHigherValue() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		Assert.assertEquals(true, oper.satisfied("Hrvatska", "Angola"));
	}

	@Test
	public void operatorGreaterOrEqualsWithLowerValue() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		Assert.assertEquals(false, oper.satisfied("Angola", "Hrvatska"));
	}

	@Test
	public void operatorGreaterOrEqualsWithEqualValue() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		Assert.assertEquals(true, oper.satisfied("Angola", "Angola"));
	}

	@Test
	public void operatorGreaterOrEqualsWithHigherValue() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		Assert.assertEquals(true, oper.satisfied("Hrvatska", "Angola"));
	}

	@Test
	public void operatorEqualsWithEqualValue() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		Assert.assertEquals(true, oper.satisfied("Angola", "Angola"));
	}

	@Test
	public void operatorEqualsWithUnequalValue() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		Assert.assertEquals(false, oper.satisfied("Hrvatska", "Angola"));
	}

	@Test
	public void operatorNotEqualsWithEqualValue() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		Assert.assertEquals(false, oper.satisfied("Angola", "Angola"));
	}

	@Test
	public void operatorNotEqualsWithUnequalValue() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		Assert.assertEquals(true, oper.satisfied("Hrvatska", "Angola"));
	}

}
