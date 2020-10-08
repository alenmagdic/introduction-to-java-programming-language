package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ComplexNumber;

/**
 * Program demonstrira aritmetiku kompleksnih brojeva. Koristi razne načine
 * konstruiranja kompleksnog broja te vrši operacije zbrajanja, dijeljenja,
 * potenciranja i korijenovanja. Program ne očekuje prihvat argumenata.
 *
 * @author Alen Magdić
 *
 */
public class ComplexDemo {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		ComplexNumber c1 = new ComplexNumber(2, 3);
		ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
		ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)).div(c2).power(3).root(2)[1];
		System.out.println(c3);
	}
}
