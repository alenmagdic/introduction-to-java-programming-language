package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;

/**
 * An environment that provides methods for input/output, and stores commands
 * and some special symbols such as a multiline symbol, a prompt symbol and a
 * morelines symbol. A multiline symbol is a symbol printed at the beginning of
 * the line of the user input when it is not the first line of that input. At
 * the beginning of the first line, there is a prompt symbol. In order to be
 * able to use a multiline input, the user has to put a morelines symbol at the
 * end of the line of input.
 *
 * @author Alen Magdić
 *
 */
public interface Environment {

	/**
	 * Reads a single input from the user and returns it as a single line. If
	 * the input contains multiple lines, it concatenates them into a single
	 * string without the morelines symbol.
	 *
	 * @return the user input
	 * @throws ShellIOException
	 *             if unable to get the user input
	 */
	public String readLine() throws ShellIOException;

	/**
	 * Writes the specified string to the output.
	 *
	 * @param text
	 *            a string that is to be written
	 * @throws ShellIOException
	 *             if unable to write to the output
	 */
	public void write(String text) throws ShellIOException;

	/**
	 * Writes the specified string to the output appending a new line character
	 * at the end of the string.
	 *
	 * @param text
	 *            a string that is to be written
	 * @throws ShellIOException
	 *             if unable to write to the output
	 */
	public void writeln(String text) throws ShellIOException;

	/**
	 * Gets the map of commands. The key of the map is a command name, while the
	 * value is a reference to the {@link ShellCommand} object.
	 *
	 * @return map of commands
	 */
	public SortedMap<String, ShellCommand> commands();

	/**
	 * Gets the multiline symbol.
	 *
	 * @return the multiline symbol
	 */
	public Character getMultilineSymbol();

	/**
	 * Sets the multiline symbol.
	 *
	 * @param symbol
	 *            a new multiline symbol
	 */
	public void setMultilineSymbol(Character symbol);

	/**
	 * Gets the prompt symbol.
	 *
	 * @return the prompt symbol
	 */
	public Character getPromptSymbol();

	/**
	 * Sets the prompt symbol.
	 *
	 * @param symbol
	 *            a new prompt symbol
	 */
	public void setPromptSymbol(Character symbol);

	/**
	 * Gets the morelines symbol.
	 *
	 * @return the morelines symbol
	 */
	public Character getMorelinesSymbol();

	/**
	 * Sets the morelines symbol.
	 *
	 * @param symbol
	 *            a new morelines symbol
	 */
	public void setMorelinesSymbol(Character symbol);

}
