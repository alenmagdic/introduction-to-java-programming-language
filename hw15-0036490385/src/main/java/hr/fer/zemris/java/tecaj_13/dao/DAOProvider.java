package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/**
 * Singleton klasa kojom se dohvaća {@link DAO}.
 *
 * @author Alen Magdić
 *
 */
public class DAOProvider {
	/** Objekt preko kojeg se pristupa podacima **/
	private static DAO dao = new JPADAOImpl();

	/**
	 * Dohvaća {@link DAO}.
	 *
	 * @return {@link DAO}
	 */
	public static DAO getDAO() {
		return dao;
	}

}