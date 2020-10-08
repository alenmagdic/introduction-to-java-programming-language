package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * A command that prints the list of all supported charsets to the shell.
 *
 * @author Alen Magdić
 *
 */
public class CharsetsCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "charsets";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Prints the list of all supported charsets to the shell.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] availableCharsets = Charset.availableCharsets().keySet().toArray(new String[0]);

		for (String charset : availableCharsets) {
			env.writeln(charset);
		}
		return ShellStatus.CONTINUE;
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
