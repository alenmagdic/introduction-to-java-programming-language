package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Program koji kao argument komandne linije prima matematički izraz u postfix
 * notaciji te ga izračunava i ispisuje njegov rezultat na standardni izlaz.
 * Podržane operacije su +,-,/,* i %. Program podržava samo cijele brojeve. U
 * slučaju da je dani izraz neispravan, ispisuje se odgovarajuća poruka.
 *
 * @author Alen Magdić
 *
 */
public class StackDemo {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.printf("Program očekuje točno jedan argument. Broj predanih argumenata: %d\n", args.length);
			return;
		}

		ObjectStack stack = new ObjectStack();
		String[] expression = args[0].split(" ");
		for (String element : expression) {
			if (element.equals("")) {
				continue;
			}

			// provjera je li element broj i dodavanje na stog ako jest
			try {
				int num = Integer.parseInt(element);
				stack.push(num);
				continue;
			} catch (NumberFormatException ex) {
			}

			// element nije broj -> izvršavanje dane operacije
			try {
				int result = executeOperation(stack, element);
				stack.push(result);
			} catch (RuntimeException ex) {
				System.out.println(ex.getMessage());
				return;
			}
		}

		if (stack.size() != 1) {
			System.out.println("Došlo je do pogreške u izračunu. Dani izraz nije ispravan.");
		} else {
			System.out.printf("Zadana operacija: %s\nRezultat operacije: %d", args[0], stack.pop());
		}
	}

	/**
	 * Pomoćna metoda koja izvršava operaciju zadanu argumentom element pri čemu
	 * se operandi dohvaćaju sa zadnog stoga. Ako dani element ne predstavlja
	 * niti jednu od podržanih operacija, odnosno ako predstavlja bilo što što
	 * nije +,-,/,*,%, baca se IllegalArgumentException. Ako dođe do pokušaja
	 * dijeljenja s nulom, baca se ArithmeticException uz odgovarajuću poruku.
	 *
	 * @param stack
	 *            stog se kojeg se uzimaju operandi
	 * @param element
	 *            string koji predstavlja operator čiju je operaciju potrebno
	 *            izvršiti
	 * @return rezultat zadane operacije
	 */
	private static int executeOperation(ObjectStack stack, String element) {
		int num1;
		int num2;
		try {
			num2 = (int) stack.pop();
			num1 = (int) stack.pop();
		} catch (EmptyStackException ex) {
			throw new EmptyStackException("Došlo je do pogreške pri izračunu izraza. Dani izraz nije korektan.");
		}

		int result; // rezultat operacije
		if (element.equals("+")) {
			result = num1 + num2;
		} else if (element.equals("-")) {
			result = num1 - num2;
		} else if (element.equals("/")) {
			if (num2 == 0) {
				throw new ArithmeticException("Dani izraz sadrži dijeljenje sa nulom. Rezultat je nedefiniran.");
			}
			result = num1 / num2;
		} else if (element.equals("*")) {
			result = num1 * num2;
		} else if (element.equals("%")) {
			if (num2 == 0) {
				throw new ArithmeticException("Dani izraz sadrži operaciju modulo sa nulom. Rezultat je nedefiniran.");
			}
			result = num1 % num2;
		} else {
			throw new IllegalArgumentException("Dani izraz nije ispravan.");
		}
		return result;
	}
}
