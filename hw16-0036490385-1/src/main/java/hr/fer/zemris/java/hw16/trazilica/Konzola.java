package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw16.trazilica.SearchEngine.QueryResult;

/**
 * Program koji omogućuje pretragu dokumenata na temelju zadanih ključnih
 * riječi. Program podržava sljedeće komande: query, type, results,exit. Komanda
 * query kao argumente prima popis ključnih riječi na temelju kojih se vrši
 * pretraga. Komanda type prima kao argument indeks rezultata zadnjeg izvršenog
 * upita te ispisuje sadržaj tog dokumenta. Komanda results vraća rezultate
 * zadnjeg izvršenog upita. Komanda exit omogućuje izlaz iz programa.
 *
 * Program prima jedan argument, putanju vršnog direktorija u kojem se nalazi
 * baza dokumenata za koje će se vršiti pretraga.
 *
 * @author Alen Magdić
 *
 */
public class Konzola {
	/**
	 * Putanja do datoteke u kojoj se nalazi popis zaustavnih riječi.
	 */
	private static final Path STOPWORDS_DIR = Paths.get("src/main/resources/stopwords.txt");
	/**
	 * Sortirana lista rezultata zadnjeg izvršenog upita.
	 */
	private static List<QueryResult> results;

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		String tag = "pero";
		System.out.println("<button onclick = \"prikaziSlike(\"" + tag + "\");\">" + tag + "</button>");

		if (args.length != 1) {
			System.out.println("Expected exactly 1 argument, " + args.length + " given.");
			return;
		}

		Path dataDir = Paths.get(args[0]);
		if (!Files.exists(dataDir)) {
			System.out.println("The specified path does not exist. Path: " + dataDir.normalize());
			return;
		} else if (!Files.isDirectory(dataDir)) {
			System.out.println("The specified path does not represent a directory. Path: " + dataDir.normalize());
			return;
		}

		if (!Files.exists(STOPWORDS_DIR) || Files.isDirectory(STOPWORDS_DIR)) {
			System.out.println(
					"Unable to start engine because a resource file can not be found. Expected resource file path: "
							+ STOPWORDS_DIR.normalize());
			return;
		}

		List<String> stopwordsUnfiltered;
		try {
			stopwordsUnfiltered = Files.readAllLines(STOPWORDS_DIR);
		} catch (IOException e1) {
			System.out.println(
					"There has been a problem reading the following resource file: " + STOPWORDS_DIR.normalize());
			return;
		}
		List<String> stopwords = removeNonLetters(stopwordsUnfiltered);

		SearchEngine engine;
		try {
			engine = new SearchEngine(dataDir, stopwords);
		} catch (IOException e) {
			System.out.println("There has been a problem reading the documents.");
			return;
		}

		System.out.println("Vocabulary size: " + engine.getVocabularySize());

		runShell(engine);
	}

	/**
	 * Metoda koja iz dane liste riječi kreira listu riječi iz kojih su
	 * uklonjeni svi znakovi koji nisu slova.
	 *
	 * @param wordsUnfiltered
	 *            lista riječi u kojima ima znakova koji nisu slovo
	 * @return filtrirana verzija liste u kojima se sve riječi sastoje
	 *         isključivo od znakova
	 */
	private static List<String> removeNonLetters(List<String> wordsUnfiltered) {
		List<String> wordsFiltered = new ArrayList<>();

		for (String word : wordsUnfiltered) {
			StringBuilder strB = new StringBuilder();
			for (char c : word.toCharArray()) {
				if (Character.isLetter(c)) {
					strB.append(c);
				}
			}
			wordsFiltered.add(strB.toString());
		}
		return wordsFiltered;
	}

	/**
	 * Pokreće ljusku.
	 *
	 * @param engine
	 *            tražilica koja će se koristiti za izvršavanje upita
	 */
	private static void runShell(SearchEngine engine) {
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.print("Enter command > ");
			String command = scan.nextLine().trim();
			String commandName = command.split(" ")[0];

			String[] cmdParts = command.split(" ");
			String[] args = Arrays.copyOfRange(cmdParts, 1, cmdParts.length);

			switch (commandName) {
			case "query":
				executeQuery(args, engine);
				break;
			case "type":
				executeType(args);
				break;
			case "results":
				executeResults();
				break;
			case "exit":
				scan.close();
				System.out.println("Goodbye!");
				return;
			default:
				System.out.println("Unrecognized command!");
			}
		}

	}

	/**
	 * Izvršava naredbu 'results', odnosno ispisuje popis rezultata zadnjeg
	 * izvršenog upita. Ako nije izvršen niti jedan upit, ispisuje poruku da
	 * nema rezultata.
	 *
	 */
	private static void executeResults() {
		if (results == null) {
			System.out.println("No results to show.");
			return;
		}

		for (int i = 0, n = results.size(); i < n; i++) {
			QueryResult res = results.get(i);
			System.out.printf("[%d] (%.4f) %s\n", i, res.getSimilarity(), res.getDocPath());
		}
	}

	/**
	 * Izvršava naredbu 'type' odnosno ispisuje dokument iz zadnje dobivene
	 * liste rezultata. Argument određuje indeks rezultata čiji se dokument želi
	 * ispisati. Na primjer, ako se želi ispisati dokumente koji je prvi u listi
	 * rezultata, treba upisati naredbu 'type 0'.
	 *
	 * @param args
	 *            polje argumenata za komandu 'type'
	 */
	private static void executeType(String[] args) {
		if (args.length != 1) {
			System.out.println("Command 'type' expects exactly 1 argument, " + args.length + " given.");
			return;
		}

		if (results == null) {
			System.out.println("This command can not be executed before executing at least one query.");
			return;
		}

		int resultIndex;
		try {
			resultIndex = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			System.out.println("Invalid argument given. Expected a number.");
			return;
		}

		if (resultIndex < 0 || resultIndex >= results.size()) {
			System.out.println("Argument out of results range. Results size: " + results.size());
			return;
		}

		Path docPath = results.get(resultIndex).getDocPath().normalize();

		String stringToPrint = "Document: " + docPath.toString();
		printCharMultTimes('-', stringToPrint.length());
		System.out.println(stringToPrint);
		printCharMultTimes('-', stringToPrint.length());
		try {
			System.out.println(new String(Files.readAllBytes(docPath)));
		} catch (IOException e) {
			System.out.println("Unable to print document content.");
		}

		printCharMultTimes('-', stringToPrint.length());
		System.out.println();
	}

	/**
	 * Ispisuje zadani karakter zadani broj puta.
	 *
	 * @param ch
	 *            karakter koji treba ispisati
	 * @param length
	 *            broj puta koliko treba ispisati zadani karakter
	 */
	private static void printCharMultTimes(char ch, int length) {
		for (int i = 0; i < length; i++) {
			System.out.print(ch);
		}
		System.out.println();
	}

	/**
	 * Izvršava naredbu 'query'. Ispisuje koje se ključne riječi koriste za
	 * pretragu (ignoriraju se riječi koje ne postoje u vokabularu) te ispisuje
	 * listu najviše 10 rezultata.
	 *
	 * @param args
	 *            argumenti naredbe 'query', popis ključnih riječi za pretragu
	 * @param engine
	 *            tražilica koja će izvršiti upit
	 */
	private static void executeQuery(String[] args, SearchEngine engine) {
		System.out.println("Query is: " + engine.getFilteredQuery(args));

		results = engine.executeQuery(args);

		if (results.size() == 0) {
			System.out.println("No matches found.");
			return;
		}

		System.out.println("Query results: ");
		executeResults();
	}
}
