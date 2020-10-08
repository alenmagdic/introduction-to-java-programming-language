package hr.fer.zemris.java.hw06.shell.commands;

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
 * A command that makes a directory (or a directory structure) that is specified
 * by the given path, i.e. the command takes exactly one argument, a path that
 * specifies the new directory structure.
 *
 * @author Alen Magdić
 *
 */
public class MkdirCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "mkdir";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Makes a directory (or a directory structure) that is specified by the given path,");
		COMMAND_DESCRIPTION
				.add("i.e. the command takes exactly one argument, a path that specifies the new directory structure.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArgumentsThatIncludeStrings(arguments);

		if (args.length != 1) {
			env.writeln("Expected exactly 1 argument, " + args.length + " given.");
		} else {
			makeDirectory(args[0], env);
		}
		return null;
	}

	/**
	 * Makes the specified directory using the specified environment to write an
	 * output.
	 *
	 * @param directoryPath
	 *            directory or directory structure that is to be created
	 * @param env
	 *            an environment used to write an output
	 */
	private void makeDirectory(String directoryPath, Environment env) {
		Path path;
		try {
			path = Paths.get(directoryPath);
		} catch (InvalidPathException ex) {
			env.writeln("The specified path is not valid.");
			return;
		}

		if (Files.exists(path)) {
			env.writeln("The specified path already exists.");
			return;
		}

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			env.writeln("Operation failed.");
		}
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
