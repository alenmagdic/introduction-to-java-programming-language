package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QueryFilterTest {

	@Test
	public void singleConditionTrue() {
		List<ConditionalExpression> conditions = new ArrayList<>();
		conditions.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER));

		QueryFilter filter = new QueryFilter(conditions);

		StudentRecord rec = new StudentRecord("0000000002", "Bakamović", "Petra", 3);

		Assert.assertEquals(true, filter.accepts(rec));
	}

	@Test
	public void singleConditionFalse() {
		List<ConditionalExpression> conditions = new ArrayList<>();
		conditions.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "C", ComparisonOperators.GREATER));

		QueryFilter filter = new QueryFilter(conditions);

		StudentRecord rec = new StudentRecord("0000000002", "Bakamović", "Petra", 3);

		Assert.assertEquals(false, filter.accepts(rec));
	}

	@Test
	public void multipleConditionsTrue() {
		List<ConditionalExpression> conditions = new ArrayList<>();
		conditions.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "C", ComparisonOperators.LESS));
		conditions.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Petra", ComparisonOperators.EQUALS));
		conditions.add(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.GREATER));

		QueryFilter filter = new QueryFilter(conditions);

		StudentRecord rec = new StudentRecord("0000000002", "Bakamović", "Petra", 3);

		Assert.assertEquals(true, filter.accepts(rec));
	}

	@Test
	public void multipleConditionsFalse() {
		List<ConditionalExpression> conditions = new ArrayList<>();
		conditions.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "C", ComparisonOperators.LESS));
		conditions
				.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Petra", ComparisonOperators.NOT_EQUALS));
		conditions.add(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.GREATER));

		QueryFilter filter = new QueryFilter(conditions);

		StudentRecord rec = new StudentRecord("0000000002", "Bakamović", "Petra", 3);

		Assert.assertEquals(false, filter.accepts(rec));
	}
}
