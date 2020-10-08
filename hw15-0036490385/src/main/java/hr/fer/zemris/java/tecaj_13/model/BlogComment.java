package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Klasa koja modelira blog komentar.
 *
 * @author Alen Magdić
 *
 */

@Entity
@Table(name = "blog_comments")
public class BlogComment {
	/** Id komentara **/
	private Long id;
	/** Zapis kojem komentar pripada **/
	private BlogEntry blogEntry;
	/** Email autora komentara **/
	private String usersEMail;
	/** Poruka komentara **/
	private String message;
	/** Vrijeme postanja komentara **/
	private Date postedOn;

	/**
	 * Dohvaća id komentara.
	 *
	 * @return id komentara
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Postavlja id komentara.
	 *
	 * @param id
	 *            id komentara
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Dohvaća zapis kojem ovaj komentar pripada.
	 *
	 * @return zapis kojem ovaj komentar pripada
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Postavlja zapis kojem ovaj komentar pripada
	 *
	 * @param blogEntry
	 *            zapis kojem ovaj komentar pripada
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Dohvaća mail autora ovog komentara.
	 *
	 * @return mail autora ovog komentara
	 */
	@Column(nullable = false, length = 100)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Postavlja mail autora ovog komentara
	 *
	 * @param usersEMail
	 *            mail autora ovog komentara
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Dohvaća poruku komentara.
	 *
	 * @return poruka komentara
	 */
	@Column(nullable = false, length = 4096)
	public String getMessage() {
		return message;
	}

	/**
	 * Postavlja poruku komentara.
	 *
	 * @param message
	 *            poruka komentara
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Dohvaća vrijeme postanja komentara.
	 *
	 * @return vrijeme postanja komentara
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Postavlja vrijeme postanja komentara.
	 *
	 * @param postedOn
	 *            vrijeme postanja komentara
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}