package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

/**
 * Apstraktna klasa koja predstavlja objekt koji ima svoje ime te ime svoga
 * tipa.
 *
 * @author Alen Magdić
 *
 */
public abstract class Nameable {
	/**
	 * Ime objekta.
	 */
	private String name;

	/**
	 * Postavlja ime objekta.
	 *
	 * @param name
	 *            ime objekta
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Dohvaća ime objekta.
	 *
	 * @return ime objekta
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Vraća ime tipa objekta.
	 *
	 * @return ime tipa objekta
	 */
	abstract public String getTypeName();

}