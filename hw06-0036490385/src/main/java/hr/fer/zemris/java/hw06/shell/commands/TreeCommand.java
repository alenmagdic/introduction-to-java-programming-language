package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * A command that prints a file tree with the specified root directory. The
 * command takes exactly one argument, a path that specifies the root directory
 * of the file tree that is to be printed out.
 *
 * @author Alen Magdić
 *
 */
public class TreeCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "tree";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Prints a file tree with the specified root directory.");
		COMMAND_DESCRIPTION.add("The command takes exactly one argument, a path that specifies");
		COMMAND_DESCRIPTION.add("the root directory of the file tree that is to be printed out.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArgumentsThatIncludeStrings(arguments);

		if (args.length != 1) {
			env.writeln("Expected exactly one argument, " + args.length + " given.");
		} else {
			try {
				printFileTree(env, args[0]);
			} catch (IOException e) {
				env.write("There was a problem. Operation failed.");
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints the file tree with the specified root directory and using the
	 * specified environment to write an output.
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param rootDir
	 *            a root directory of the tree
	 * @throws IOException
	 *             if there is a problem with walking through the file tree
	 */
	private void printFileTree(Environment env, String rootDir) throws IOException {
		Path root;
		try {
			root = Paths.get(rootDir);
		} catch (InvalidPathException ex) {
			env.writeln("The specified path is not valid.");
			return;
		}
		if (!Files.isDirectory(root)) {
			env.writeln("The specified path does not represent a directory.");
			return;
		}

		Files.walkFileTree(root, new FileVisitorImpl(env));

	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	/**
	 * An implementation of a file visitor. While visiting files, it prints a
	 * file tree using the specified environment (specified in constructor) to
	 * write an output.
	 *
	 * @author Alen Magdić
	 *
	 */
	private static class FileVisitorImpl implements FileVisitor<Path> {
		/** Current depth of file tree **/
		private int depth;
		/** An environment used to write an output **/
		private Environment environment;

		/**
		 * Constructor.
		 *
		 * @param env
		 *            an environment used to write an output
		 */
		public FileVisitorImpl(Environment env) {
			this.environment = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			if (depth == 0) {
				environment.writeln(dir.normalize().toAbsolutePath().toString());
			} else {
				printSpaces(2 * depth);
				environment.writeln(dir.getFileName().toString());
			}
			depth++;
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Prints a space character the specified number of times and using the
		 * environment specified in the constructor of this object to write an
		 * output.
		 *
		 * @param numOfSpaces
		 *            number of times that a space character is to be written to
		 *            the output
		 */
		private void printSpaces(int numOfSpaces) {
			for (int i = 0; i < numOfSpaces; i++) {
				environment.write(" ");
			}
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			printSpaces(2 * depth);
			environment.writeln(file.getFileName().toString());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			depth--;
			return FileVisitResult.CONTINUE;
		}

	}

}
