package hr.fer.zemris.java.hw04.db;

/**
 * An interface that represents a filter of student records. It defines a single
 * method whose task is to check if the specified record is acceptable or not.
 * If a record is acceptable, it means that it should be contained in a
 * resulting collection of the filtration, while a record that is not acceptable
 * should not be a part of the resulting collection of the filtration.
 *
 * @author Alen Magdić
 *
 */
public interface IFilter {
	/**
	 * Checks if the specified record is acceptable or not, i.e. it checks if
	 * the record should be contained in a resulting collection of filtration.
	 *
	 * @param record
	 *            a record that is to be checked
	 * @return true if the specified record should be contained in a resulting
	 *         collection of filtration
	 */
	public boolean accepts(StudentRecord record);

}
