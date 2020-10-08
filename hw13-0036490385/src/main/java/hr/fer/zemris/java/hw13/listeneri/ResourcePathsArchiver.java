package hr.fer.zemris.java.hw13.listeneri;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * A {@link ServletContextListener} that holds information about resource paths.
 * More precisely, it holds information about real paths of files containing
 * poll information.
 *
 * @author Alen Magdić
 *
 */
@WebListener
public class ResourcePathsArchiver implements ServletContextListener {
	/**
	 * Path of file defining a poll, i.e. containing information about poll
	 * candidates.
	 */
	public static Path POLL_DEFINITION_FILE_PATH;
	/**
	 * Path of file containing poll results.
	 */
	public static Path POLL_RESULTS_FILE_PATH;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		POLL_DEFINITION_FILE_PATH = Paths.get(sce.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
		POLL_RESULTS_FILE_PATH = Paths.get(sce.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
