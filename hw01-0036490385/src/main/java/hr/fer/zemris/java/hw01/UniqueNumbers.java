package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program prihvaća unos cijelih brojeva sa tipkovnice te ih pohranjuje. Unos se
 * prekida riječju 'kraj'. Program ispisuje pohranjene vrijednosti sortirano od
 * manjeg prema većem te od većeg prema manjem.
 * 
 * @author Alen Magdić
 *
 */

public class UniqueNumbers {

    /**
     * Metoda od koje počinje izvođenje programa.
     * 
     * @param args
     *            argumenti zadani preko naredbenog retka - u ovom se programu
     *            ne koriste
     */
    public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
	TreeNode head = inputValuesToTree(sc);

	System.out.print("Ispis od najmanjeg: ");
	printInOrder(head);

	System.out.print("\nIspis od najvećeg: ");
	printReversed(head);
    }

    /**
     * Ispisuje zadano stablo cijelih brojeva sortirano od najmanjeg do najvećeg
     * broja.
     * 
     * @param head
     *            glava stabla cijelih brojeva čiji je sadržaj potrebno ispisati
     */
    private static void printInOrder(TreeNode head) {
	if (head == null) {
	    return;
	}
	printInOrder(head.left);
	System.out.printf("%d ", head.value);
	printInOrder(head.right);

    }

    /**
     * Ispisuje zadano stablo cijelih brojeva sortirano od najvećeg do najmanjeg
     * broja.
     * 
     * @param head
     *            glava stabla cijelih brojeva čiji je sadržaj potrebno ispisati
     */
    private static void printReversed(TreeNode head) {
	if (head == null) {
	    return;
	}
	printReversed(head.right);
	System.out.printf("%d ", head.value);
	printReversed(head.left);
    }

    /**
     * Izvodi korisnički unos cijelih brojeva te ih pohranjuje u stablo. U
     * slučaju pogrešnog unosa, on se ignorira uz odgovarajuću poruku. Unos
     * brojeva se prekida riječju 'kraj'. Za unos se koristi predani scanner.
     * Metoda vraća kreirano stablo napunjeno unesenim vrijednostima.
     * 
     * @param sc
     *            scanner s kojim se vrši korisnički unos
     * @return kreirano stablo napunjeno unesenim vrijednostima
     */
    private static TreeNode inputValuesToTree(Scanner sc) {
	String input;
	int number; // pohranjuje parsirani input
	TreeNode head = null;

	while (true) {
	    System.out.print("Unesite broj > ");
	    input = sc.next();
	    if (input.equals("kraj")) {
		break;
	    }

	    try {
		number = Integer.parseInt(input);
	    } catch (NumberFormatException ex) {
		System.out.printf("'%s' nije cijeli broj.%n", input);
		continue;
	    }

	    head = addNode(head, number);
	}

	return head;
    }

    /**
     * Dodaje zadanu vrijednost u zadano stablo cijelih brojeva. Ukoliko je
     * vrijednost već pohranjena u stablu, ništa se ne mijenja. Metoda vraća
     * modificirano stablo. U slučaju predaje null kao argumenta za stablo,
     * metoda će sama kreirati stablo sa zadanom vrijednošću.
     * 
     * @param head
     *            stablo u koje je potrebno dodati zadanu vrijednost
     * @param value
     *            vrijednost koju je potrebno dodati u zadano stablo
     * @return stablo sa dodanim elementom ili nepromijenjeno stablo u slučaju
     *         dodavanja već postojeće vrijednosti
     */
    public static TreeNode addNode(TreeNode head, int value) {
	if (head == null) {
	    head = new TreeNode();
	    head.value = value;
	    return head;
	} else if (head.value == value) {
	    System.out.println("Broj već postoji. Preskačem.");
	    return head;
	}

	if (value > head.value) {
	    head.right = addNode(head.right, value);
	} else {
	    head.left = addNode(head.left, value);
	}
	return head;
    }

    /**
     * Metoda vraća veličinu zadanog stabla, odnosno broj čvorova.
     * 
     * @param head
     *            stablo čiju je veličinu potrebno izračunati
     * @return veličina zadanog stabla, odnosno broj čvorova
     */
    public static int treeSize(TreeNode head) {
	if (head == null) {
	    return 0;
	}
	return 1 + treeSize(head.left) + treeSize(head.right);
    }

    /**
     * Metoda provjerava postoji li zadana vrijednost u zadanom stablu. Vraća
     * true ako postoji, inače false.
     * 
     * @param head
     *            stablo koje je potrebno provjeriti
     * @param value
     *            vrijednost čiju je prisutnost u zadanom stablu potrebno
     *            provjeriti
     * @return true ako zadana vrijednost postoji u zadanom stablu, inače false
     */
    public static boolean containsValue(TreeNode head, int value) {
	if (head == null) {
	    return false;
	}

	if (value == head.value) {
	    return true;
	} else if (value > head.value) {
	    return containsValue(head.right, value);
	}
	return containsValue(head.left, value);
    }

    /**
     * Pomoćna klasa, odnosno struktura, koja predstavlja čvor stabla. Sadrži
     * reference na lijevo i desno dijete te sadrži vlastitu vrijednost.
     * 
     * @author Alen Magdić
     *
     */
    public static class TreeNode {
	/**
	 * Referenca na lijevo dijete.
	 */
	TreeNode left;
	/**
	 * Referenca na desno dijete.
	 */
	TreeNode right;
	/**
	 * Vrijednost čvora.
	 */
	int value;
    }

}
