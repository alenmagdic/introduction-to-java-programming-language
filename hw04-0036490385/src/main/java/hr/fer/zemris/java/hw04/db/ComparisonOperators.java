package hr.fer.zemris.java.hw04.db;

/**
 * This class contains some public static comparison operators. Precisely, there
 * are seven operators: LESS, LESS_OR_EQUALS, GREATER,GREATER_OR_EQUALS,EQUALS,
 * NOT_EQUALS and LIKE.
 *
 * @author Alen Magdić
 *
 */
public class ComparisonOperators {
	/** A comparison operator that represents the '<' operator */
	public static final IComparisonOperator LESS;
	/** A comparison operator that represents the '<=' operator */
	public static final IComparisonOperator LESS_OR_EQUALS;
	/** A comparison operator that represents the '>' operator */
	public static final IComparisonOperator GREATER;
	/** A comparison operator that represents the '>=' operator */
	public static final IComparisonOperator GREATER_OR_EQUALS;
	/** A comparison operator that represents the '=' operator */
	public static final IComparisonOperator EQUALS;
	/** A comparison operator that represents the '!=' operator */
	public static final IComparisonOperator NOT_EQUALS;
	/**
	 * A comparison operator that represents the 'LIKE' operator.
	 */
	public static final IComparisonOperator LIKE;

	static {
		LESS = (value1, value2) -> value1.compareTo(value2) < 0;
		LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
		GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
		GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
		EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;
		NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0;
		LIKE = new LikeOperator();
	}
}
