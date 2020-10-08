package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Implementacija {@link DAO} objekta uporabom tehnologije JPA.
 *
 * @author Alen Magdić
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getAuthor(Long id) {
		BlogUser author = JPAEMProvider.getEntityManager().find(BlogUser.class, id);
		return author;
	}

	@Override
	public List<BlogUser> getAuthors() {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogUser> authors = em.createQuery("select BlogUser from BlogUser as BlogUser").getResultList();
		return authors;
	}

	@Override
	public BlogUser getAuthorByNick(String nick) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogUser> author = em.createQuery("select user from BlogUser as user where user.nick = :ni")
				.setParameter("ni", nick).getResultList();
		return author.size() == 0 ? null : author.get(0);
	}

	@Override
	public void addNewUser(BlogUser user) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(user);
	}

	@Override
	public List<BlogEntry> getUserEntries(String nick) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogEntry> entries = em.createQuery("select ent from BlogEntry as ent where ent.creator.nick = :ni")
				.setParameter("ni", nick).getResultList();
		return entries;
	}

	@Override
	public void addNewEntry(BlogEntry entry) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(entry);
	}

	@Override
	public void addNewComment(BlogComment comment) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(comment);
		comment.getBlogEntry().getComments().add(comment);
	}

}