package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * A command that quits the shell. It takes no arguments.
 *
 * @author Alen Magdić
 *
 */
public class ExitShellCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "exit";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Quits the shell.");
		COMMAND_DESCRIPTION.add("Takes no arguments. The given arguments are ignored.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
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
