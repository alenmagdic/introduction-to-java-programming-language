package hr.fer.zemris.java.hw05.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This program loads data about students from a text file and then prints lists
 * of students that satisfy some conditions. Data about a single student are
 * stored in {@link StudentRecord} object.
 *
 * @author Alen Magdić
 *
 */
public class StudentDemo {

	/**
	 * This method is the starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		List<StudentRecord> records;
		try {
			records = loadDatabase();
		} catch (IOException e) {
			System.out.println("There was a problem loading the database. The program can not proceed.");
			return;
		}

		System.out.println("Number of students with more than 25 points: " + vratiBodovaViseOd25(records));
		System.out.println("Number of excellent (grade 5) students: " + vratiBrojOdlikasa(records));

		System.out.println("\nExcellent students: ");
		printStudents(vratiListuOdlikasa(records));

		System.out.println("\nExcellent students sorted by points: ");
		printStudents(vratiSortiranuListuOdlikasa(records), true);

		System.out.println("\nJmbag's of students that failed the class: ");
		for (String jmbag : vratiPopisNepolozenih(records)) {
			System.out.println(jmbag);
		}

		System.out.println("\nDistribution of students by grades: ");
		Map<Integer, Integer> mapOfGrades = vratiBrojStudenataPoOcjenama(records);
		Map<Integer, List<StudentRecord>> mapOfStudentsByGrades = razvrstajStudentePoOcjenama(records);
		for (int grade = 1; grade <= 5; grade++) {
			System.out.printf("Grade %d (%d students):\n", grade, mapOfGrades.get(grade));
			printStudents(mapOfStudentsByGrades.get(grade));
			System.out.println("\n");
		}

		Map<Boolean, List<StudentRecord>> mapOfPassedAndFailed = razvrstajProlazPad(records);
		System.out.println("\nStudents that failed the class: ");
		printStudents(mapOfPassedAndFailed.get(false));

		System.out.println("\nStudents that passed the class: ");
		printStudents(mapOfPassedAndFailed.get(true));
	}

	/**
	 * Prints the specified list of student records. It prints student's jmbag
	 * and students name. It can optionally print the total number of points
	 * that each student earned. *
	 *
	 * @param records
	 *            a list of students
	 * @param printPoints
	 *            true if the method should print the total number of points
	 *            that each student earned
	 */
	private static void printStudents(List<StudentRecord> records, boolean printPoints) {
		System.out.println((printPoints ? "Points -" : "") + "Jmbag - Name");
		for (StudentRecord rec : records) {
			if (printPoints) {
				System.out.printf("%2f %s %s %s\n", rec.getTotalPoints(), rec.getJmbag(), rec.getLastName(),
						rec.getFirstName());
			} else {
				System.out.printf("%s %s %s\n", rec.getJmbag(), rec.getLastName(), rec.getFirstName());
			}
		}
	}

	/**
	 * Prints the specified list of student records. It prints student's jmbag
	 * and student name.
	 *
	 * @param records
	 *            a list of students
	 */
	private static void printStudents(List<StudentRecord> records) {
		printStudents(records, false);
	}

	/**
	 * This method takes a list of student records as a parameter and gets the
	 * number of students, from that list, that earned more than 25 points.
	 *
	 * @param records
	 *            a list of student records
	 * @return the number of students that earned more than 25 points
	 */
	public static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream().filter(record -> record.getTotalPoints() > 25).count();
	}

	/**
	 * This method takes a list of student records as a parameter and gets the
	 * number of excellent (grade 5) students.
	 *
	 * @param records
	 *            a list of student records
	 * @return the number of excellent students
	 */
	public static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(record -> record.getFinalGrade() == 5).count();
	}

	/**
	 * This metod takes a list of student records as a parameter and returns a
	 * list of student records that contains only the excellent students.
	 *
	 * @param records
	 *            a list of student records
	 * @return a list of student records that contains only the excellent
	 *         students
	 */
	public static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(record -> record.getFinalGrade() == 5).collect(Collectors.toList());
	}

	/**
	 * This method takes a list of student records as a parameter and returns a
	 * list of student records that contains only the excellent students sorted
	 * by their points so that the best student is at the first place in the
	 * list.
	 *
	 * @param records
	 *            a list of student records
	 * @return a list of student records that contains only the excellent
	 *         students sorted by their points so that the best student is at
	 *         the first place in the list
	 */
	public static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(record -> record.getFinalGrade() == 5)
				.sorted((rec1, rec2) -> Double.compare(rec2.getTotalPoints(), rec1.getTotalPoints()))
				.collect(Collectors.toList());
	}

	/**
	 * This method takes a list of student records as a parameter and returns a
	 * list of jmbag's of students that failed the class.
	 *
	 * @param records
	 *            a list of student records
	 * @return a list of jmbag's of students that failed the class
	 */
	public static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream().filter(record -> record.getFinalGrade() == 1).map(record -> record.getJmbag()).sorted()
				.collect(Collectors.toList());
	}

	/**
	 * This method takes a list of student records as a parameter and returns a
	 * map with grades as keys and lists of student records as values.
	 *
	 * @param records
	 *            a list of student records
	 * @return a map with grades as keys and lists of student records as values
	 */
	public static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.groupingBy(record -> ((StudentRecord) record).getFinalGrade()));
	}

	/**
	 * This method takes a list of student records as a parameter and returns a
	 * map where a key is a grade, and a value is the number of students with
	 * that grade.
	 *
	 * @param records
	 *            a list of student records
	 * @return a map where a key is a grade, and a value is the number of
	 *         students with that grade
	 */
	public static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.toMap(record -> ((StudentRecord) record).getFinalGrade(),
				record -> 1, (val1, val2) -> val1 + val2));
	}

	/**
	 * This method takes a list of student records as a parameter and returns a
	 * map where a key is a boolean value. An entry whose key is equal to true,
	 * contains a list of student records of students that passed the class. An
	 * entry whose key is equal to false, contains a list of student records of
	 * students that failed the class.
	 *
	 * @param records
	 *            a list of student records
	 * @return a map where a key is a boolean value, and a value is a list of
	 *         student records of students that passed/failed the class
	 */
	public static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream().collect(Collectors.partitioningBy(record -> record.getFinalGrade() != 1));
	}

	/**
	 * Loads data from a text file. If loading does not succeed, an
	 * {@link IOException} is thrown.
	 *
	 * @return list of student records loaded from a text file
	 * @throws IOException
	 *             if loading does not succeed
	 */
	public static List<StudentRecord> loadDatabase() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./studenti.txt"), StandardCharsets.UTF_8);
		List<StudentRecord> records = new ArrayList<>();

		for (String line : lines) {
			if (line.trim().equals("")) {
				continue;
			}

			String[] attributes = line.split("\t");

			if (attributes.length != 7) {
				throw new IOException();
			}

			try {
				String jmbag = attributes[0];
				String lastName = attributes[1];
				String firstName = attributes[2];
				double midtermExamResult = Double.parseDouble(attributes[3]);
				double finalExamResult = Double.parseDouble(attributes[4]);
				double labExerciseResult = Double.parseDouble(attributes[5]);
				int finalGrade = Integer.parseInt(attributes[6]);

				records.add(new StudentRecord(jmbag, lastName, firstName, midtermExamResult, finalExamResult,
						labExerciseResult, finalGrade));
			} catch (NumberFormatException ex) {
				throw new IOException(ex);
			}
		}

		return records;
	}
}
