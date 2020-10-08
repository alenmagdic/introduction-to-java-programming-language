package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;

/**
 * A collection of methods that are used by {@link ShellCommand} objects. It
 * contains different methods for splitting the specified array of arguments.
 *
 * @author Alen Magdić
 *
 */
public class Util {

	/**
	 * Gets array of arguments made by splitting the specified string containing
	 * the arguments. The arguments are splitted by blank characters.
	 *
	 * @param arguments
	 *            arguments that are to be splitted to an array
	 * @return an array of arguments
	 */
	public static String[] getArrayOfArguments(String arguments) {
		if (arguments.trim().isEmpty()) {
			return new String[0];
		}
		return arguments.trim().split("\\s+");
	}

	/**
	 * Gets array of arguments made by splitting the specified string containing
	 * the arguments. This method enables appearances of arguments that contain
	 * spaces. In order to treat a sequence containing spaces as a single
	 * argument, it has to be written between quotation marks (like a string).
	 * Example: arg1 "a r g2" arg2 -> this will create an array of three
	 * arguments: arg1,a r g2 and arg2. Inside of quotation marks can be used an
	 * escape character '\' in order to write a quotation mark or to write the
	 * '\' itself.
	 *
	 *
	 * @param arguments
	 *            arguments in a single string
	 * @return splitted arguments
	 */
	public static String[] getArrayOfArgumentsThatIncludeStrings(String arguments) {
		if (arguments.indexOf('"') == -1) {
			return getArrayOfArguments(arguments);
		}

		List<String> args = new ArrayList<>();
		arguments = arguments.trim();

		int index = 0;
		while (index < arguments.length()) {
			int nextQuoteIndex = indexOfNextQuote(arguments, index);

			// process part of arguments that comes before the string part
			if (nextQuoteIndex == -1) {
				args.addAll(Arrays.asList(getArrayOfArguments(arguments.substring(index))));
				return args.toArray(new String[0]);
			} else if (nextQuoteIndex > index) {
				args.addAll(Arrays.asList(getArrayOfArguments(arguments.substring(index, nextQuoteIndex))));
			}

			// extract the string
			StringBuilder stringB = new StringBuilder();
			int lastEscapeCharIndex = -1;
			int stringStartIndex = nextQuoteIndex + 1;
			int stringEndIndex = -1;

			for (int i = stringStartIndex; i < arguments.length(); i++) {

				if (arguments.charAt(i) == '\\' && (lastEscapeCharIndex == -1 || lastEscapeCharIndex < i - 1)) {
					if (i + 1 < arguments.length()
							&& (arguments.charAt(i + 1) == '\\' || arguments.charAt(i + 1) == '\"')) {
						lastEscapeCharIndex = i;
						continue;
					}
				}

				if (arguments.charAt(i) == '\"' && (lastEscapeCharIndex == -1 || lastEscapeCharIndex < i - 1)) {
					stringEndIndex = i;
					break;
				}

				stringB.append(arguments.charAt(i));
			}

			args.add(stringB.toString());

			index = stringEndIndex + 1;
			if (index < arguments.length() && !isBlankChar(arguments.charAt(index))) {
				throw new RuntimeException("There has to be at least one space after a string!");
			}
		}

		return args.toArray(new String[0]);
	}

	/**
	 * Checks if the specified character is a blank character. A blank character
	 * is one of the following characters: ' ','\t','\n','\r'
	 *
	 * @param ch
	 *            a character that is to be checked
	 * @return true if the specified character is a blank character
	 */
	private static boolean isBlankChar(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
	}

	/**
	 * Returns an index of the next quote looking from the perspective of the
	 * specified index. If no such quote found, a -1 is returned.
	 *
	 * @param arguments
	 *            a string containg arguments and maybe containing quotes
	 * @param index
	 *            index that is the starting point of the search for quotes
	 * @return an index of the next quote
	 */
	private static int indexOfNextQuote(String arguments, int index) {
		if (index == 0 && arguments.charAt(index) == '\"') {
			return 0;
		}

		int relatIndexOfSpaceQuote = arguments.substring(index).indexOf(" \"");
		int relatIndexOfTabQuote = arguments.substring(index).indexOf("\t\"");

		int relatIndex;
		if (relatIndexOfSpaceQuote == -1 && relatIndexOfTabQuote == -1) {
			return -1;
		} else if (relatIndexOfSpaceQuote == -1) {
			relatIndex = relatIndexOfTabQuote;
		} else if (relatIndexOfTabQuote == -1) {
			relatIndex = relatIndexOfSpaceQuote;
		} else {
			relatIndex = relatIndexOfSpaceQuote < relatIndexOfTabQuote ? relatIndexOfSpaceQuote : relatIndexOfTabQuote;
		}

		return index + relatIndex + 1;
	}
}
