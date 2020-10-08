package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Klasa koja modelira korisnika bloga.
 *
 * @author Alen Magdić
 *
 */
@Entity
@Table(name = "blog_users")
public class BlogUser {
	/** Id korisnika **/
	private Long id;
	/** Ime korisnika **/
	private String firstName;
	/** Prezime korisnika **/
	private String lastName;
	/** Nick korisnika **/
	private String nick;
	/** Email korisnika **/
	private String email;
	/** Password hash korisnika **/
	private String passwordHash;
	/** Lista svih zapisa korisnika **/
	private List<BlogEntry> entries = new ArrayList<>();

	/**
	 * Konstruktor.
	 *
	 */
	public BlogUser() {
	}

	/**
	 * Konstruktor.
	 *
	 * @param firstName
	 *            ime korisnika
	 * @param lastName
	 *            prezime korisnika
	 * @param nick
	 *            nick korisnika
	 * @param email
	 *            email korisnika
	 * @param passwordHash
	 *            password hash korisnika
	 */
	public BlogUser(String firstName, String lastName, String nick, String email, String passwordHash) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.nick = nick;
		this.email = email;
		this.passwordHash = passwordHash;
	}

	/**
	 * Dohvaća listu svih zapisa korisnika.
	 *
	 * @return lista svih zapisa korisnika
	 */
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("createdAt")
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * Postavlja listu svih zapisa korisnika.
	 *
	 * @param entries
	 *            lista svih zapisa korisnika
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Id korisnika.
	 *
	 * @return id korisnika
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Postavlja id korisnika.
	 *
	 * @param id
	 *            id korisnika
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Dohvaća ime korisnika.
	 *
	 * @return ime korisnika
	 */
	@Column(nullable = false, length = 100)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Postavlja ime korisnika.
	 *
	 * @param firstName
	 *            ime korisnika
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Dohvaća prezime korisnika.
	 *
	 * @return prezime korisnika
	 */
	@Column(nullable = false, length = 100)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Postavlja prezime korisnika.
	 *
	 * @param lastName
	 *            prezime korisnika
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Dohvaća nick korisnika.
	 *
	 * @return nick korisnika
	 */
	@Column(nullable = false, length = 100, unique = true)
	public String getNick() {
		return nick;
	}

	/**
	 * Postavlja nick korisnika.
	 *
	 * @param nick
	 *            nick korisnika
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Dohvaća email korisnika.
	 *
	 * @return email korisnika
	 */
	@Column(nullable = false, length = 100)
	public String getEmail() {
		return email;
	}

	/**
	 * Postavlja email korisnika.
	 *
	 * @param email
	 *            email korisnika
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Dohvaća password hash korisnika.
	 *
	 * @return password hash korisnika
	 */
	@Column(nullable = false, length = 100)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Postavlja password hash korisnika.
	 *
	 * @param passwordHash
	 *            password hash korisnika
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}
