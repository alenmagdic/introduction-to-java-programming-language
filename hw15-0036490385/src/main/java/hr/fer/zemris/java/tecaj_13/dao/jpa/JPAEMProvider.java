package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;

/**
 * Klasa koja služi za višedretveni dohvat entity managera. Svakoj dretvi se
 * dodjeljuje njen entity manager.
 *
 * @author Alen Magdić
 *
 */
public class JPAEMProvider {
	/** Mapa koja pohranjuje entity managere za dretve. **/
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Dohvaća entity manager namjenjen dretvi koja poziva ovu metodu.
	 *
	 * @return entity manager namjenjen dretvi koja poziva ovu metodu
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if (ldata == null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Oslobađa resurse koje je zauzeo ovaj {@link JPAEMProvider}.
	 *
	 * @throws DAOException
	 *             ako dođe do problema pri oslobađanju resursa
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if (ldata == null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null) {
			throw dex;
		}
	}

	/**
	 * Klasa koja modelira lokalne podatke, odnosno podatke koje vrijede samo za
	 * jednu dretvu.
	 *
	 * @author Alen Magdić
	 *
	 */
	private static class LocalData {
		/** Entity manager **/
		EntityManager em;
	}

}