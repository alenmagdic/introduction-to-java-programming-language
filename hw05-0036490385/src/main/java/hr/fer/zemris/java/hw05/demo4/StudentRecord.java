package hr.fer.zemris.java.hw05.demo4;

/**
 * This class represents a record of a single student. It contains student's
 * data and his achievement in some unspecified class. *
 *
 * @author Alen Magdić
 *
 */
public class StudentRecord {
	/** Student's jmbag. **/
	private String jmbag;
	/** Student's last name. **/
	private String lastName;
	/** Student's first name. **/
	private String firstName;
	/** Student's result achieved at midterm exam. **/
	private double midtermExamResult;
	/** Student's result achieved at final exam. **/
	private double finalExamResult;
	/** Student's result achieved at laboratory exercises. **/
	private double laboratoryExerciseResult;
	/** Student's final grade. **/
	private int finalGrade;

	/**
	 * Constructor.
	 *
	 * @param jmbag
	 *            student's jmbag
	 * @param lastName
	 *            student's last name
	 * @param firstName
	 *            student's first name
	 * @param midtermExamResult
	 *            student's result achieved at midterm exam
	 * @param finalExamResult
	 *            student's result achieved at final exam.
	 * @param laboratoryExerciseResult
	 *            student's result achieved at laboratory exercises
	 * @param finalGrade
	 *            student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, double midtermExamResult,
			double finalExamResult, double laboratoryExerciseResult, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.midtermExamResult = midtermExamResult;
		this.finalExamResult = finalExamResult;
		this.laboratoryExerciseResult = laboratoryExerciseResult;
		this.finalGrade = finalGrade;
	}

	/**
	 * Gets student's jmbag.
	 *
	 * @return student's jmabg
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Gets student's last name.
	 *
	 * @return student's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets student's first name.
	 *
	 * @return student's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets student's result achieved at midterm exam.
	 *
	 * @return student's result achieved at midterm exam
	 */
	public double getMidtermExamResult() {
		return midtermExamResult;
	}

	/**
	 * Gets student's result achieved at final exam.
	 *
	 * @return student's result achieved at final exam
	 */
	public double getFinalExamResult() {
		return finalExamResult;
	}

	/**
	 * Gets student's result achieved at laboratory exercises.
	 *
	 * @return student's result achieved at laboratory exercises
	 */
	public double getLaboratoryExerciseResult() {
		return laboratoryExerciseResult;
	}

	/**
	 * Gets student's final grade.
	 *
	 * @return student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Gets the total number of points that student has achieved. It is a sum of
	 * results from the midterm exam, the final exam and from the laboratory
	 * exercises.
	 *
	 * @return the total number of points that student has achieved
	 */
	public double getTotalPoints() {
		return midtermExamResult + finalExamResult + laboratoryExerciseResult;
	}

}
