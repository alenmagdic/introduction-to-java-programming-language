package hr.fer.zemris.java.hw06.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * A shell command. It provides a method for execution, and getters for command
 * name and command description.
 *
 * @author Alen Magdić
 *
 */
public interface ShellCommand {
	/**
	 * A method that executes this command using the specified environment to
	 * write an output and using the specified arguments.
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param arguments
	 *            command arguments
	 * @return a {@link ShellStatus}
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Gets the command name
	 *
	 * @return command name
	 */
	String getCommandName();

	/**
	 * Gets the command description.
	 *
	 * @return command description
	 */
	List<String> getCommandDescription();
}
