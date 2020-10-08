package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Klasa koja pohranjuje tvornicu entity managera i omogućuje njen dohvat.
 *
 * @author Alen Magdić
 *
 */
public class JPAEMFProvider {

	/** Tvornica entity managera **/
	public static EntityManagerFactory emf;

	/** Konstruktor. **/
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Postavlja tvornicu entity managera.
	 *
	 * @param emf
	 *            tvornica entity managera
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}