package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program prima podatke o širini i visini pravokutnika te izračunava i ispisuje
 * podatke o površini i opsegu zadanog pravokutnika. Program prihvaća podatke na
 * dva načina: kao argumente zadane preko naredbenog retka ili preko unosa sa
 * tipkovnice. U slučaju da je broj argumenata različit od 0 i 2, prekida se
 * izvođenje. Ako su argumenti pogrešno zadani, prekida se izvođenje programa.
 * Unos preko tipkovnice se ponavlja sve dok unos ne bude korektan.
 * 
 * @author Alen Magdić
 *
 */

public class Rectangle {

    /**
     * Metoda od koje počinje izvođenje programa.
     * 
     * @param args
     *            argumenti zadani preko naredbenog retka - očekuje se 0 ili 2
     *            argumenta (širina i visina pravokutnika)
     */
    public static void main(String[] args) {
	double width, height;

	if (args.length != 0 && args.length != 2) {
	    System.out.printf("Program prihvaća ili 0 ili 2 argumenta. Broj primljenih argumenata: %d%n", args.length);
	    return;
	}

	if (args.length == 2) {
	    width = convertToDoubleAndCheck(args[0]);
	    height = convertToDoubleAndCheck(args[1]);
	    if (width == -1 || height == -1) {
		return;
	    }
	} else {
	    Scanner sc = new Scanner(System.in);
	    width = inputPositiveDouble("Unesite širinu > ", sc);
	    height = inputPositiveDouble("Unesite visinu > ", sc);
	    sc.close();
	}

	System.out.println("Pravokutnik širine " + width + " i visine " + height + " ima površinu " + width * height
		+ " te opseg " + 2 * (width + height) + ".");

    }

    /**
     * Pomoćna metoda koja konvertira zadani string u pozitivni double veći od
     * nule. Ukoliko to nije moguće, odnosno ako se dani string ne može
     * protumačiti kao pozitivni decimalni broj veći od nule, ispisuje se
     * odgovarajuća poruka. U slučaju nemogućnosti konverzije, metoda vraća -1,
     * a ako je sve u redu, vraća rezultantni double.
     * 
     * 
     * @param string
     *            string koji je potrebno konvertirati u pozitivni double veći
     *            od nule
     * @return pozitivni decimalni broj veći od nule dobiven iz danog stringa
     */
    private static double convertToDoubleAndCheck(String string) {
	double result;

	try {
	    result = Double.parseDouble(string);
	} catch (NumberFormatException ex) {
	    System.out.printf("'%s' se ne može protumačiti kao broj.%n", string);
	    return -1;
	}

	if (result <= 0) {
	    System.out.println(result == 0 ? "Unijeli ste nulu." : "Unijeli ste negativnu vrijednost.");
	    return -1;
	}

	return result;
    }

    /**
     * Izvršava input pozitivnog decimalnog broja većeg od nule uz danu poruku i
     * korištenjem danog scannera. Metoda ponavlja unos sve dok ne bude unesen
     * pozitivan decimalni broj veći od nule. Unos je popraćen odgovarajućim
     * porukama u slučaju pogrešnog unosa.
     * 
     * @param inputText
     *            poruka koja će biti ispisana pri zahtjevanju unosa s
     *            tipkovnice
     * @param sc
     *            scanner preko kojeg se čitaju podaci s tipkovnice
     * 
     * @return uneseni pozitivni decimalni broj veći od nule
     */
    private static double inputPositiveDouble(String inputText, Scanner sc) {
	String input;
	double number; // pohranjuje input parsiran u double

	do {
	    System.out.print(inputText);
	    input = sc.next();
	    number = convertToDoubleAndCheck(input);
	} while (number == -1);
	return number;
    }
}
