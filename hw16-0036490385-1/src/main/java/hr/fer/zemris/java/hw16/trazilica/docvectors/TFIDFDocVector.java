package hr.fer.zemris.java.hw16.trazilica.docvectors;

import java.util.List;

import hr.fer.zemris.java.hw16.math.Vector;
import hr.fer.zemris.java.hw16.trazilica.Vocabulary;

/**
 * Klasa koja predstavlja term frequency-inversion document frequency dokument
 * vektor. Tj. predstavlja vektor čiji je broj dimenzija jednak broju dimenzija
 * vokabulara. Komponenta sa određenim indeksom predstavlja tf-idf vrijednost za
 * riječ s istim indeksom iz vokabulara.
 *
 * @author Alen Magdić
 *
 */
public class TFIDFDocVector {
	/**
	 * Matematička reprezentacija ovog vektora.
	 *
	 */
	private Vector vector;

	/**
	 * Konstruktor.
	 *
	 * @param docWords
	 *            riječi sadržane u dokumentu kojeg ovaj vektor predstavlja
	 * @param idfVector
	 *            idf vektor koji se koristi za izračun komponenti ovog vektora
	 * @param vocab
	 *            vokabular
	 */
	public TFIDFDocVector(List<String> docWords, double[] idfVector, Vocabulary vocab) {
		if (idfVector.length != vocab.getSize()) {
			throw new IllegalArgumentException("Length of idfVector has to be equal to number of words in vocabulary.");
		}

		double[] components = new double[vocab.getSize()];

		for (int i = 0, n = vocab.getSize(); i < n; i++) {
			String word = vocab.getWord(i);
			components[i] = calcTermFreq(word, docWords) * idfVector[i];
		}

		vector = new Vector(components);
	}

	/**
	 * Vraća broj pojavljivanja zadane riječi u zadanoj listi riječi.
	 *
	 * @param word
	 *            riječ
	 * @param docWords
	 *            lista riječi
	 * @return broj pojavljivanja zadane riječi u zadanoj listi riječi
	 */
	private double calcTermFreq(String word, List<String> docWords) {
		int freq = 0;
		for (String w : docWords) {
			if (w.equals(word)) {
				freq++;
			}
		}
		return freq;
	}

	/**
	 * Vraća sličnost između ovog i zadanog {@link TFIDFDocVector} vektora.
	 *
	 * @param other
	 *            {@link TFIDFDocVector} s kojim se uspoređuje ovaj vektor.
	 * @return sličnost između ovog i zadanog {@link TFIDFDocVector} vektora
	 */
	public double calculateSimilarity(TFIDFDocVector other) {
		return vector.calcScalarProduct(other.vector) / (vector.getNorm() * other.vector.getNorm());
	}
}
