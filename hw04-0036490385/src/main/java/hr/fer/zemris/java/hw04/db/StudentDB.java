package hr.fer.zemris.java.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program is a database emulator. It takes multiple user inputs while
 * every input represents a single query. If the input is not valid, the program
 * will print an appropriate message to the screen. Otherwise, it will print a
 * table including all the records from the database that satisfy the condition
 * specified in a query from the user input. It will also print the number of
 * records selected. To exit the program, input 'exit'. The program supports the
 * following operators: <,>,<=,>=,=,!=,LIKE. Logical operator AND is the only
 * supported logical operator and it is case insensitive. The database consists
 * of only one relation with three attributes: jmbag, lastName, firstName, grade
 * but the attribute grade can not be used in a query.
 *
 * @author Alen Magdić
 *
 */

public class StudentDB {
	/**
	 * Path to the database file.
	 */
	private static final String DATABASE_PATH = "./src/main/resources/database.txt";

	/**
	 * This is the main method which is the starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(DATABASE_PATH), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("There was a problem loading the database. The program can not proceed.");
			return;
		}
		StudentDatabase database;
		try {
			database = new StudentDatabase(lines);
		} catch (RuntimeException ex) {
			System.out.println(
					"There was a problem loading the database. The database is corrupted. The program can not proceed.");
			return;
		}

		interactWithUser(database);
	}

	/**
	 * This is actually the main part of the program which interacts with a
	 * user. It reads the user input, parses it and prints the specified
	 * selection (specified by the user input) from the database to the screen.
	 *
	 *
	 * @param database
	 *            the database from which the records are to be selected
	 */
	private static void interactWithUser(StudentDatabase database) {
		Scanner scan = new Scanner(System.in);

		System.out.println(
				"Input a query and press enter to get the specified selection (specified by the query) from the database. Input 'exit' in order to quit the program.");
		while (true) {
			System.out.print("> ");
			String input = scan.nextLine().trim();

			if (input.equals("exit")) {
				System.out.println("Goodbye!");
				scan.close();
				return;
			}
			if (input.equals("query")) {
				System.out.println("Incomplete query!");
				continue;
			}
			if (!input.startsWith("query ")) {
				System.out.println("Unknown command! The only supported command is 'query'.");
				continue;
			}

			String inputWithoutTheQueryCommand = input.substring("query".length());
			QueryParser parser;
			try {
				parser = new QueryParser(inputWithoutTheQueryCommand);
			} catch (ParserException ex) {
				System.out.println(ex.getMessage());
				continue;
			}

			List<StudentRecord> records = selectDataFromTheDatabase(parser, database);
			printDataToTheScreen(records);
		}
	}

	/**
	 * This method prints all the records from the given list to the screen.
	 *
	 * @param records
	 *            list of records that are to be printed to the screen
	 */
	private static void printDataToTheScreen(List<StudentRecord> records) {
		if (records.size() == 0) {
			System.out.println("Records selected: 0");
			return;
		}

		int jmbagLen = getLengthOfTheLongestString(records, FieldValueGetters.JMBAG) + 2;
		int lNameLen = getLengthOfTheLongestString(records, FieldValueGetters.LAST_NAME) + 2;
		int fNameLen = getLengthOfTheLongestString(records, FieldValueGetters.FIRST_NAME) + 2;
		int gradeLen = 3;

		printBorderOfTheTable(jmbagLen, lNameLen, fNameLen, gradeLen);

		for (StudentRecord record : records) {
			printRecordToTable(record, jmbagLen, lNameLen, fNameLen, gradeLen);
		}

		printBorderOfTheTable(jmbagLen, lNameLen, fNameLen, gradeLen);
		System.out.println("Records selected: " + records.size());
	}

	/**
	 * This method prints the specified record as a row of the table specified
	 * by the given column widths.
	 *
	 * @param record
	 *            a record that is to be printed to the table of specified
	 *            dimensions
	 * @param columnWidths
	 *            variable number of arguments defining widths of every column
	 *            of the table
	 */
	private static void printRecordToTable(StudentRecord record, int... columnWidths) {
		System.out.print("|");
		// the next line of code is similiar to "printf("%-20s",string)" which
		// would print the string aligned to the left and using the 20 places of
		// space
		System.out.printf(" %-" + (columnWidths[0] - 1) + "s", record.getJmbag());
		System.out.print("|");
		System.out.printf(" %-" + (columnWidths[1] - 1) + "s", record.getLastName());
		System.out.print("|");
		System.out.printf(" %-" + (columnWidths[2] - 1) + "s", record.getFirstName());
		System.out.print("|");
		System.out.printf(" %-" + (columnWidths[3] - 1) + "s", record.getFinalGrade());
		System.out.println("|");

	}

	/**
	 * This method prints the border line of the table. It prints something like
	 * "+===+===+=====+" where char '+' represents a border between some two
	 * columns. The method takes a variable number of arguments which represents
	 * the widths of columns.
	 *
	 * @param columnWidths
	 *            widths of columns
	 */
	private static void printBorderOfTheTable(int... columnWidths) {
		for (int columnWidth : columnWidths) {
			System.out.print("+");
			printMultipleTimes("=", columnWidth);
		}
		System.out.println("+");
	}

	/**
	 * This method prints the specified string the specified number of times.
	 * The new line is printed only if it is a part of the specified string,
	 * otherwise it will all be printed in the same line.
	 *
	 * @param string
	 *            the string that is to be printed
	 * @param times
	 *            the number of times to print the specified string
	 */
	private static void printMultipleTimes(String string, int times) {
		for (int i = 0; i < times; i++) {
			System.out.print(string);
		}

	}

	/**
	 * This method returns the longest string from the indirectly specified
	 * collection of strings. That collection of strings is specified with a
	 * list of student records and with a field value getter. So, for example,
	 * if there is a field value getter for the last name, this method will
	 * return the length of the longest last name from the specified list of
	 * records.
	 *
	 * @param records
	 *            list of student records
	 * @param fieldValueGetter
	 *            a field value getter for the records of the specified list of
	 *            student records
	 * @return the length of the longest string from the indirectly specified
	 *         collection of strings, i.e. the collection that is specified by
	 *         the given arguments
	 */
	private static int getLengthOfTheLongestString(List<StudentRecord> records, IFieldValueGetter fieldValueGetter) {
		if (records.size() == 0) {
			return 0;
		}

		int max = 0;
		for (StudentRecord record : records) {
			int length = fieldValueGetter.get(record).length();
			if (length > max) {
				max = length;
			}
		}
		return max;
	}

	/**
	 * This method returns a list of records from the specified database that
	 * satisfy the conditions specified in a query that has been parsed by the
	 * specified parser.
	 *
	 * @param parser
	 *            a parser that contains the conditions for the data selection
	 * @param database
	 *            the database from which records are to be selected
	 * @return a list of student records that satisfy the specified conditions
	 */
	private static List<StudentRecord> selectDataFromTheDatabase(QueryParser parser, StudentDatabase database) {
		List<StudentRecord> records = new ArrayList<>();

		if (parser.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");
			StudentRecord record = database.forJMBAG(parser.getQueriedJMBAG());
			if (record == null) {
				return records;
			}
			records.add(record);
		} else {
			return database.filter(new QueryFilter(parser.getQuery()));
		}
		return records;
	}
}
