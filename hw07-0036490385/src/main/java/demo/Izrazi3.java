package demo;

import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.parser.ParserException;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * A demonstration of {@link VariablesGetter}. The program prints a list of
 * variable names contained in various expressions.
 *
 * @author Alen Magdić
 *
 */
public class Izrazi3 {

	/**
	 * Starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		String[] expressions = new String[] { "0", "tRue", "Not a", "A aNd b", "a or b", "a xoR b", "A and b * c",
				"a or b or c", "a xor b :+: c", "not not a", "a or b xor c and d", "a or b xor c or d",
				"a xor b or c xor d", "(a + b) xor (c or d)", "(d or b) xor not (a or c)", "(c or d) mor not (a or b)",
				"not a not b", "a and (b or", "a and (b or c", "a and 10" };

		for (String expr : expressions) {
			System.out.println("==================================");
			System.out.println("Izraz: " + expr);
			System.out.println("==================================");

			try {
				System.out.println("Varijable:");
				Parser parser = new Parser(expr);
				VariablesGetter getter = new VariablesGetter();
				parser.getExpression().accept(getter);
				System.out.println(getter.getVariables());
			} catch (ParserException | LexerException ex) {
				System.out.println("Iznimka: " + ex.getClass() + " - " + ex.getMessage());
			}
			System.out.println();
		}
	}
}
