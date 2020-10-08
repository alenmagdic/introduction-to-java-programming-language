package hr.fer.zemris.java.hw13.glasanje;

/**
 * This class represents a music band. It contains band information like band
 * id, band name and a link to some song of the band.
 *
 * @author Alen Magdić
 *
 */
public class Band implements Comparable<Band> {
	/** Band id **/
	private int id;
	/** Band name **/
	private String name;
	/** Link to some song of this band **/
	private String songLink;

	/**
	 * Constructor.
	 *
	 * @param id
	 *            band id
	 * @param name
	 *            band name
	 * @param songLink
	 *            link to some song of the band
	 */
	public Band(int id, String name, String songLink) {
		super();
		this.id = id;
		this.name = name;
		this.songLink = songLink;
	}

	@Override
	public int compareTo(Band o) {
		return id - o.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Band other = (Band) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	/**
	 * Returns band id.
	 *
	 * @return band id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns band name.
	 *
	 * @return band name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a link to some song of this band.
	 *
	 * @return link to some song of this band
	 */
	public String getSongLink() {
		return songLink;
	}
}
