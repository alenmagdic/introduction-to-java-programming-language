package hr.fer.zemris.java.hw04.db;

/**
 * An interface that represents a field value getter. It extracts a field value
 * from the specified student record. The field whose value is to be extracted
 * is defined by an implementation of this interface.
 *
 * @author Alen Magdić
 *
 */
public interface IFieldValueGetter {

	/**
	 * Extracts a field value from the specified student record. The field whose
	 * value is to be extracted is defined by an implementation of this
	 * interface.
	 *
	 * @param record
	 *            student record whose field value is to be extracted
	 * @return a field value from the specified record
	 */
	public String get(StudentRecord record);

}
