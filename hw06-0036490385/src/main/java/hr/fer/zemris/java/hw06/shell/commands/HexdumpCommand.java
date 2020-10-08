package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * A command
 *
 * @author Alen Magdić
 *
 */
public class HexdumpCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "hexdump";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	/** The length of a line number. **/
	private static final int LINE_NUMBER_LENGTH = 8;
	/** Number of bytes that are printed in per line of output. **/
	private static final int BYTES_PER_LINE = 16;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("This command expects exactly one argument, a path that specifies");
		COMMAND_DESCRIPTION.add("a file whose hexadecimal content is to be printed, i.e. it prints");
		COMMAND_DESCRIPTION.add("out all bytes contained in the specified file as hexadecimal values.");
		COMMAND_DESCRIPTION.add("Every line contains the following data: line number (hexadecimal), .");
		COMMAND_DESCRIPTION.add("hexadecimal values of 16 bytes, and a string representation of that bytes.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArgumentsThatIncludeStrings(arguments);

		if (args.length != 1) {
			env.writeln("Expected exactly one argument, " + args.length + " given.");
		} else {
			try {
				printHexContent(env, args[0]);
			} catch (IOException e) {
				env.writeln("There was a problem. Operation failed.");
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints the hexadecimal content of the file with specified path using the
	 * specified environment to write an output.
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param filePath
	 *            path to the file whose content is to be printed
	 * @throws IOException
	 *             if there is a problem reading the specified file
	 */
	private void printHexContent(Environment env, String filePath) throws IOException {
		Path file;
		try {
			file = Paths.get(filePath);
		} catch (InvalidPathException ex) {
			env.writeln("The specified path is not valid.");
			return;
		}
		if (!Files.isRegularFile(file)) {
			env.writeln("The specified path does not represent a file.");
			return;
		}

		BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(file));
		long lineNumber = 0;
		byte[] buffer = new byte[16];
		while (true) {
			int numOfBytesRead = inputStream.read(buffer);
			writeLineNumber(lineNumber, env);

			for (int i = 0; i < BYTES_PER_LINE; i++) {
				String hexVal = Integer.toHexString(Byte.toUnsignedInt(buffer[i]));
				env.write((hexVal.length() == 1 ? "0" + hexVal : hexVal) + " ");
				if (i == 7) {
					env.write("|");
				}
			}
			env.write("|");
			writeStringRepresentation(env, buffer);

			lineNumber++;
			if (numOfBytesRead < BYTES_PER_LINE) {
				break;
			}
		}

		inputStream.close();
	}

	/**
	 * Creates and writes a string representation of the specified array of
	 * bytes using the specified environment to write an output. Every byte that
	 * is in range 32-127 is represented by it's character from the ascii table
	 * while the other bytes are represented by a dot.
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param buffer
	 *            an array of bytes whose string representation is to be written
	 *            to the output
	 */
	private void writeStringRepresentation(Environment env, byte[] buffer) {
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] >= 32 && buffer[i] <= 127) {
				env.write(new Character((char) buffer[i]).toString());
			} else {
				env.write(".");
			}
		}
		env.writeln("");

	}

	/**
	 * Writes the specified line number (i.e. it's hexadecimal value) using the
	 * specified environment to write an output.
	 *
	 * @param lineNumber
	 *            a line number, decimal number
	 * @param env
	 *            an environment used to write an output
	 */
	private void writeLineNumber(long lineNumber, Environment env) {
		String hexString = Long.toHexString(lineNumber);
		int numOfZeros = LINE_NUMBER_LENGTH - hexString.length();
		for (int i = 0; i < numOfZeros; i++) {
			env.write("0");
		}
		env.write(hexString + ": ");
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
