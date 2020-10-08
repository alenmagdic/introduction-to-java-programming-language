package hr.fer.zemris.java.hw16.jvdraw.mousedrawers;

import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Sučelje koje predstavlja objekt koji razumije način na koji se mišem crta
 * određen geometrijski objekt. Svaka implementacija ovog sučelja zna raditi sa
 * samo jednom određenom vrstom objekta, npr. krugom.
 *
 * @author Alen Magdić
 *
 */
public interface MouseDrawer {
	/**
	 * Dojavljuje objektu da je miš kliknut, a on tada modificira podatke o
	 * geometrijskom objektu koji se generira. Npr. ako se radi o liniji, na
	 * prvi klik se sprema startna točka, na drugi završna točka.
	 *
	 * @param mousePosition
	 *            položaj miša u trenutku klika, relativan u odnosu na platno na
	 *            kojem se crta
	 */
	public void mouseClicked(Point mousePosition);

	/**
	 * Dojavljuje da je crtanje objekta gotovo.
	 *
	 * @return true ako je crtanje objekta gotovo
	 */
	public boolean isDone();

	/**
	 * Vraća kreirani geometrijski objekt. Ako kreiranje još nije gotovo, vraća
	 * null.
	 *
	 * @return kreirani geometrijski objekt
	 */
	public GeometricalObject getCreatedObject();

	/**
	 * Vraća trenutnu verziju objekta dobivena uzimajući u obzir trenutnu
	 * poziciju miša kao poziciju zadnjeg klika. Ako je za crtanje nekog objekta
	 * nužno više od 2 klika, onda će se {@link MouseDrawer} naći u stanju u
	 * kojem neće moći generirati privremenu verziju - tada metoda vraća null.
	 * Ako je objekt već gotov, tada će metoda vratiti isti objekt kao i metoda
	 * getCreatedObject, odnosno ignorirat će se trenutni položaj miša.
	 *
	 * @param mousePosition
	 *            trenutna pozicija miša, relativno u odnosu na platno za
	 *            crtanje
	 * @return trenutnu verziju objekta, generirana uzimajući u obzir trenutnu
	 *         poziciju miša kao poziciju zadnjeg klika
	 */
	public GeometricalObject getTempVersion(Point mousePosition);

	/**
	 * Nalaže crtaču da zaboravi zadnje kreirani objekt tako da sljedeći poziv
	 * metode mouseClicked registrira kao početak crtanja novog objekta.
	 */
	public void restart();
}
