package hr.fer.zemris.java.hw04.db;

/**
 * This is an implementation of the LIKE operator. The first operand of this
 * operator is a string while the second operand is a pattern that can contain
 * only one or zero wildcards represented by an asterisk ('*'). A wildcard
 * represents a variable number of characters. So, for example, a string is LIKE
 * pattern "En*nd" if it starts with "En" and ends with "nd". "England" is an
 * example of string that is LIKE that pattern, but so is "Engdsfgdnd" because
 * it also starts with "En" and ands with "nd".
 *
 * @author AlenMagdić
 *
 */
public class LikeOperator implements IComparisonOperator {

	@Override
	public boolean satisfied(String value, String pattern) {
		String[] patternParts = pattern.split("\\*");
		if (patternParts.length > 2) {
			throw new IllegalArgumentException("A pattern can not contain more than one wildcard.");
		}

		if (!pattern.contains("*")) {
			return pattern.equals(value);
		}

		if (pattern.equals("*")) {
			return true;
		}

		if (pattern.startsWith("*")) {
			return value.endsWith(patternParts[1]);
		} else if (pattern.endsWith("*")) {
			return value.startsWith(patternParts[0]);
		} else {
			return value.startsWith(patternParts[0]) && value.endsWith(patternParts[1])
					&& value.length() >= patternParts[0].length() + patternParts[1].length();
		}
	}

}
