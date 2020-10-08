package hr.fer.zemris.java.hw13.listeneri;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * A {@link ServletContextListener} that stores information about this web
 * application. More precisely, this listener stores only one piece of
 * information: time when this app was started. This information is stored to
 * {@link ServletContext} like attribute with name "startuptime".
 *
 * @author Alen Magdić
 *
 */
@WebListener
public class AppInfoListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("startuptime", System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
