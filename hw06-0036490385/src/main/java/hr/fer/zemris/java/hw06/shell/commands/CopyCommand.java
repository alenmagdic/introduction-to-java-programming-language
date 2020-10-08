package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * A command that copies a file to the specified path. There are two arguments
 * expected. The first one is a path to the file. that is to be copied, while
 * the other one can be either a path to a destination directory or a path that
 * includes the name of the copy. If there is a path to the destination
 * directory given as the second argument, name of the copied file will be the
 * same as the name of original file.
 *
 * @author Alen Magdić
 *
 */
public class CopyCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "copy";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Copies a file to the specified path.");
		COMMAND_DESCRIPTION.add("There are two arguments expected. The first one is a path to the file.");
		COMMAND_DESCRIPTION.add("that is to be copied, while the other one can be either a path to a");
		COMMAND_DESCRIPTION.add("destination directory or a path that includes the name of the copy.");
		COMMAND_DESCRIPTION.add("If there is a path to the destination directory given as the second argument, ");
		COMMAND_DESCRIPTION.add("name of the copied file will be the same as the name of original file.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArgumentsThatIncludeStrings(arguments);

		if (args.length != 2) {
			env.writeln("Expected exactly 2 arguments, " + args.length + " given.");
		} else {
			try {
				copyFile(args[0], args[1], env);
			} catch (Exception e) {
				env.writeln("There was a problem, the specified file could not be copied.");
			}
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Copies the file from the specified source to the specified destination
	 * using the specified environment to write an output.
	 *
	 * @param source
	 *            name of the file that is to be copied
	 * @param destination
	 *            name of the destination file, or a destination directory
	 * @param env
	 *            an environment used to write an output
	 * @throws IOException
	 *             if there is a problem reading the source file
	 * @throws InvalidPathException
	 *             if there is an invalid source or destination path given
	 */
	private void copyFile(String source, String destination, Environment env) throws IOException, InvalidPathException {
		Path sourcePath = Paths.get(source);
		Path destinationPath = Paths.get(destination);

		if (sourcePath.equals(destinationPath)) {
			env.writeln("The source and the destination can not be equal.");
			return;
		}
		if (!Files.exists(sourcePath)) {
			env.writeln("The specified file does not exist.");
			return;
		}
		if (Files.isDirectory(sourcePath)) {
			env.writeln("Copy command can not copy a directory.");
			return;
		}
		if (Files.isDirectory(destinationPath)) {
			destinationPath = Paths.get(destinationPath.toString(), sourcePath.getFileName().toString());
		}
		if (Files.exists(destinationPath)) {
			env.write(
					"There already is a file with the same name at the specified destination. \nDo you want to overwrite it? [y/n] ");

			while (true) {
				String answer;
				try {
					answer = env.readLine().trim();
				} catch (ShellIOException ex) {
					return;
				}
				if (answer.equals("n")) {
					env.writeln("Operation canceled.");
					return;
				} else if (answer.equals("y")) {
					env.writeln("Operation confirmed.");
					break;
				} else {
					env.writeln("Invalid answer. Answer 'y' for 'yes' or 'n' for 'no'.");
				}
			}
		}

		BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(sourcePath));
		BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(destinationPath));
		while (true) {
			int b = inputStream.read();
			if (b == -1) {
				break;
			}

			outputStream.write(b);
		}

		inputStream.close();
		outputStream.close();
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
