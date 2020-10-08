package hr.fer.zemris.java.tecaj_13.console;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;

/**
 * Primjer rada sa bazom podataka za blog.
 *
 * @author Alen Magdić
 *
 */
public class Example1 {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.blog");

		// 1. korak - stvaranje novog blog zapisa...
		// -----------------------------------------
		System.out.println("Dodajem blog zapis.");
		BlogEntry blogEntry = dodajZapis(emf);
		System.out.println("Dodajem blog zapis - gotovo.");

		Long blogEntryID = blogEntry.getId();

		// 2. korak - dodavanje dva komentara...
		// -----------------------------------------
		System.out.println("Dodajem komentar.");
		dodajKomentar(emf, blogEntryID, "Blog ti je super!");
		System.out.println("Dodajem komentar - gotovo.");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}

		System.out.println("Dodajem komentar.");
		dodajKomentar(emf, blogEntryID, "Vau!");
		System.out.println("Dodajem komentar - gotovo.");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}

		System.out.println("Dodajem komentar.");
		dodajKomentar(emf, blogEntryID, "Još jedan komentar.");
		System.out.println("Dodajem komentar - gotovo.");

		System.out.println("Primjer upita.");
		primjerUpita(emf, blogEntryID);
		System.out.println("Primjer upita - gotovo.");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}

		System.out.println("Primjer upita 2.");
		primjerUpita2(emf, blogEntryID);
		System.out.println("Primjer upita 2 - gotovo.");

		emf.close();
	}

	/**
	 * Dodaje novi zapis u bazu bloga.
	 *
	 * @param emf
	 *            tvornica entity managera
	 * @return kreirani zapis
	 */
	public static BlogEntry dodajZapis(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setCreatedAt(new Date());
		blogEntry.setLastModifiedAt(blogEntry.getCreatedAt());
		blogEntry.setTitle("Moj prvi blog");
		blogEntry.setText("Ovo je moj prvi blog zapis.");

		em.persist(blogEntry);

		em.getTransaction().commit();
		em.close();

		return blogEntry;
	}

	/**
	 * Dodaje novi komentar u bazu za blog.
	 *
	 * @param emf
	 *            tvornica entity managera
	 * @param blogEntryID
	 *            id zapisa kojem se dodaje komentar
	 * @param message
	 *            poruka komentara
	 */
	public static void dodajKomentar(EntityManagerFactory emf, Long blogEntryID, String message) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryID);

		BlogComment blogComment = new BlogComment();
		blogComment.setUsersEMail("ivica@host.com");
		blogComment.setPostedOn(new Date());
		blogComment.setMessage(message);
		blogComment.setBlogEntry(blogEntry);

		em.persist(blogComment);

		blogEntry.getComments().add(blogComment);

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Primjer upita koji ispisuje sve komentare zadanog zapisa.
	 *
	 * @param emf
	 *            tvornica entity managera
	 * @param blogEntryID
	 *            id zapisa čije komentare treba ispisati
	 */
	private static void primjerUpita(EntityManagerFactory emf, Long blogEntryID) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryID);

		@SuppressWarnings("unchecked")
		List<BlogComment> comments = (List<BlogComment>) em
				.createQuery("select b from BlogComment as b where b.blogEntry=:be").setParameter("be", blogEntry)
				.getResultList();

		for (BlogComment bc : comments) {
			System.out.println("Blog [" + bc.getBlogEntry().getTitle() + "] ima komentar: [" + bc.getMessage() + "]");
		}

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Primjer upita koji izvodi imenovani upit.
	 *
	 * @param emf
	 *            tvornica entity managera
	 * @param blogEntryID
	 *            id zapisa
	 */
	private static void primjerUpita2(EntityManagerFactory emf, Long blogEntryID) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryID);

		@SuppressWarnings("unchecked")
		List<BlogComment> comments = (List<BlogComment>) em.createNamedQuery("BlogEntry.upit1")
				.setParameter("be", blogEntry).setParameter("when", new Date(new Date().getTime() - 2000))
				.getResultList();

		for (BlogComment bc : comments) {
			System.out.println("Blog [" + bc.getBlogEntry().getTitle() + "] ima komentar: [" + bc.getMessage() + "]");
		}

		em.getTransaction().commit();
		em.close();
	}
}