package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program koji prihvaća unos cijelog broja sa tipkovnice te izračunava i
 * ispisuje vrijednost faktorijela zadanog broja. Prihvaćaju se samo brojevi u
 * rasponu 1-20. Ukoliko se unese nešto što nije broj ili broj izvan zadanog
 * raspona, biti će ispisana odgovarajuća poruka. Unos se prekida riječju
 * 'kraj'.
 *
 * @author Alen Magdić
 *
 */

public class Factorial {
    /**
     * Donj limit podržanih brojeva za izračun faktorijela, odosno najmanji
     * podržani broj.
     *
     */
    public static final int LIMIT_DOWN = 1;
    /**
     * Gornji limit podržanih brojeva za izračun faktorijela, odnosno najveći
     * podržani broj.
     */
    public static final int LIMIT_UP = 20;

    /**
     * Metoda od koje kreće izvođenje programa.
     *
     * @param args
     *            argumenti zadani preko naredbenog retka. Program ne očekuje
     *            prihvat argumenata.
     */
    public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
	String input;
	int number; // pohranjuje parsirani input

	while (true) {
	    System.out.print("Unesite broj > ");
	    input = sc.next();

	    if (input.equals("kraj")) {
		System.out.println("Doviđenja.");
		break;
	    }

	    try {
		number = Integer.parseInt(input);
	    } catch (NumberFormatException ex) {
		System.out.printf("'%s' nije cijeli broj.%n", input);
		continue;
	    }
	    if (number < LIMIT_DOWN) { // || number > LIMIT_UP) {
		System.out.printf("'%d' nije broj u dozvoljenom rasponu.%n", number);
		continue;
	    }

	    System.out.printf("%d! = %f\n", number, factorial(number));
	}

	sc.close();
    }

    /**
     * Metoda koja računa faktorijel broja zadanog parametrom number. Očekuje se
     * broj u rasponu 1-20. Ako je dan broj izvan tog raspona, metoda vraća
     * vrijednost -1L.
     *
     * @param number
     *            broj (u rasponu 1-20) čiji faktorijel je potrebno izračunati
     * @return faktorijel zadanog broja tj. faktorijel parametra number - u
     *         slučaju zadanog negativnog broja, vraća -1L
     */
    public static double factorial(int number) {
	if (number < LIMIT_DOWN) {// || number > LIMIT_UP) {
	    return -1;
	}

	double result = 1;
	for (int i = 2; i <= number; i++) {
	    result *= i;
	}
	return result;
    }
}
