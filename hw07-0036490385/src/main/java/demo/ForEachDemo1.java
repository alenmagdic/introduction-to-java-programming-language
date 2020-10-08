package demo;

import java.util.Arrays;

import hr.fer.zemris.bf.utils.Util;

/**
 * A demonstration of method forEach from {@link Util}. Prints all combinations
 * from truth table for three variables.
 *
 * @author Alen Magdić
 *
 */
public class ForEachDemo1 {

	/**
	 * Starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Util.forEach(Arrays.asList("A", "B", "C"),
				values -> System.out.println(Arrays.toString(values).replaceAll("true", "1").replaceAll("false", "0")));
	}

}
