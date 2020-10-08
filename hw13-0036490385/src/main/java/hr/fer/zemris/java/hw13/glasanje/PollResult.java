package hr.fer.zemris.java.hw13.glasanje;

/**
 * This class represents a poll result of poll for the favourite music band.
 *
 * @author Alen Magdić
 *
 */
public class PollResult {
	/** A music band that is a candidate for the favourite music band. **/
	private Band band;
	/**
	 * Number of votes for the music band represented by this VoteResult.
	 */
	private int votes;

	/**
	 * Constructor.
	 *
	 * @param band
	 *            a music band that is a candidate for the favourite music band
	 * @param votes
	 *            number of votes for the music band represented by this
	 *            VoteResult
	 */
	public PollResult(Band band, int votes) {
		super();
		this.band = band;
		this.votes = votes;
	}

	/**
	 * Gets the music band represented by this object.
	 *
	 * @return the music band represented by this VoteResult
	 */
	public Band getBand() {
		return band;
	}

	/**
	 * Gets the number of votes for the music band represented by this
	 * VoteResult
	 *
	 * @return
	 */
	public int getVotes() {
		return votes;
	}

	@Override
	public String toString() {
		return band.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (band == null ? 0 : band.hashCode());
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
		PollResult other = (PollResult) obj;
		if (band == null) {
			if (other.band != null) {
				return false;
			}
		} else if (!band.equals(other.band)) {
			return false;
		}
		return true;
	}
}
