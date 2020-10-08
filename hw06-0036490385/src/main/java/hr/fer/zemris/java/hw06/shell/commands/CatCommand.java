package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
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
 * A command that prints the content of the specified file. If one argument
 * given, it is expected to be the path to the file that is to be printed. If
 * two arguments given, the first should be the path to the file that is to be
 * printed. while the other one should be a charset name that is to be used for
 * reading the specified file.
 *
 * @author Alen Magdić
 *
 */
public class CatCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "cat";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Prints the content of the specified file to the shell.");
		COMMAND_DESCRIPTION
				.add("If one argument given, it is expected to be the path to the file that is to be printed.");
		COMMAND_DESCRIPTION
				.add("If two arguments given, the first should be the path to the file that is to be printed.");
		COMMAND_DESCRIPTION
				.add("while the other one should be a charset name that is to be used for reading the specified file.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArgumentsThatIncludeStrings(arguments);

		if (args.length == 0) {
			env.writeln("Expected at least one argument, 0 given.");
		} else if (args.length == 1 || args.length == 2) {
			try {
				printFileContents(args[0], args.length == 1 ? null : args[1], env);
			} catch (IOException e) {
				env.writeln("There was a problem reading the file.");
			}
		} else {
			env.writeln("To many arguments given!");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints the contents of the specified file (specified by it's path) using
	 * the specified charset name and using the specified environment for
	 * writing the output.
	 *
	 * @param path
	 *            a path to the file that is to be printed
	 * @param charsetName
	 *            a name of the charset that is to be used
	 * @param env
	 *            an environment used to write the output
	 * @throws IOException
	 *             if there is a problem reading the specified file
	 */
	private void printFileContents(String path, String charsetName, Environment env) throws IOException {
		Path filePath;
		try {
			filePath = Paths.get(path);
		} catch (InvalidPathException ex) {
			env.writeln("The specified path is not valid.");
			return;
		}

		if (!Files.exists(filePath)) {
			env.writeln("The specified file does not exist!");
			return;
		}

		Charset charset;
		if (charsetName == null) {
			charset = Charset.defaultCharset();
		} else if (Charset.availableCharsets().containsKey(charsetName)) {
			charset = Charset.forName(charsetName);
		} else {
			env.writeln("Unknown charset!");
			return;
		}

		BufferedReader reader = Files.newBufferedReader(filePath, charset);

		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}

			env.writeln(line);
		}

		reader.close();
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
