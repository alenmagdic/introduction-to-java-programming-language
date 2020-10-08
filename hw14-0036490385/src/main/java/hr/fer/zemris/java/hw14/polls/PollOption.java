package hr.fer.zemris.java.hw14.polls;

/**
 * This class represents a poll option. It contains option link, a link to some
 * information about that option, id of the poll to which this option belongs,
 * number of votes for this option and the option id.
 *
 * @author Alen Magdić
 *
 */

public class PollOption implements Comparable<PollOption> {
	/** Option title **/
	private String title;
	/** A link to some information about this title **/
	private String link;
	/** Id of the poll to which this option belongs **/
	private long pollId;
	/** Number of votes for this option **/
	private int votesCount;
	/** Option id **/
	private long id;

	/**
	 * Constructor.
	 *
	 * @param title
	 *            option title
	 * @param link
	 *            a link to some information about this title
	 * @param pollId
	 *            id of the poll to which this option belongs
	 */
	public PollOption(String title, String link, long pollId) {
		this(title, link, pollId, 0, 0);
	}

	/**
	 * Constructor.
	 *
	 * @param title
	 *            option title
	 * @param link
	 *            a link to some information about this title
	 * @param pollId
	 *            id of the poll to which this option belongs
	 * @param votesCount
	 *            number of votes for this option
	 * @param id
	 *            option id
	 */
	public PollOption(String title, String link, long pollId, int votesCount, long id) {
		super();
		this.title = title;
		this.link = link;
		this.pollId = pollId;
		this.votesCount = votesCount;
		this.id = id;
	}

	/**
	 * Returns option title.
	 *
	 * @return option title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the link to some information about this option.
	 *
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Returns the id of the poll to which this option belongs.
	 *
	 * @return poll id
	 */
	public long getPollId() {
		return pollId;
	}

	/**
	 * Returns number of votes for this option.
	 *
	 * @return
	 */
	public int getVotesCount() {
		return votesCount;
	}

	/**
	 * Returns option id.
	 *
	 * @return
	 */
	public long getId() {
		return id;
	}

	@Override
	public int compareTo(PollOption o) {
		return (int) (id - o.id);
	}

}
