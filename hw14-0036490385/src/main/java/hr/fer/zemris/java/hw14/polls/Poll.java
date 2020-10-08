package hr.fer.zemris.java.hw14.polls;

/**
 * This class represents a poll, i.e. it contains poll data like poll title,
 * poll message and the poll id.
 *
 * @author Alen Magdić
 *
 */
public class Poll {
	/** Poll title **/
	private String title;
	/** Poll message **/
	private String message;
	/** Poll id **/
	private long pollId;

	/**
	 * Constructor.
	 *
	 * @param title
	 *            poll title
	 * @param message
	 *            poll message
	 */
	public Poll(String title, String message) {
		this(0, title, message);
	}

	/**
	 * Constructor.
	 *
	 * @param pollId
	 *            poll id
	 * @param title
	 *            poll title
	 * @param message
	 *            poll message
	 */
	public Poll(long pollId, String title, String message) {
		super();
		this.title = title;
		this.message = message;
		this.pollId = pollId;
	}

	/**
	 * Returns poll id.
	 *
	 * @return poll id
	 */
	public long getPollId() {
		return pollId;
	}

	/**
	 * Returns poll title.
	 *
	 * @return poll title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns poll message.
	 *
	 * @return poll message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets poll id.
	 *
	 * @param pollId
	 *            poll id
	 */
	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

}
