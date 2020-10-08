package hr.fer.zemris.java.hw16.math;

import java.util.Arrays;

/**
 * Klasa koja reprezentira n-dimenzijski vektor. Vektor se definira preko polja
 * njegovih komponenata.
 *
 * @author Alen Magdić
 *
 */
public class Vector {
	/**
	 * Polje komponenata vektora.
	 */
	private double[] components;

	/**
	 * Konstruktor.
	 *
	 * @param components
	 *            komponente vektora
	 */
	public Vector(double... components) {
		this.components = Arrays.copyOf(components, components.length);
	}

	/**
	 * Izračunava skalarni produkt ovog i zadanog vektora. Da bi produkt bio
	 * izračunljiv, zadani vektor mora imati jednak broj dimenzija kao ovaj
	 * vektor.
	 *
	 * @param other
	 *            drugi operand skalarnog produkta vektora
	 * @return
	 */
	public double calcScalarProduct(Vector other) {
		if (other == null) {
			throw new IllegalArgumentException("Null is not legal argument for this method.");
		}
		if (components.length != other.components.length) {
			throw new IllegalArgumentException(
					"Unable to calculate scalar product of components with unequal number of dimensions.");
		}
		double product = 0;

		for (int i = 0, n = components.length; i < n; i++) {
			product += components[i] * other.components[i];
		}
		return product;
	}

	/**
	 * Vraća duljinu vektora.
	 *
	 * @return duljina vektora
	 */
	public double getNorm() {
		double sum = 0;
		for (double comp : components) {
			sum += comp * comp;
		}
		return Math.sqrt(sum);
	}

	/**
	 * Vraća broj dimenzija vektora.
	 *
	 * @return broj dimenzija
	 */
	public int getNumOfDimensions() {
		return components.length;
	}

	/**
	 * Vraća polje komponenata vektora.
	 *
	 * @return polje komponenata vektora
	 */
	public double[] getComponents() {
		return Arrays.copyOf(components, components.length);
	}
}
