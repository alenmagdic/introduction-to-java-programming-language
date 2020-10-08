package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumerator koji predstavlja stanje Lexera.
 *
 * @author Alen Magdić
 *
 */
public enum LexerState {
	/**
	 * Predstavlja osnovni način rada Lexera koji razdvaja riječi, brojeve i
	 * simbole.
	 */
	BASIC,
	/**
	 * Predstavlja prošireni način rada Lexera koji sve osim simbola '#' tretira
	 * kao jedan niz znakova.
	 */
	EXTENDED;
}
