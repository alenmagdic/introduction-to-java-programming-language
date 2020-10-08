package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Graphics;

/**
 * Sučelje koje predstavlja objekt koji se zna nacrtati korištenjem danog
 * {@link Graphics} objekta.
 *
 * @author Alen Magdić
 *
 */
public interface Drawable {

	/**
	 * Crta sebe korištenje zadanog {@link Graphics} objekta.
	 *
	 * @param g
	 *            objekt koji se koristi za crtanje
	 */
	public void draw(Graphics g);
}
