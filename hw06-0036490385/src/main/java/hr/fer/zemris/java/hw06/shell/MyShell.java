package hr.fer.zemris.java.hw06.shell;

import java.util.Map;

import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;

/**
 * This program is a shell that features ten commands, such as commands for
 * copying a file, makeing a new directory, listing files, printing a file tree,
 * etc. In order to get more details about commands, run the program and input
 * 'help' in order to get the full list of all supported commands. Or, specify
 * the name of a command in order to get more details about that specific
 * command (for example, input 'help tree' to get more details about the tree
 * command). In order to exit the program, there is an 'exit' command.
 *
 * @author Alen Magdić
 *
 */
public class MyShell {

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		ShellEnvironment environment = new ShellEnvironment();
		Map<String, ShellCommand> commands = environment.commands();
		ShellCommand command;
		ShellStatus status = ShellStatus.CONTINUE;

		environment.writeln("Welcome to MyShell v 1.0");
		do {
			String userInput;
			try {
				userInput = environment.readLine();
			} catch (ShellIOException ex) {
				// communication not possible -> terminate
				break;
			}

			String commandName = extractCommandName(userInput);
			if (commandName.isEmpty()) {
				continue;
			}

			String arguments = extractArguments(userInput);

			command = commands.get(commandName);
			if (command == null) {
				environment.writeln("Unknown command!");
				continue;
			}

			status = command.executeCommand(environment, arguments);
		} while (status != ShellStatus.TERMINATE);
	}

	/**
	 * Extracts the command arguments from the specified user input.
	 *
	 * @param userInput
	 *            a single user input
	 * @return the specified user input, but without the name of command, i.e.
	 *         there is only a part where arguments are
	 */
	private static String extractArguments(String userInput) {
		int commandNameLength = extractCommandName(userInput).length();
		if (userInput.length() == commandNameLength) {
			return "";
		}

		return userInput.substring(commandNameLength);
	}

	/**
	 * Extract the command name from the specified user input.
	 *
	 * @param userInput
	 *            a single user input
	 * @return a command name extracted from the specified user input
	 */
	private static String extractCommandName(String userInput) {
		return userInput.split("\\s+")[0];
	}

}
