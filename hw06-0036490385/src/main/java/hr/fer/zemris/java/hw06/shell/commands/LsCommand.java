package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * A command that lists all files in the specified directory. The command takes
 * exactly one argument, a path that specifies the directory whose files are to
 * be listed. The output consists of 4 columns. First column indicates if
 * current object is directory (d), readable (r), writable (w) and executable
 * (x). Second column contains object size in bytes that is right aligned and
 * occupies 10 characters. Follows file creation date/time and finally file
 * name. An example of output: \"-rw- 0 2008-09-02 12:59:31 ca.key\"
 *
 * @author Alen Magdić
 *
 */
public class LsCommand implements ShellCommand {
	/** Command name **/
	private static final String COMMAND_NAME = "ls";
	/** Command description **/
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();
		COMMAND_DESCRIPTION.add("Lists all files in the specified directory.");
		COMMAND_DESCRIPTION.add("The command takes exactly one argument, a path that specifies");
		COMMAND_DESCRIPTION.add("the directory whose files are to be listed.");
		COMMAND_DESCRIPTION.add("The output consists of 4 columns. First column indicates");
		COMMAND_DESCRIPTION.add("if current object is directory (d), readable (r), ");
		COMMAND_DESCRIPTION.add("writable (w) and executable (x). Second column contains object size in bytes");
		COMMAND_DESCRIPTION.add("that is right aligned and occupies 10 characters.");
		COMMAND_DESCRIPTION.add("Follows file creation date/time and finally file name.");
		COMMAND_DESCRIPTION.add("An example of output: \"-rw- 0 2008-09-02 12:59:31 ca.key\"");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Util.getArrayOfArgumentsThatIncludeStrings(arguments);

		if (args.length != 1) {
			env.writeln("Expected exactly one argument, " + args.length + " given.");
		} else {
			try {
				listDir(env, args[0]);
			} catch (IOException e) {
				env.write("There was a problem. Operation failed.");
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Lists all files (with details - see the documentation of this class) in
	 * the specified directory using the specified environment to write an
	 * output.
	 *
	 * @param env
	 *            an environment used to write an output
	 * @param directory
	 *            a directory whose contents are to be listed
	 * @throws IOException
	 *             if there is a problem reading the contents of the specified
	 *             directory
	 */
	private void listDir(Environment env, String directory) throws IOException {
		Path dirPath;
		try {
			dirPath = Paths.get(directory);
		} catch (InvalidPathException ex) {
			env.writeln("The specified path is not valid.");
			return;
		}
		if (!Files.isDirectory(dirPath)) {
			env.writeln("The specified path does not represent a directory.");
			return;
		}

		List<Path> paths = Files.list(dirPath).collect(Collectors.toList());
		for (Path path : paths) {
			env.write(Files.isDirectory(path) ? "d" : "-");
			env.write(Files.isReadable(path) ? "r" : "-");
			env.write(Files.isWritable(path) ? "w" : "-");
			env.write(Files.isExecutable(path) ? "x" : "-");
			env.write(String.format("%20d ", Files.size(path)));
			env.write(getFormattedDateAndTime(path) + " ");
			env.writeln(path.getFileName().toString());
		}
	}

	/**
	 * Gets the formatted date and time of creation of the file specified by
	 * it's path. If there is a problem with getting those details for the
	 * specified file, the following string is returned: "-".
	 *
	 * @param path
	 *            path of a file
	 * @return the formatted date and time of creation of the file specified by
	 *         it's path
	 */
	private String getFormattedDateAndTime(Path path) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes;
		try {
			attributes = faView.readAttributes();
		} catch (IOException e) {
			return "-";
		}
		FileTime fileTime = attributes.creationTime();
		return sdf.format(new Date(fileTime.toMillis()));
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
