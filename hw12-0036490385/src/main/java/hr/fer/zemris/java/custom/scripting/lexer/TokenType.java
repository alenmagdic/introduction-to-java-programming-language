package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumerator koji predstavlja različite tipove tokena.
 *
 * @author Alen Magdić
 *
 */
public enum TokenType {
	/** Predstavlja tip tokena koji sadrži komad teksta pisan izvan tagova. **/
	TEXT,
	/**
	 * Predstavlja tip tokena koji sadrži komad teksta okružen navodnicima.
	 **/
	STRING,
	/**
	 * Predstavlja tip tokena koji sadrži ime funkcije, sa početnom oznakom '@'
	 **/
	FUNCTION,
	/**
	 * Predstavlja tip tokena koji sadrži nekakvo ime. To ime može biti ime taga
	 * (npr. FOR) ili ime varijable.
	 */
	NAME,
	/**
	 * Predstavlja tip tokena koji sadrži cijeli broj, odnosno integer.
	 */
	INT_CONST,
	/**
	 * Predstavlja tip tokena koji sadrži realni broj, odnosno double.
	 */
	DOUBLE_CONST,
	/**
	 * Predstavlja tip tokena koji sadrži samo jedan simbol.
	 */
	SYMBOL,
	/**
	 * Predstavlja tip tokena koji sadrži oznaku za otvoreni tag, odnosno
	 * dvoznakovni niz "{$".
	 */
	OPEN_TAG,
	/**
	 * Predstavlja tip tokena koji sadrži oznaku za zatvoreni tag, odnosno
	 * dvoznakovni niz "$}".
	 */
	CLOSE_TAG,
	/**
	 * Predstavlja tip tokena koji predstavlja završetak datoteke.
	 */
	EOF;
}
