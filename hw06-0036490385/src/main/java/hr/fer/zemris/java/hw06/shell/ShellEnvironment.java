package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirCommand;
import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeCommand;

/**
 * A shell environment. It provides methods for input/output from the standard
 * input and to the standard output. It stores commands and some special symbols
 * such as a multiline symbol, a prompt symbol and a morelines symbol. A
 * multiline symbol is a symbol printed at the beginning of the line of the user
 * input when it is not the first line of that input. At the beginning of the
 * first line, there is a prompt symbol. In order to be able to use a multiline
 * input, the user has to put a morelines symbol at the end of the line of
 * input.
 *
 * @author Alen Magdić
 *
 */
public class ShellEnvironment implements Environment {
	/** A scanner used for user input **/
	private Scanner scanner;
	/**
	 * Map of commands. A command name is the map key, while a
	 * {@link ShellCommand} is the map value.
	 **/
	private SortedMap<String, ShellCommand> mapOfCommands;

	/** A multiline symbol. **/
	private Character multilineSymbol;
	/** A morelines symbol. **/
	private Character morelinesSymbol;
	/** A prompt symbol. **/
	private Character promptSymbol;

	/**
	 * Default constructor.
	 */
	public ShellEnvironment() {
		scanner = new Scanner(System.in);

		multilineSymbol = '|';
		morelinesSymbol = '\\';
		promptSymbol = '>';

		mapOfCommands = new TreeMap<String, ShellCommand>();
		mapOfCommands.put("symbol", new SymbolCommand());
		mapOfCommands.put("exit", new ExitShellCommand());
		mapOfCommands.put("help", new HelpCommand());
		mapOfCommands.put("charsets", new CharsetsCommand());
		mapOfCommands.put("cat", new CatCommand());
		mapOfCommands.put("ls", new LsCommand());
		mapOfCommands.put("tree", new TreeCommand());
		mapOfCommands.put("copy", new CopyCommand());
		mapOfCommands.put("mkdir", new MkdirCommand());
		mapOfCommands.put("hexdump", new HexdumpCommand());
	}

	@Override
	public String readLine() throws ShellIOException {
		StringBuilder inputBuilder = new StringBuilder();
		write(Character.toString(promptSymbol) + " ");

		while (true) {
			String line;
			try {
				line = scanner.nextLine();
			} catch (RuntimeException ex) {
				throw new ShellIOException("Can not read input.", ex);
			}

			if (!line.endsWith(Character.toString(morelinesSymbol))) {
				inputBuilder.append(line);
				return inputBuilder.toString();
			}

			line = line.substring(0, line.lastIndexOf(morelinesSymbol));
			inputBuilder.append(line);

			write(Character.toString(multilineSymbol) + " ");
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(mapOfCommands);
	}

	@Override
	public Character getMultilineSymbol() {
		return multilineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		if (symbol == null) {
			throw new IllegalArgumentException("Multiline symbol can not be set to null");
		}

		multilineSymbol = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		if (symbol == null) {
			throw new IllegalArgumentException("Prompt symbol can not be set to null");
		}

		promptSymbol = symbol;
	}

	@Override
	public Character getMorelinesSymbol() {
		return morelinesSymbol;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		if (symbol == null) {
			throw new IllegalArgumentException("Morelines symbol can not be set to null");
		}

		morelinesSymbol = symbol;
	}

}
