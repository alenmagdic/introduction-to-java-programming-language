package hr.fer.zemris.java.hw15.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Klasa koja sadrži pomoćne metode. Konkretno, sadrži samo jednu metodu, za
 * kalkuliranje hasha za zadani {@link String} uporabom algoritma SHA-1.
 *
 * @author Alen Magdić
 *
 */
public class Util {

	/**
	 * Kalkulira hash za zadani {@link String} uporabom algoritma SHA-1.
	 *
	 * @param string
	 *            string za koji treba napraviti hash
	 * @return SHA-1 hash zadanog string
	 */
	public static String calculateHash(String string) {
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ignorable) {
		}

		msgDigest.update(string.getBytes());

		return bytesToHexString(msgDigest.digest());
	}

	/**
	 * Pretvara polje bajtova u tekstualni heksadekadski zapis.
	 *
	 * @param bytes
	 *            polje bajtova koje treba pretvoriti u heksadekadski zapis
	 * @return polje bajtova u heksadekadskom zapisu
	 */
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder strB = new StringBuilder();

		for (byte b : bytes) {
			strB.append(Integer.toHexString(Byte.toUnsignedInt(b)));
		}
		return strB.toString();
	}

}
