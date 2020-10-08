package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Klasa koja modelira blog zapis.
 *
 * @author Alen Magdić
 *
 */
@NamedQueries({
		@NamedQuery(name = "BlogEntry.upit1", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when") })
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {
	/** Id zapisa **/
	private Long id;
	/** Komentari zapisa **/
	private List<BlogComment> comments = new ArrayList<>();
	/** Vrijeme kreiranja zapisa **/
	private Date createdAt;
	/** Vrijeme zadnjeg uređivanja zapisa **/
	private Date lastModifiedAt;
	/** Naslov zapisa **/
	private String title;
	/** Tekst zapisa **/
	private String text;
	/** Autor zapisa **/
	private BlogUser creator;

	/**
	 * Dohvaća id zapisa.
	 *
	 * @return id zapisa
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Postavlja id zapisa.
	 *
	 * @param id
	 *            id zapisa
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Dohvaća autora zapisa.
	 *
	 * @return autor zapisa
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Postavlja autora zapisa.
	 *
	 * @param creator
	 *            autor zapisa
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}

	/**
	 * Dohvaća komentare zapisa.
	 *
	 * @return komentari zapisa
	 */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Postavlja listu komentara zapisa.
	 *
	 * @param comments
	 *            lista komentara zapisa
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Dohvaća vrijeme kreiranja zapisa.
	 *
	 * @return vrijeme kreiranja zapisa
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Postavlja vrijeme kreiranja zapisa.
	 *
	 * @param createdAt
	 *            vrijeme kreiranja zapisa
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Dohvaća vrijeme zadnje izmjene zapisa.
	 *
	 * @return vrijeme zadanje izmjene zapisa
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Postavlja vrijeme zadnje izmjene zapisa.
	 *
	 * @param lastModifiedAt
	 *            vrijeme zadnje izmjene zapisa
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Dohvaća naslov zapisa.
	 *
	 * @return naslov zapisa
	 */
	@Column(nullable = false, length = 200)
	public String getTitle() {
		return title;
	}

	/**
	 * Postavlja naslov zapisa.
	 *
	 * @param title
	 *            naslov zapisa
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Dohvaća tekst zapisa.
	 *
	 * @return tekst zapisa
	 */
	@Column(nullable = false, length = 4096)
	public String getText() {
		return text;
	}

	/**
	 * Postavlja tekst zapisa.
	 *
	 * @param text
	 *            tekst zapisa
	 */
	public void setText(String text) {
		this.text = text;
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
		BlogEntry other = (BlogEntry) obj;
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