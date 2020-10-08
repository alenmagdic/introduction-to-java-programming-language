package hr.fer.zemris.java.hw04.db;

/**
 * This class represents a student record. It contains the following attributes:
 * jmbag, last name, first name and a final grade. It has it's own
 * implementation of hashCode() and equals() methods so that they can be used as
 * a part of any collection. It provides getters for all the attributes.
 *
 * @author Alen Magdić
 *
 */
public class StudentRecord {
	/** Student's Jmbag **/
	private String jmbag;
	/** Last name of the student **/
	private String lastName;
	/** First name of the student **/
	private String firstName;
	/** Student's final grade **/
	private int finalGrade;

	/**
	 * Constructor.
	 *
	 * @param jmbag
	 *            student's jmbag
	 * @param lastName
	 *            last name of the student
	 * @param firstName
	 *            first name of the student
	 * @param finalGrade
	 *            student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		super();
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (jmbag == null ? 0 : jmbag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null) {
				return false;
			}
		} else if (!jmbag.equals(other.jmbag)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the student's jmbag
	 *
	 * @return student's jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Gets the student's first name
	 *
	 * @return first name of the student
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the student's last name
	 *
	 * @return last name of the student
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the student's final grade
	 *
	 * @return student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
}
