package hr.fer.zemris.java.hw16.trazilica;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa koja predstavlja vokabular, odnosno određeni skup riječ, bez duplikata
 * pri čemu se svaka riječ pohranjuje malim slovima. Klasa sadrži metode za
 * dodavanje novih riječi u vokabular, brisanje riječi iz vokabulara te dohvat
 * riječi.
 *
 * @author Alen Magdić
 *
 */
public class Vocabulary {
	/**
	 * Lista riječi koje vokabular sadrži.
	 */
	public List<String> words;

	/**
	 * Konstruktor.
	 */
	public Vocabulary() {
		words = new ArrayList<>();
	}

	/**
	 * Dodaje zadanu riječ u vokabular pod uvjetom da nije već prije dodana.
	 *
	 * @param word
	 *            riječ koja se dodaje u vokabular
	 */
	public void addWord(String word) {
		if (word == null) {
			throw new IllegalArgumentException("Can not add null to vocabulary.");
		}

		word = word.trim().toLowerCase();
		if (!words.contains(word)) {
			words.add(word);
		}
	}

	/**
	 * Dodaje sve riječi iz zadane liste riječi u vokabular.
	 *
	 * @param words
	 *            riječi koje se dodaju u vokabular
	 */
	public void addWords(List<String> words) {
		if (words == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}

		for (String word : words) {
			addWord(word);
		}
	}

	/**
	 * Briše sve riječi iz vokabulara koje se nalaze u zadanoj listi.
	 *
	 * @param wordsToRemove
	 *            lista koja sadrži riječi koje je potrebno ukloniti iz
	 *            vokabulara
	 */
	public void removeWords(List<String> wordsToRemove) {
		if (wordsToRemove == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}

		for (String w : wordsToRemove) {
			words.remove(w.trim().toLowerCase());
		}
	}

	/**
	 * Dohvaća riječ sa zadanim indexom.
	 *
	 * @param index
	 *            indeks riječi koju treba dohvatiti
	 * @return riječ sa zadanim indexom
	 */
	public String getWord(int index) {
		return words.get(index);
	}

	/**
	 * Dohvaća broj riječi u vokabularu.
	 *
	 * @return broj riječi u vokabularu
	 */
	public int getSize() {
		return words.size();
	}

	/**
	 * Provjerava sadrži li vokabular zadanu riječ.
	 *
	 * @param word
	 *            riječ čija se prisutnost provjerava
	 * @return true ako vokabular sadrži zadanu riječ
	 */
	public boolean containsWord(String word) {
		return words.contains(word);
	}

	/**
	 * Dohvaća listu riječi koju vokabular sadrži.
	 *
	 * @return lista riječi koje vokabular sadrži
	 */
	public List<String> getWords() {
		return new ArrayList<>(words);
	}
}
