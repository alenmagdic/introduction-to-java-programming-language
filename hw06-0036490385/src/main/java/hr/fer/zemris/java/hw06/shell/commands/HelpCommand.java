package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * A command that prints some details that can help a user use the shell. The
 * command can be started with no arguments or with a single argument. If
 * started without any argument, it prints the list of all supported commands.
 * Otherwise, there should be a single argument representing the name of a
 * supported command. In that case, the command prints the description of the
 * specified command.
 *
 * @author Alen Magdić
 *
 */
public class HelpCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "help";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("This command can be started with no arguments or with a single argument.");
		COMMAND_DESCRIPTION.add("If started without any argument, it prints the list of all supported commands.");
		COMMAND_DESCRIPTION
				.add("Otherwise, there should be a single argument representing the name of a supported command.");
		COMMAND_DESCRIPTION.add("In that case, the command prints the description of the specified command.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArguments(arguments);

		if (args.length == 0) {
			printSupportedCommands(env);
		} else if (args.length == 1) {
			printCommandDescription(env, args[0]);
		} else {
			env.writeln("To many arguments given!");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints the description of a command with the specified name using the
	 * specified environment to write an output.
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param commandName
	 *            name of the command whose description is to be printed
	 */
	private void printCommandDescription(Environment env, String commandName) {
		ShellCommand command = env.commands().get(commandName);
		if (command == null) {
			env.writeln("Command '" + commandName + "' does not exist.");
			return;
		}

		env.writeln("Command name: " + commandName);
		for (String line : command.getCommandDescription()) {
			env.writeln(line);
		}
	}

	/**
	 * Prints the list of all supported commands using the specified
	 * environment.
	 *
	 * @param env
	 *            an environment used to write an output
	 */
	private void printSupportedCommands(Environment env) {
		String[] commands = env.commands().keySet().toArray(new String[0]);

		for (String command : commands) {
			env.writeln(command);
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
