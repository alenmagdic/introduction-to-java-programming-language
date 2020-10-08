package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.hw14.polls.Poll;
import hr.fer.zemris.java.hw14.polls.PollOption;

/**
 * A context listener that initializes data used in this web application. It
 * connects to a database, checks if there are any data and if not, it creates
 * some default data (creates a table of polls and a table of options).
 *
 * @author Alen Magdić
 *
 */

@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Properties props = new Properties();
		Path propsPath = Paths.get(sce.getServletContext().getRealPath("./WEB-INF/dbsettings.properties"));
		try {
			props.load(Files.newBufferedReader(propsPath));
		} catch (IOException e) {
			throw new RuntimeException("Unable to obtain database settings data.");
		}

		String dbName = props.getProperty("name");
		String dbHost = props.getProperty("host");
		String dbPort = props.getProperty("port");
		String dbUser = props.getProperty("user");
		String dbUserPassword = props.getProperty("password");
		if (dbName == null || dbHost == null || dbPort == null || dbUser == null || dbUserPassword == null) {
			throw new RuntimeException("Unable to obtain database settings data.");
		}

		String connectionURL = "jdbc:derby://" + dbHost + ":" + dbPort + "/" + dbName + ";user=" + dbUser + ";password="
				+ dbUserPassword;

		ComboPooledDataSource cpds = new ComboPooledDataSource();

		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("There has been a problem initializating the connection pool.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		try {
			createDbTablesIfNotExist(cpds.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if tables Poll and PollOptions exist, and creates them if any of
	 * them does not exist in the database. It also fills them with some default
	 * data. If there is a table Poll, but empty, then it also fills that table
	 * with some default data and adds some option for that polls.
	 *
	 * @param connection
	 *            connection to the application's database
	 * @throws SQLException
	 *             if there is a problem with any of database actions
	 */
	private void createDbTablesIfNotExist(Connection connection) throws SQLException {
		if (!dbTableExists("Polls", connection)) {
			PreparedStatement st = connection.prepareStatement(
					"CREATE TABLE Polls (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(150) NOT NULL, message CLOB(2048) NOT NULL)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		}

		if (!dbTableExists("PollOptions", connection)) {
			PreparedStatement st = connection.prepareStatement(
					"CREATE TABLE PollOptions (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, optionTitle VARCHAR(100) NOT NULL, optionLink VARCHAR(150) NOT NULL, pollID BIGINT, votesCount BIGINT, FOREIGN KEY (pollID) REFERENCES Polls(id))");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		}

		if (isTableEmpty("Polls", connection)) {

			Poll[] polls = new Poll[] { new Poll("Favourite band", "What is your favourite band among the given?"),
					new Poll("Buying a car", "Which among the given cars would you like to buy the most?") };

			for (int i = 0; i < polls.length; i++) {
				PreparedStatement st = connection.prepareStatement("INSERT INTO Polls (title,message) VALUES('"
						+ polls[i].getTitle() + "','" + polls[i].getMessage() + "')", Statement.RETURN_GENERATED_KEYS);

				try {
					st.executeUpdate();
					ResultSet set = null;
					try {
						set = st.getGeneratedKeys();
						set.next();
						polls[i].setPollId(set.getLong(1));
					} finally {
						if (set != null) {
							set.close();
						}
					}
				} finally {
					st.close();
				}

			}

			PollOption[] poolOptions = new PollOption[] {
					new PollOption("The Beatles", "https://www.youtube.com/watch?v=z9ypq6_5bsg", polls[0].getPollId()),
					new PollOption("The Platters", "https://www.youtube.com/watch?v=H2di83WAOhU", polls[0].getPollId()),
					new PollOption("The Beach Boys", "https://www.youtube.com/watch?v=2s4slliAtQU",
							polls[0].getPollId()),
					new PollOption("The Four Seasons", "https://www.youtube.com/watch?v=y8yvnqHmFds",
							polls[0].getPollId()),
					new PollOption("The Marcels", "https://www.youtube.com/watch?v=qoi3TH59ZEs", polls[0].getPollId()),
					new PollOption("The Everly Brothers", "https://www.youtube.com/watch?v=tbU3zdAgiX8",
							polls[0].getPollId()),
					new PollOption("The Mamas And The Papas", "https://www.youtube.com/watch?v=N-aK6JnyFmk",
							polls[0].getPollId()),
					new PollOption("Ferrari LaFerrari",
							"http://wallpaper-gallery.net/images/laferrari-hd-wallpaper/laferrari-hd-wallpaper-20.jpg",
							polls[1].getPollId()),
					new PollOption("Rimac Concept_One",
							"http://wallpapersqq.net/wp-content/uploads/2016/06/rimac-concept-one-2016-hd-wallpaper-for-desktop.jpg",
							polls[1].getPollId()),
					new PollOption("Koenigsegg Regera", "https://www.walldevil.com/wallpapers/u11/regera.jpg",
							polls[1].getPollId()),
					new PollOption("Porsche 918", "http://wallpapercave.com/wp/YiWNNVv.jpg", polls[1].getPollId()),
					new PollOption("McLaren P1", "https://images8.alphacoders.com/705/705609.jpg",
							polls[1].getPollId()), };

			for (int i = 0; i < poolOptions.length; i++) {
				PollOption opt = poolOptions[i];
				PreparedStatement st = connection
						.prepareStatement("INSERT INTO PollOptions (optionTitle,optionLink,pollID,votesCount) VALUES('"
								+ opt.getTitle() + "','" + opt.getLink() + "'," + opt.getPollId() + ",0)");
				try {
					st.executeUpdate();
				} finally {
					st.close();
				}
			}
		}
	}

	/**
	 * Checks if the specified database table is empty.
	 *
	 * @param tableName
	 *            table name
	 * @param connection
	 *            connection to the application's database
	 * @return true if the specified table is empty
	 * @throws SQLException
	 *             if there is a problem with any of database actions
	 */
	private boolean isTableEmpty(String tableName, Connection connection) throws SQLException {
		PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName);
		try {
			ResultSet rs = st.executeQuery();
			rs.next();
			return rs.getInt(1) == 0;
		} finally {
			st.close();
		}
	}

	/**
	 * Checks if the specified database table exists.
	 *
	 * @param tableName
	 *            table name
	 * @param connection
	 *            connection to the application's database
	 * @return true if the specified table exists
	 * @throws SQLException
	 *             if there is a problem with any of database actions
	 */
	private boolean dbTableExists(String tableName, Connection connection) throws SQLException {
		DatabaseMetaData dbMetaData = connection.getMetaData();

		ResultSet rs = null;
		try {
			rs = dbMetaData.getTables(null, null, tableName.toUpperCase(), null);
			if (!rs.next()) {
				return false;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return true;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}