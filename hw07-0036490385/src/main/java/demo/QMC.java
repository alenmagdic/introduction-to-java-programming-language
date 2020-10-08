package demo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.parser.ParserException;
import hr.fer.zemris.bf.qmc.Minimizer.Minimizer;
import hr.fer.zemris.bf.utils.Util;

public class QMC {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("> ");
			String input = sc.nextLine();
			if (input.trim().equals("quit")) {
				break;
			}

			String[] inputParts = splitInput(input);
			if (inputParts == null) {
				System.out.println("Invalid input!");
				continue;
			}

			List<String> vars = extractVariablesFromInput(inputParts[0]);
			if (vars == null) {
				continue;
			}

			Set<Integer> minterms;
			Set<Integer> dontCares;
			try {
				minterms = getMintermsFromInput(inputParts[1], vars);
				if (minterms == null) {
					continue;
				}

				if (inputParts[2].length() > 0) {
					dontCares = getMintermsFromInput(inputParts[2], vars);
					if (dontCares == null) {
						continue;
					}
				} else {
					dontCares = new LinkedHashSet<Integer>();
				}
			} catch (ParserException ex) {
				System.out.println("Invalid input!");
				continue;
			} catch (IllegalStateException ex) {
				System.out.println("There is at least one undeclared variable name.");
				continue;
			}

			List<String> minForms;
			try {
				Minimizer minimizer = new Minimizer(minterms, dontCares, vars);
				minForms = minimizer.getMinimalFormsAsString();
			} catch (IllegalArgumentException ex) {
				System.out.println("Invalid input.");
				continue;
			}

			for (int i = 0, n = minForms.size(); i < n; i++) {
				System.out.printf("%d. %s\n", i + 1, minForms.get(i));
			}
		}
		sc.close();
	}

	private static Set<Integer> getMintermsFromInput(String input, List<String> vars) throws ParserException {
		if (input.startsWith("[")) {
			return extractIndexesFromInput(input);
		} else {
			Parser parser = new Parser(input);
			return Util.toSumOfMinterms(vars, parser.getExpression());
		}
	}

	private static Set<Integer> extractIndexesFromInput(String input) {
		if (!input.endsWith("]")) {
			System.out.println("Invalid input!");
			return null;
		}

		Set<Integer> indexes = new LinkedHashSet<>();
		String[] strIndexes = input.substring(1, input.length() - 1).split(",");
		for (String index : strIndexes) {
			try {
				indexes.add(Integer.parseInt(index.trim()));
			} catch (NumberFormatException ex) {
				System.out.println("Invalid input!");
				return null;
			}
		}

		return indexes;
	}

	private static List<String> extractVariablesFromInput(String input) {
		String functionName = input.substring(0, input.indexOf("("));
		if (!isValidName(functionName)) {
			System.out.println("Invalid function name!");
			return null;
		}

		String names;
		try {
			names = input.substring(input.indexOf("(") + 1, input.indexOf(")"));
		} catch (IndexOutOfBoundsException ex) {
			System.out.println("Invalid input!");
			return null;
		}

		String[] namesArray = names.split(",");
		List<String> varNames = new ArrayList<>();
		for (String name : namesArray) {
			name = name.trim();
			if (!isValidName(name)) {
				System.out.println("Invalid input!");
				return null;
			} else {
				varNames.add(name);
			}
		}

		return varNames;
	}

	private static boolean isValidName(String name) {
		if (!Character.isLetter(name.charAt(0))) {
			return false;
		}
		for (int i = 1, n = name.length(); i < n; i++) {
			if (!Character.isLetterOrDigit(name.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private static String[] splitInput(String input) {
		int indexOfEq = input.indexOf("=");
		if (indexOfEq == -1) {
			return null;
		}

		String[] parts = new String[3];

		parts[0] = input.substring(0, indexOfEq).trim();

		int indexOfVerBar = input.indexOf("|");
		if (indexOfVerBar != -1) {
			parts[1] = input.substring(indexOfEq + 1, indexOfVerBar).trim();
			parts[2] = input.substring(indexOfVerBar + 1).trim();
		} else {
			parts[1] = input.substring(indexOfEq + 1).trim();
			parts[2] = "";
		}
		return parts;
	}
}
