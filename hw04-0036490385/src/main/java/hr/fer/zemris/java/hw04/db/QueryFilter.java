package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an implemtation of IFilter. It contains a list of conditional
 * expressions. While checking if a record is acceptable, it checks if it
 * satisfies all the conditions from the list of conditional expressions. If any
 * of the conditions is not satisfied, the record is not acceptable.
 *
 * @author Alen Magdić
 *
 */
public class QueryFilter implements IFilter {
	/**
	 * List of conditions that are to be satisfied in order for a record to be
	 * acceptable.
	 */
	private List<ConditionalExpression> conditions;

	/**
	 * Constructor.
	 *
	 * @param conditions
	 *            a list of conditional expressions that are to be satisfied
	 */
	public QueryFilter(List<ConditionalExpression> conditions) {
		this.conditions = new ArrayList<>(conditions);
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression condition : conditions) {
			IComparisonOperator operator = condition.getComparisonOperator();
			String fieldValue = condition.getFieldGetter().get(record);
			String stringLiteral = condition.getStringLiteral();

			if (!operator.satisfied(fieldValue, stringLiteral)) {
				return false;
			}
		}
		return true;
	}

}
