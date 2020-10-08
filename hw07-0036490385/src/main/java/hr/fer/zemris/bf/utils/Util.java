package hr.fer.zemris.bf.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import hr.fer.zemris.bf.model.Node;

/**
 * A class that contains various methods for working with parsed logical
 * expressions.
 *
 * @author Alen Magdić
 *
 */
public class Util {

	/** Private constructor **/
	private Util() {
	}

	/**
	 * Calls the specified consumer for every combination of values of the
	 * specified variables.
	 *
	 * @param variables
	 *            list of variable names
	 * @param consumer
	 *            consumer that is to be called for every combination of values
	 *            of the specified variables
	 */
	public static void forEach(List<String> variables, Consumer<boolean[]> consumer) {
		boolean[] combination = new boolean[variables.size()];
		int numOfCombinations = (int) Math.pow(2, combination.length);

		for (int i = 0; i < numOfCombinations; i++) {
			for (int j = combination.length - 1, x = i; j >= 0; j--) {
				combination[j] = x % 2 == 1 ? true : false;
				x /= 2;
			}
			consumer.accept(combination);
		}

	}

	/**
	 * Generates the truth table for the specified expression and returns a set
	 * of value combinations that satisfy the following condition:
	 * expression(combination) == expressionValue. In general: function(values)
	 * == wantedValue.
	 *
	 * @param variables
	 *            list of variable names
	 * @param expression
	 *            tree of an expression
	 * @param expressionValue
	 *            an expression value, determines which combinations are to be
	 *            included in the set that is to be returned
	 * @return set of value combinations satisfying the following condition:
	 *         expression(combination) == expressionValue
	 */
	public static Set<boolean[]> filterAssignments(List<String> variables, Node expression, boolean expressionValue) {
		Set<boolean[]> acceptableComb = new TreeSet<>((comb1, comb2) -> {
			for (int i = 0; i < comb1.length; i++) {
				if (comb1[i] != comb2[i]) {
					return comb1[i] == true ? 1 : -1;
				}
			}
			return 0;
		});

		ExpressionEvaluator eval = new ExpressionEvaluator(variables);

		Util.forEach(variables, values -> {
			eval.setValues(values);
			expression.accept(eval);
			if (eval.getResult() == expressionValue) {
				acceptableComb.add(Arrays.copyOf(values, values.length));
			}
		});

		return acceptableComb;
	}

	/**
	 * Generates an integer that is the index of the specified combination of
	 * values from the logical truth table. For example, if there are the
	 * following values: false,false,true,false, then the result is number 2.
	 *
	 * @param values
	 *            boolean values
	 * @return index of the specified combination of boolean values in the
	 *         logical truth table
	 */
	public static int booleanArrayToInt(boolean[] values) {
		int index = 0;
		for (int i = values.length - 1, x = 1; i >= 0; i--, x *= 2) {
			if (values[i]) {
				index += x;
			}
		}
		return index;
	}

	/**
	 * Returns a set of minterms for the specified expression.
	 *
	 * @param variables
	 *            list of variable names
	 * @param expression
	 *            an expression
	 * @return set of minterms for the specified expression
	 */
	public static Set<Integer> toSumOfMinterms(List<String> variables, Node expression) {
		return getSetOfIntValues(variables, expression, true);
	}

	/**
	 * Returns a set of maxterms for the specified expression.
	 *
	 * @param variables
	 *            list of variable names
	 * @param expression
	 *            an expression
	 * @return set of maxterms for the specified expression
	 */
	public static Set<Integer> toProductOfMaxterms(List<String> variables, Node expression) {
		return getSetOfIntValues(variables, expression, false);
	}

	/**
	 * Returns a set of indices of combinations from the truth table that
	 * satisfy (for the specified expression) the following condition:
	 * expression(combination) == acceptableResult. In general: function(values)
	 * == wantedValue.
	 *
	 * @param variables
	 *            list of variable names
	 * @param expression
	 *            an expression
	 * @param acceptableResult
	 *            acceptable result
	 * @return set of integer values that are the indices of combinations from
	 *         the truth table that satisfy the following condition:
	 *         expression(combination) == acceptableResult
	 */
	private static Set<Integer> getSetOfIntValues(List<String> variables, Node expression, boolean acceptableResult) {
		Set<Integer> intValues = new TreeSet<>();
		for (boolean[] values : Util.filterAssignments(variables, expression, acceptableResult)) {
			intValues.add(booleanArrayToInt(values));
		}
		return intValues;
	}

	/**
	 * Creates an array of bytes where every byte represents a bit of the
	 * specified integer. For example, if the specified number is 4 and the
	 * specified length 5 the returned array should be like the following:
	 * {0,0,1,0,0}.
	 *
	 * @param x
	 *            a number whose bits are to be put into an array
	 * @param n
	 *            array length
	 * @return array of byes where every byte represents a bit of the specified
	 *         integer
	 */
	public static byte[] indexToByteArray(int x, int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Number of bits can not be negative.");
		}

		boolean isNegative = x < 0;
		x = isNegative ? -x : x;
		byte[] byteArray = new byte[n];

		for (int i = byteArray.length - 1; i >= 0 && x > 0; i--) {
			byteArray[i] = (byte) (x % 2);
			x /= 2;
		}

		if (isNegative) {
			// implementation idea for two's complement: looking from right to
			// the left, find the first appearence of bit 1 and then invert all
			// bits to the left of it, for example: 010100-> 101 100
			boolean startInverting = false;
			for (int i = byteArray.length - 1; i >= 0; i--) {
				if (startInverting) {
					byteArray[i] = (byte) (byteArray[i] == 1 ? 0 : 1);
				}
				if (!startInverting && byteArray[i] == 1) {
					startInverting = true;
				}
			}
		}
		return byteArray;
	}

}
