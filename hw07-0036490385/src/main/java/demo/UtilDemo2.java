package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * A demonstration of toSumOfMinters method from class {@link Util}. The program
 * prints minterms of expression 'A and b or C'.
 *
 * @author Alen Magdić
 *
 */
public class UtilDemo2 {

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
		System.out.println("Mintermi f(" + variables + "): " + Util.toSumOfMinterms(variables, expression));

		List<String> variables2 = new ArrayList<>(variables);
		Collections.reverse(variables2);
		System.out.println("Mintermi f(" + variables2 + "): " + Util.toSumOfMinterms(variables2, expression));
	}

}
