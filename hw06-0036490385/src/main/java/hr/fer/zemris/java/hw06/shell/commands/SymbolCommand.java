package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * This command's action is determined by the number of arguments it is given.
 * If one argument given, it is expected to be a symbol name. Symbol names:
 * MORELINES, MULTILINE, PROMPT. If two arguments given, the first one should be
 * a symbol name and the second should be a new symbol that represents the
 * symbol with the specified name.
 *
 * @author Alen Magdić
 *
 */
public class SymbolCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "symbol";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Number of arguments given determine what this command does.");
		COMMAND_DESCRIPTION.add(
				"If one argument given, it is expected to be a symbol name. Symbol names: MORELINES, MULTILINE, PROMPT.");
		COMMAND_DESCRIPTION
				.add("If two arguments given, the first one should be a symbol name and the second should be");
		COMMAND_DESCRIPTION.add("a new symbol that represents the symbol with the specified name.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArguments(arguments);

		if (args.length == 0) {
			env.writeln("Symbol name not defined!");
		} else if (args.length == 1) {
			printSymbol(env, args[0]);
		} else if (args.length == 2) {
			changeSymbol(env, args);
		} else {
			env.writeln("To many arguments given!");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Changes a symbol with the specified name to the new specified symbol
	 * using the specified environment to write an output. Both symbol name and
	 * the new symbol are indirectly specified through the specified array of
	 * arguments where the first argument is the name of the symbol whose new
	 * value is to be set (MORELINES/MULTILINE/PROMPT).
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param args
	 *            command arguments
	 */
	private void changeSymbol(Environment env, String[] args) {
		String symbolName = args[0];
		String newSymbol = args[1];
		if (newSymbol.length() > 1) {
			env.writeln("To long argument given! A symbol was expected.");
			return;
		}

		if (symbolName.equals("MORELINES")) {
			env.write("Symbol for MORELINES changed from '" + env.getMorelinesSymbol() + "' to '");
			env.setMorelinesSymbol(newSymbol.toCharArray()[0]);
			env.writeln(env.getMorelinesSymbol() + "'");

		} else if (symbolName.equals("PROMPT")) {
			env.write("Symbol for PROMPT changed from '" + env.getPromptSymbol() + "' to '");
			env.setPromptSymbol(newSymbol.toCharArray()[0]);
			env.writeln(env.getPromptSymbol() + "'");

		} else if (symbolName.equals("MULTILINE")) {
			env.write("Symbol for MULTILINE changed from '" + env.getMultilineSymbol() + "' to '");
			env.setMultilineSymbol(newSymbol.toCharArray()[0]);
			env.writeln(env.getMultilineSymbol() + "'");

		} else {
			env.writeln("Unknown symbol name!");
		}

	}

	/**
	 * Prints the name of the symbol specified by it's name using the specified
	 * environment to write an output.
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param symbolName
	 *            name of the symbol (MORELINES/MULTILINE/PROMPT) whose value is
	 *            to be printed
	 */
	private void printSymbol(Environment env, String symbolName) {

		if (symbolName.equals("MORELINES")) {
			env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'");
		} else if (symbolName.equals("PROMPT")) {
			env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
		} else if (symbolName.equals("MULTILINE")) {
			env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'");
		} else {
			env.writeln("Unknown symbol name!");
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
