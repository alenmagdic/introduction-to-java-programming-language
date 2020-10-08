package hr.fer.zemris.java.hw06.crypto;

/**
 * A class that contains static methods such as a method for converting a hex to
 * byte array and vice versa.
 *
 * @author Alen Magdić
 *
 */
public class Util {

	/**
	 * Converts the specified string of hexadecimal values to the array of
	 * bytes. The string's length has to be an even number.
	 *
	 * @param hex
	 *            a string of hexadecimal values
	 * @return array of bytes with values from the specified string
	 */
	public static byte[] hextobyte(String hex) {
		if (hex == null) {
			throw new IllegalArgumentException("Null is not a legal argument in this method.");
		}
		if (hex.length() % 2 == 1) {
			throw new IllegalArgumentException(
					"The number of elements in the argument is expected to be an even number.");
		}

		byte[] bytes = new byte[hex.length() / 2];
		char[] hexChars = hex.toLowerCase().toCharArray();

		for (int i = 0; i < hex.length(); i += 2) {
			if (!isHexDigit(hexChars[i])) {
				throw new IllegalArgumentException(
						"Invalid string given. It does not contain a valid hex number. String: " + hex);
			}

			bytes[i / 2] = (byte) (getHexCharAsDecimalDigit(hexChars[i]) * 16
					+ getHexCharAsDecimalDigit(hexChars[i + 1]));
		}
		return bytes;
	}

	/**
	 * Checks if the specified character is a hexadecimal digit.
	 *
	 * @param c
	 *            a character that is to be checked
	 * @return true if the specified character is a hexadecimal digit.
	 */
	private static boolean isHexDigit(char c) {
		return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
	}

	/**
	 * Converts the specified array of bytes to a string of hexadecimal values.
	 *
	 * @param bytes
	 *            array of bytes that are to be converted
	 * @return string of hexadecimal values, made from the specified array of
	 *         bytes
	 */
	public static String bytetohex(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("Null is not a legal argument in this method.");
		}

		StringBuilder hexBuilder = new StringBuilder();

		for (byte b : bytes) {
			int unsignedByte = Byte.toUnsignedInt(b);

			int firstDigit = unsignedByte / 16 % 16;
			int secondDigit = unsignedByte % 16;

			char firstDigitAsChar = getDecimalDigitAsHexChar(firstDigit);
			char secondDigitAsChar = getDecimalDigitAsHexChar(secondDigit);

			hexBuilder.append(firstDigitAsChar);
			hexBuilder.append(secondDigitAsChar);
		}

		return hexBuilder.toString();
	}

	/**
	 * Converts the specified decimal digit to a hexadecimal digit.
	 *
	 * @param digit
	 *            a decimal digit that is to be converted
	 * @return a hexadecimal digit
	 */
	private static char getDecimalDigitAsHexChar(int digit) {
		return (char) (digit < 10 ? '0' + digit : 'a' + (digit - 10));
	}

	/**
	 * Converts the specified hexadecimal character (i.e. digit) to a decimal
	 * digit.
	 *
	 * @param hexChar
	 *            hexadecimal character
	 * @return decimal digit
	 */
	private static int getHexCharAsDecimalDigit(char hexChar) {
		return hexChar >= '0' && hexChar <= '9' ? hexChar - '0' : hexChar - 'a' + 10;
	}
}
