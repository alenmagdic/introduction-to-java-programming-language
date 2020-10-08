package hr.fer.zemris.java.hw05.demo2;

/**
 * This program is a demonstration of {@link PrimesCollection}. It just prints
 * the first five prime numbers.
 *
 * @author Alen Magdić
 *
 */
public class PrimesDemo1 {

	/**
	 * This method is the starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5);
		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
	}
}
