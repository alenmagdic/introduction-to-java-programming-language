package hr.fer.zemris.java.hw18.galerija.models;

/**
 * Klasa koja sadrži podatke o jednoj slici, odnosno: ime, adresu slike i popis
 * tagova za tu sliku.
 *
 * @author Alen Magdić
 *
 */
public class ImageData {
	/** Ime slike **/
	private String name;
	/** Adresa slike **/
	private String src;
	/** Popis tagova slike **/
	private String tags;

	/**
	 * Dohvaća ime slike.
	 *
	 * @return ime slike
	 */
	public String getName() {
		return name;
	}

	/**
	 * Postavlja ime slike.
	 *
	 * @param name
	 *            ime slike
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Dohvaća adresu slike.
	 *
	 * @return adresu slike
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * Postavlja adresu slike.
	 *
	 * @param src
	 *            adresa slike
	 */
	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * Dohvaća popis tagova slike.
	 *
	 * @return popis tagova slike
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * Postavlja popis tagova slike.
	 *
	 * @param tags
	 *            popis tagova slike
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}
}
