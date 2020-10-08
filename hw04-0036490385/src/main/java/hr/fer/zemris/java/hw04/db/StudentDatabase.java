package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

/**
 * This class represents a student database. It contains a method for filtering
 * data from the database using the specified filter, and a method that can get
 * a record from the database in O(1) complexity using the specified Jmbag.
 *
 * @author Alen Magdić
 *
 */
public class StudentDatabase {
	/**
	 * List of records in this database.
	 */
	private List<StudentRecord> listOfRecords;
	/**
	 * Map of records in this database.
	 */
	private SimpleHashtable<String, StudentRecord> mapOfRecords;

	/**
	 * Constructor.
	 *
	 * @param rows
	 *            list of rows containing the database data
	 */
	public StudentDatabase(List<String> rows) {
		createListAndMapOfRecords(rows);
	}

	/**
	 * A method that creates a list and a map of records from the database
	 *
	 * @param rows
	 *            list of rows containing the database data
	 */
	private void createListAndMapOfRecords(List<String> rows) {
		listOfRecords = new ArrayList<>();
		mapOfRecords = new SimpleHashtable<>();

		for (String row : rows) {
			String[] attributes = row.split("\t");
			if (attributes.length != 4) {
				throw new RuntimeException("Invalid data found. Data: " + row);
			}

			String jmbag = attributes[0];
			System.out.println(jmbag.equals("0000000001"));
			if (mapOfRecords.containsKey(jmbag)) {
				throw new RuntimeException("There are at least two entries with the same jmbag. Jmbag: " + jmbag);
			}

			String lastName = attributes[1];
			String firstName = attributes[2];
			int finalGrade;
			try {
				finalGrade = Integer.parseInt(attributes[3]);
			} catch (NumberFormatException ex) {
				throw new NumberFormatException(
						"Invalid data found. The value '" + attributes[3] + "' can not be a grade.");
			}

			StudentRecord record = new StudentRecord(jmbag, lastName, firstName, finalGrade);
			listOfRecords.add(record);
			mapOfRecords.put(jmbag, record);
		}
	}

	/**
	 * Gets a record from the database with the specified jmbag in O(1)
	 * complexity.
	 *
	 * @param jmbag
	 *            jmbag of the record to be retrieved
	 * @return retrieved record
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return mapOfRecords.get(jmbag);
	}

	/**
	 * Gets a list of records from the database that are accepted by the
	 * specified filter.
	 *
	 * @param filter
	 *            filter using to determine if a record should be added to the
	 *            resulting list of filtration
	 * @return list of records that are accepted by the specified filter
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> resultingList = new ArrayList<>();

		for (StudentRecord record : listOfRecords) {
			if (filter.accepts(record)) {
				resultingList.add(record);
			}
		}
		return resultingList;
	}

}
