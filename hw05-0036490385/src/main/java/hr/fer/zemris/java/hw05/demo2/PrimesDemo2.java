package hr.fer.zemris.java.hw05.demo2;

/**
 * This program is a demonstration of {@link PrimesCollection}. It prints all
 * pairs of the first two prime numbers.
 *
 * @author Alen Magdić
 *
 */
public class PrimesDemo2 {

	/**
	 * This method is the starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
	}
}
