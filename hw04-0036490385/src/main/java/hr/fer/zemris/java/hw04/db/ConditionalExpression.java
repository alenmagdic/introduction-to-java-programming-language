package hr.fer.zemris.java.hw04.db;

/**
 * This class represents a conditional expression. A conditional expression
 * consists of two operands and a IComparisonOperator. The first operand is not
 * specifed directly but through a IFieldValueGetter which extracts a value of a
 * specified attribute (specified by the implementation of IFieldValueGetter) of
 * a StudentRecord. The second operand is specified directly.
 *
 *
 * @author Alen Magdić
 *
 */
public class ConditionalExpression {
	/**
	 * The first operand, specified indirectly through an implementation of
	 * IFieldValueGetter.
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * The second operand.
	 */
	private String stringLiteral;
	/**
	 * The operator of comparison.
	 */
	private IComparisonOperator comparisonOperator;

	/**
	 * Constructor.
	 *
	 * @param fieldGetter
	 *            getter for the first operand
	 * @param stringLiteral
	 *            the second operand
	 * @param comparisonOperator
	 *            operator of comparison
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		super();
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;

		if (comparisonOperator == ComparisonOperators.LIKE) {
			validatePattern();
		}
	}

	/**
	 * Checks if the specified pattern is valid. If it is not, throws an
	 * IllegalArgumentException exception.
	 *
	 */
	private void validatePattern() {
		char[] patternArray = stringLiteral.toCharArray();
		int wildcards = 0;

		for (char c : patternArray) {
			if (c == '*') {
				wildcards++;
			}
		}

		if (wildcards > 1) {
			throw new IllegalArgumentException("A pattern for operator LIKE can not contain more than one wildcard.");
		}

	}

	/**
	 * Gets the field getter.
	 *
	 * @return the field getter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Gets the second operand of expression.
	 *
	 * @return the second operand of expression
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Gets the comparison operator.
	 *
	 * @return comparison operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

}
