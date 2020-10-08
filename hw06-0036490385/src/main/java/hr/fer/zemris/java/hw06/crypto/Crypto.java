package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This is a program that offers the next features: checking the SHA-256 file
 * digest, encryption of a file and the decryption. It expects at least two
 * arguments. If a user wants to check SHA-256 file digest, the first argument
 * should be "checksha" and then the name of the file that is to be checked. If
 * a user wants to encrypt a file, the first argument should be set to
 * "encrypt", the second argument has to be the name of the file that is to
 * encrypted and the name of the encrypted file (that will be created by this
 * program). Instead of giving only a name as argument, the user can also give
 * an absolute or relative path to the file.
 *
 * @author Alen Magdić
 *
 */
public class Crypto {

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("This program expects at least two arguments.");
			return;
		}

		Scanner sc = new Scanner(System.in);

		if (args[0].equals("checksha")) {
			checkFileDigestOperation(args, sc);
		} else if (args[0].equals("encrypt") || args[0].equals("decrypt")) {
			encryptionDecryptionOperation(args, sc);
		} else {
			System.out.println("Invalid argument given!");
		}

		sc.close();
	}

	/**
	 * Gets user input.
	 *
	 * @param message
	 *            a message that is to be written before waiting for input
	 * @param scanner
	 *            scanner used for input
	 * @return input text
	 */
	private static String getUserInput(String message, Scanner scanner) {
		System.out.print(message);
		String input = scanner.nextLine();
		return input;
	}

	/**
	 * This method executes the operation of decryption or encryption, depending
	 * of the content of the specified array of arguments. If the first argument
	 * is equal to 'decryption', it executes a decryption of the specified file
	 * (the second argument). Otherwise, it executes an encryption of the
	 * specified file. To do that, it asks the user to input the password of
	 * encryption and an initialization vector, both as hex-encoded texts.
	 *
	 * @param args
	 *            array of command line arguments
	 * @param sc
	 *            scanner that is to be used for getting a user input
	 */
	private static void encryptionDecryptionOperation(String args[], Scanner sc) {
		if (args.length != 3) {
			System.out.println("Unexpected number of arguments for '" + args[0]
					+ "' operation. Expected 3 arguments, given " + args.length);
			return;
		}

		String inputFileName = args[1];
		String outputFileName = args[2];
		String keyText = getUserInput("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n>",
				sc);
		String ivText = getUserInput("Please provide initialization vector as hex-encoded text (32 hex-digits):\n>",
				sc);

		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException ignorable) {
		} catch (NoSuchPaddingException ignorable) {
		}
		try {
			cipher.init(args[0].equals("encrypt") ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (InvalidKeyException ex) {
			System.out.println("The given key is not valid!");
		} catch (InvalidAlgorithmParameterException ex) {
			System.out.println("The given initialization vector is not valid!");
		}

		InputStream inStream;
		try {
			inStream = new BufferedInputStream(Files.newInputStream(Paths.get(inputFileName)));
		} catch (IOException e) {
			System.out.println("There was a problem opening the given file. File: " + inputFileName);
			return;
		}

		OutputStream outStream;
		try {
			outStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(outputFileName)));
		} catch (IOException e) {
			System.out.println("There was a problem opening the given file. File: " + outputFileName);
			return;
		}

		byte[] buffer = new byte[1024];
		while (true) {
			int numOfBytesRead;
			try {
				numOfBytesRead = inStream.read(buffer);
			} catch (IOException e) {
				System.out.println("There was a problem reading the given file. File: " + inputFileName);
				return;
			}

			if (numOfBytesRead == -1) {
				break;
			}

			try {
				outStream.write(cipher.update(buffer, 0, numOfBytesRead));
			} catch (IOException e) {
				System.out.println("There was a problem writing to the given file. File: " + outputFileName);
			}
		}

		try {
			outStream.write(cipher.doFinal());
		} catch (IllegalBlockSizeException | BadPaddingException | IOException e1) {
			System.out.println("There was a problem. Operation failed!");
			return;
		}
		try {
			inStream.close();
		} catch (IOException ignorable) {
		}
		try {
			outStream.close();
		} catch (IOException ignorable) {
		}

		if (args[0].equals("encrypt")) {
			System.out.println("Encryption completed. Generated file " + args[2] + " based on file " + args[1] + ".");
		} else {
			System.out.println("Decryption completed. Generated file " + args[2] + " based on file " + args[1] + ".");
		}
	}

	/**
	 * This method checks if the SHA-256 file digest of the specified file (the
	 * file is specified in the second command line argument) matches the digest
	 * from user input.
	 *
	 * @param args
	 *            command line arguments
	 * @param sc
	 *            scanner that is to be used for user input
	 */
	private static void checkFileDigestOperation(String args[], Scanner sc) {
		if (args.length > 2) {
			System.out
					.println("Too many arguments for 'checksha' operation. Expected 2 arguments, given " + args.length);
			return;
		}

		String fileName = args[1];
		String expDigest = getUserInput("Please provide expected sha-256 digest for " + fileName + ":\n>", sc);

		String digest;
		try {
			digest = calculateFileDigest(fileName);
		} catch (IOException e) {
			System.out.println("There was a problem reading the given file. File: " + fileName);
			return;
		}

		if (digest.equals(expDigest.toLowerCase())) {
			System.out.println("Digesting completed. Digest of " + fileName + " matches expected digest.");
		} else {
			System.out.println("Digesting completed. Digest of " + fileName
					+ " does not match the expected digest. Digest was: " + digest);
		}
	}

	/**
	 * Calculates the SHA-256 file digest of the file with the specified name.
	 *
	 * @param fileName
	 *            name of the file whose SHA-256 file digest is to be calculated
	 * @return file digest of the file with the specified name
	 * @throws IOException
	 *             if there is a problem with reading the file
	 */
	private static String calculateFileDigest(String fileName) throws IOException {
		InputStream inputStream = Files.newInputStream(Paths.get(fileName));
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException ignorable) {
		}

		while (true) {
			int aByte = inputStream.read();
			if (aByte == -1) {
				break;
			}
			msgDigest.update((byte) aByte);
		}
		return Util.bytetohex(msgDigest.digest());
	}
}
