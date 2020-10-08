package demo;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * A demonstration of filterAssignments method from class {@link Util}. The
 * program prints all combinations of values that give result 'true' for
 * expression 'A and b or C'.
 *
 * @author Alen Magdić
 *
 */
public class UtilDemo1 {

	/**
	 * Starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {

		Node expression = new Parser("A and b or C").getExpression();

		VariablesGetter getter = new VariablesGetter();
		expression.accept(getter);

		List<String> variables = getter.getVariables();
		for (boolean[] values : Util.filterAssignments(variables, expression, true)) {
			System.out.println(Arrays.toString(values).replaceAll("true", "1").replaceAll("false", "0"));
		}
	}

}
