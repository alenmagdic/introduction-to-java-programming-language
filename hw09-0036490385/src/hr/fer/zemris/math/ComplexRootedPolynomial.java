package hr.fer.zemris.math;

/**
 * This class is a representation of a complex rooted polynomial. It is a
 * polynomial with the following structure: (Z-Z0)(Z-Z1)...(Z-Zn), where Zi is a
 * root of the polynomial. This structure can be converted to the typical
 * polynomial strucutre used the toComplexPolynomial method.
 *
 * @author Alen Magdiæ
 *
 */
public class ComplexRootedPolynomial {
	/** Roots of this polynomial **/
	private Complex[] roots;

	/**
	 * Constructor.
	 *
	 * @param roots
	 *            polynomial roots
	 */
	public ComplexRootedPolynomial(Complex... roots) {
		if (roots == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		} else if (roots.length == 0) {
			throw new IllegalArgumentException("Polynom should have at least one root.");
		}
		this.roots = roots;
	}

	/**
	 * Returns the result of evaluation of this polynomial for the specified
	 * complex number.
	 *
	 * @param z
	 *            a complex number
	 * @return the result of evaluation of this polynomial for the specified
	 *         complex number
	 */
	public Complex apply(Complex z) {
		if (z == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		if (roots.length == 0) {
			return Complex.ZERO;
		}

		Complex result = z.sub(roots[0]);
		for (int i = 1; i < roots.length; i++) {
			result = result.multiply(z.sub(roots[i]));
		}
		return result;
	}

	/**
	 * Returns a {@link ComplexPolynomial} version of this polynomial.
	 *
	 * @return a {@link ComplexPolynomial} version of this polynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(roots[0].negate(), Complex.ONE);
		for (int i = 1; i < roots.length; i++) {
			result = result.multiply(new ComplexPolynomial(roots[i].negate(), Complex.ONE));
		}
		return result;
	}

	@Override
	public String toString() {
		if (roots.length == 0) {
			return "";
		}

		StringBuilder stringB = new StringBuilder();
		for (Complex root : roots) {
			Complex rootNeg = root.negate();
			stringB.append("(z" + (rootNeg.toString().startsWith("-") ? "" : "+") + rootNeg + ")");
		}
		return stringB.toString();
	}

	/**
	 * Returns the index of the root that is the closest to the specified
	 * complex number. If there is no any root that is closer than the specified
	 * treshold, -1 is returned.
	 *
	 * @param z
	 *            a complex number
	 * @param treshold
	 *            a treshold that specifies how close does a root have to be in
	 *            order to be considered as possible closest root
	 * @return the index of the root that is the closest to the specified
	 *         complex number
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		if (z == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		if (treshold < 0) {
			throw new IllegalArgumentException("Treshold should be >=0. Treshold: " + treshold);
		}

		int minIndex = -1;
		for (int i = 0; i < roots.length; i++) {
			double distance = Math.abs(roots[i].sub(z).module());
			if (distance <= treshold) {
				if (minIndex == -1 || distance < Math.abs(roots[minIndex].sub(z).module())) {
					minIndex = i;
				}
			}
		}
		return minIndex;
	}
}
