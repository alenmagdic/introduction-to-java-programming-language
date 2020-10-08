package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Sučelje koje predstavlja objekt za dohvat podataka za blog.
 *
 * @author Alen Magdić
 *
 */
public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 *
	 * @param id
	 *            ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Dohvaća listu svih registriranih autora.
	 *
	 * @return lista svih registriranih autora
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public List<BlogUser> getAuthors() throws DAOException;

	/**
	 * Dohvaća autora sa zadanim id-om.
	 *
	 * @param id
	 *            id autora kojeg treba dohvatiti
	 * @return autor sa zadanim id-om
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogUser getAuthor(Long id) throws DAOException;

	/**
	 * Dohvaća autora sa zadanim nickom.
	 *
	 * @param nick
	 *            nick autora kojeg treba dohvatiti
	 * @return autor sa zadanim nickom
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogUser getAuthorByNick(String nick) throws DAOException;

	/**
	 * Dodaje zadanog korisnika.
	 *
	 * @param user
	 *            korisnik kojeg treba dodati
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public void addNewUser(BlogUser user) throws DAOException;

	/**
	 * Dohvaća sve zapise korisnika sa zadanim nickom.
	 *
	 * @param nick
	 *            nick korisnika čije je zapise potrebno dohvatiti
	 * @return zapisi korisnika sa zadanim nickom
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public List<BlogEntry> getUserEntries(String nick) throws DAOException;

	/**
	 * Dodaje zadani zapis.
	 *
	 * @param entry
	 *            zapis koji je potrebno dodati
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public void addNewEntry(BlogEntry entry) throws DAOException;

	/**
	 * Dodaje zadani komentar.
	 *
	 * @param comment
	 *            komentar koji je potrebno dodati
	 * @throws DAOException
	 *             ako dođe do pogreške pri dohvatu podataka
	 */
	public void addNewComment(BlogComment comment) throws DAOException;

}