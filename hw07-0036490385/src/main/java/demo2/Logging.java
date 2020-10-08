package demo2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A demonstration of logging. Prints log messages of various levels.
 *
 * @author Alen Magdić
 *
 */
public class Logging {
	/** Logger **/
	private static final Logger LOG = Logger.getLogger("demo2");

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Level[] levels = new Level[] { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE, Level.FINER,
				Level.FINEST };
		for (Level l : levels) {
			LOG.log(l, "This is a message of level " + l + ".");
		}
	}
}
