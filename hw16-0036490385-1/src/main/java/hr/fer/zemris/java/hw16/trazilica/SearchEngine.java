package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hr.fer.zemris.java.hw16.trazilica.docvectors.TFIDFDocVector;

import java.util.Set;
import java.util.TreeSet;

/**
 * Klasa koja predstavlja tražilicu. Tražilica na temelju vršnog direktorija s
 * datotekama i sa listom zaustavnih riječi izgrađuje vokabular i vrši sve što
 * je potrebno za izvršavanje upita. Sadrži metodu za izvršavanje zadanih upita.
 *
 * @author Alen Magdić
 *
 */
public class SearchEngine extends SimpleFileVisitor<Path> {
	/**
	 * Vokabular sadržan u bazi dokumenata nad kojom tražilica vrši pretragu.
	 */
	private Vocabulary vocabulary;
	/**
	 * Mapira putanju datoteke na popis riječi sadržanih u toj datoteci.
	 */
	private Map<Path, List<String>> fileToWords;
	/**
	 * Inverse document frequency vektor izgrađen za bazu dokumenata nad kojom
	 * tražilica radi.
	 */
	private double[] idfVector;
	/**
	 * Mapira putanju datoteke na njen {@link TFIDFDocVector}.
	 */
	private Map<Path, TFIDFDocVector> fileToDocVector;

	/**
	 * Konstruktor.
	 *
	 * @param dataDir
	 *            vršni direktorij u kojem se nalaze dokumenti nad kojima će
	 *            tražilica vršiti pretragu
	 * @param stopwords
	 *            lista zaustavnih riječi, tj. riječi koje će se ignorirati pri
	 *            pretragi
	 * @throws IOException
	 *             ukoliko dođe do problema sa čitanjem dokumenata
	 */
	public SearchEngine(Path dataDir, List<String> stopwords) throws IOException {
		fileToWords = new HashMap<>();
		vocabulary = new Vocabulary();
		Files.walkFileTree(dataDir, this);
		vocabulary.removeWords(stopwords);

		idfVector = new double[vocabulary.getSize()];
		for (int i = 0, n = idfVector.length; i < n; i++) {
			idfVector[i] = calcInvDocFreq(vocabulary.getWord(i));
		}

		fileToDocVector = new HashMap<>();
		for (Entry<Path, List<String>> entry : fileToWords.entrySet()) {
			fileToDocVector.put(entry.getKey(), new TFIDFDocVector(entry.getValue(), idfVector, vocabulary));
		}

	}

	/**
	 * Računa inverse document frequency vrijednost za zadanu riječ i dokumente
	 * iz baze dokumenata.
	 *
	 * @param word
	 *            riječ za koju se računa inverse document frequency
	 * @return inverse document frequency vrijednost za zadanu riječ
	 */
	private double calcInvDocFreq(String word) {
		int containWord = 0;
		for (Entry<Path, List<String>> entry : fileToWords.entrySet()) {
			if (entry.getValue().contains(word)) {
				containWord++;
			}
		}

		return Math.log((double) fileToWords.size() / containWord);
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		List<String> lines = Files.readAllLines(file);
		List<String> words = extractWordsFromText(lines);
		fileToWords.put(file, words);
		vocabulary.addWords(words);

		return FileVisitResult.CONTINUE;
	}

	/**
	 * Iz zadanog teksta (koji je zadan kao lista linija) generira listu riječi
	 * sadržanih u tome tekstu, pri čemu su i duplikati prisutni.
	 *
	 * @param lines
	 *            tekst iz kojeg je potrebno izdvojiti riječi
	 * @return lista riječi sadržanih u zadanom tekstu, uključujući duplikate
	 */
	private static List<String> extractWordsFromText(List<String> lines) {
		List<String> words = new ArrayList<>();
		StringBuilder stringB = new StringBuilder();

		for (String line : lines) {
			for (int i = 0, n = line.length(); i < n; i++) {
				char c = line.charAt(i);
				if (Character.isLetter(c)) {
					stringB.append(c);
				}
				if (!Character.isLetter(c) || i == n - 1) {
					if (stringB.length() > 0) {
						words.add(stringB.toString().toLowerCase());
						stringB.delete(0, stringB.length()); // clear
					}
				}
			}
		}
		return words;

	}

	/**
	 * Vraća veličinu vokabulara, tj. broj riječi u vokabularu.
	 *
	 * @return broj riječi u vokabularu
	 */
	public int getVocabularySize() {
		return vocabulary.getSize();
	}

	/**
	 * Izvršava upit zadan listom ključnih riječi. Pronalazi najbolje rezultate
	 * i vraća listu od maksimalno 10 rezultata.
	 *
	 * @param keywords
	 *            lista ključnih riječi za pretragu
	 * @return lista rezultata, sortirana silazno
	 */
	public List<QueryResult> executeQuery(String[] keywords) {
		List<String> words = getFilteredQuery(keywords);
		TFIDFDocVector queryVector = new TFIDFDocVector(words, idfVector, vocabulary);
		Set<QueryResult> results = new TreeSet<>();

		for (Entry<Path, TFIDFDocVector> entry : fileToDocVector.entrySet()) {
			double sim = queryVector.calculateSimilarity(entry.getValue());
			QueryResult res = new QueryResult(sim, entry.getKey());
			results.add(res);
		}

		List<QueryResult> listOfResults = new ArrayList<>(10);
		for (QueryResult res : results) {
			if (listOfResults.size() == 10 || res.getSimilarity() == 0) {
				break;
			}
			listOfResults.add(res);

		}

		return listOfResults;
	}

	/**
	 * Obrađuje dano polje ključnih riječi na način da u novu listu prepisuje
	 * samo ključne riječi koje su sadržane u vokabularu. Vraća kreiranu listu.
	 *
	 * @param keywords
	 *            polje ključnih riječi
	 * @return lista ključnih riječi bez riječi koje nisu sadržane u vokabularu
	 */
	public List<String> getFilteredQuery(String[] keywords) {
		List<String> wordsInVocab = new ArrayList<>();
		for (String w : keywords) {
			if (vocabulary.containsWord(w)) {
				wordsInVocab.add(w);
			}
		}

		return wordsInVocab;
	}

	/**
	 * Klasa koja predstavlja rezultat upita. Sadrži dobivenu sličnost
	 * (dokumenta i popisa ključnih riječi) te putanju do dokumenta na kojeg se
	 * odnosi rezultat.
	 *
	 * @author Alen Magdić
	 *
	 */
	public static class QueryResult implements Comparable<QueryResult> {
		/**
		 * Sličnost između dokumenta i upita.
		 */
		private double similarity;
		/**
		 * Putanja do dokumenta na kojeg se odnosi ovaj rezultat.
		 */
		private Path docPath;

		/**
		 * Konstruktor.
		 *
		 * @param similarity
		 *            sličnost između dokumenta i upita
		 * @param docPath
		 *            putanja do dokumenta
		 */
		public QueryResult(double similarity, Path docPath) {
			this.similarity = similarity;
			this.docPath = docPath;
		}

		@Override
		public int compareTo(QueryResult o) {
			int r = Double.compare(o.similarity, similarity);
			if (r != 0) {
				return r;
			}

			return docPath.getFileName().toString().compareTo(o.docPath.getFileName().toString());
		}

		/**
		 * Vraća sličnost dokumenta i upita.
		 *
		 * @return sličnost dokumenta i upita
		 */
		public double getSimilarity() {
			return similarity;
		}

		/**
		 * Vraća putanju do dokumenta.
		 *
		 * @return putanja do dokumenta
		 */
		public Path getDocPath() {
			return docPath;
		}

	}
}
