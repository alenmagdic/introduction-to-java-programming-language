package hr.fer.zemris.java.p12.dao;

import java.util.List;
import java.util.Set;

import hr.fer.zemris.java.hw14.polls.Poll;
import hr.fer.zemris.java.hw14.polls.PollOption;

/**
 * An interface describing an object that enables access to data about polls.
 *
 * @author Alen Magdić
 *
 */
public interface DAO {

	/**
	 * Gets a list of polls.
	 *
	 * @return a list of polls
	 * @throws DAOException
	 *             if there is a problem getting the data
	 */
	public List<Poll> getPolls() throws DAOException;

	/**
	 * Gets a poll with the specified id.
	 *
	 * @param pollID
	 *            poll id
	 * @return a poll with the specified id
	 * @throws DAOException
	 *             if there is a problem getting the data
	 */
	public Poll getPoll(long pollID) throws DAOException;

	/**
	 * Updates the number of votes for the specified poll and the specified
	 * option. The method increments the number of votes for one vote. The poll
	 * and the poll option are specified through their ids.
	 *
	 * @param pollID
	 *            poll id
	 * @param optionID
	 *            option id
	 * @throws DAOException
	 *             if there is a problem getting the data
	 */
	public void updateVotes(long pollID, long optionID) throws DAOException;

	/**
	 * Gets the poll options for poll with the specified id.
	 *
	 * @param pollID
	 *            poll id
	 * @return poll options for poll with the specified id
	 * @throws DAOException
	 *             if there is a problem getting the data
	 */
	public Set<PollOption> getPollOptions(long pollID) throws DAOException;

	/**
	 * Gets the poll results for poll with the specified id. The results are
	 * sorted by number of votes descending.
	 *
	 * @param pollId
	 *            poll id
	 * @return poll results for poll with the specified id
	 * @throws DAOException
	 *             if there is a problem getting the data
	 */
	public Set<PollOption> getPollResults(long pollId) throws DAOException;
}