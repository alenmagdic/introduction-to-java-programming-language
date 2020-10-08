package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.java.hw14.polls.Poll;
import hr.fer.zemris.java.hw14.polls.PollOption;
import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 *
 * @author marcupic
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() throws DAOException {
		return getPollsWithCondition(null);
	}

	/**
	 * Gets a list of polls that satisfy the specified condition. The condition
	 * can be null - in that case all polls from the database will be returned.
	 *
	 * @param condition
	 *            a condition which a poll has to satisfy in order to be
	 *            included in the list that is to be returned
	 * @return a list of polls satisfying the specified condition
	 * @throws DAOException
	 *             if there is a problem getting the polls from database
	 */
	private List<Poll> getPollsWithCondition(String condition) throws DAOException {
		Connection conn = SQLConnectionProvider.getConnection();
		List<Poll> polls = new ArrayList<>();

		try (PreparedStatement st = conn.prepareStatement(
				"SELECT id,title,message FROM Polls" + (condition != null ? " WHERE " + condition : ""));
				ResultSet rs = st.executeQuery();) {
			while (rs.next()) {
				polls.add(new Poll(rs.getLong(1), rs.getString(2), rs.getString(3)));
			}
		} catch (SQLException ex) {
			throw new DAOException("There has been a problem getting the polls data from the database.");
		}

		return polls;
	}

	@Override
	public void updateVotes(long pollID, long optionID) throws DAOException {
		Connection conn = SQLConnectionProvider.getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE PollOptions SET votesCount=votesCount+1 where pollID = " + pollID
					+ " and id = " + optionID);
			st.executeUpdate();
		} catch (SQLException ex) {
			throw new DAOException("There has been a problem updating the polls data in the database.");
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException ignorable) {
				}
			}
		}
	}

	@Override
	public Set<PollOption> getPollOptions(long pollId) throws DAOException {
		return getPollOptions(pollId, "optionTitle");
	}

	@Override
	public Set<PollOption> getPollResults(long pollId) throws DAOException {
		return getPollOptions(pollId, "votesCount DESC, optionTitle");
	}

	/**
	 * Gets poll options for the specified poll (specified by it's id) ordered
	 * by the specified order description (for example: 'votesCount DESC,
	 * optionTitle').
	 *
	 * @param pollId
	 *            poll id
	 * @param orderDescription
	 *            options order description
	 * @return poll options for the specified poll ordered by the specified
	 *         order description
	 */
	private Set<PollOption> getPollOptions(long pollId, String orderDescription) {
		Connection conn = SQLConnectionProvider.getConnection();
		Set<PollOption> options = new LinkedHashSet<>();

		try (PreparedStatement st = conn
				.prepareStatement("SELECT id,optionTitle,optionLink,votesCount FROM PollOptions where pollID = "
						+ pollId + " order by " + orderDescription);
				ResultSet rs = st.executeQuery();) {
			while (rs.next()) {
				options.add(new PollOption(rs.getString(2), rs.getString(3), pollId, rs.getInt(4), rs.getLong(1)));
			}
		} catch (SQLException ex) {
			throw new DAOException("There has been a problem getting the polls data from the database.");
		}

		return options;
	}

	@Override
	public Poll getPoll(long pollID) throws DAOException {
		List<Poll> polls = getPollsWithCondition("id = " + pollID);
		if (polls.size() == 0) {
			return null;
		}
		return polls.get(0);
	}

}