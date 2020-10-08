package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumerator koji predstavlja stanja Lexera.
 *
 * @author Alen Magdić
 *
 */
public enum LexerState {
	/** Predstavlja stanje Lexera kada on čita tokene unutar taga. **/
	IN_TAG,
	/** Predstavlja stanje Lexera kada on čita tokene izvan tagova. **/
	OUT_TAG;
}
