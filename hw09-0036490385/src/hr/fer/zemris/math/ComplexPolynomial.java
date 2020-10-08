package hr.fer.zemris.math;

/**
 * This class is a representation of a complex polynomial. It contains methods
 * for multiplication and derivation of polynomials and also a method that
 * evaluates this polynomial for the specified value.
 *
 * @author Alen Magdiæ
 *
 */
public class ComplexPolynomial {
	/** Polynomial factors **/
	private Complex[] factors;

	/**
	 * Constructor.
	 *
	 * @param factors
	 *            polynomial factors
	 */
	public ComplexPolynomial(Complex... factors) {
		if (factors == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		} else if (factors.length == 0) {
			throw new IllegalArgumentException("A polynomial should have at least one factor.");
		}
		this.factors = factors;
	}

	/**
	 * Returns the order of this polynomial.
	 *
	 * @return the order of this polynomial
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Returns a new complex polynomial that is the result of multiplication of
	 * this and the specified polynomial.
	 *
	 * @param p
	 *            the other operand of polynomial multiplication
	 * @return a new complex polynomial that is the result of multiplication of
	 *         this and the specified polynomial
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] factors = new Complex[order() + p.order() + 1];
		for (int i = 0; i < factors.length; i++) {
			factors[i] = Complex.ZERO;
		}

		for (int i = 0; i < this.factors.length; i++) {
			for (int j = 0; j < p.factors.length; j++) {
				int exponent = i + j;
				factors[exponent] = factors[exponent].add(this.factors[i].multiply(p.factors[j]));
			}
		}

		return new ComplexPolynomial(factors);
	}

	/**
	 * Returns a new complex polynomial that is the result of derivating this
	 * complex polynomial.
	 *
	 * @return a new complex polynomial that is the result of derivating this
	 *         complex polynomial
	 */
	public ComplexPolynomial derive() {
		if (this.factors.length == 1) {
			return new ComplexPolynomial(Complex.ZERO);
		}
		Complex[] factors = new Complex[this.factors.length - 1];

		for (int i = this.factors.length - 1; i > 0; i--) {
			factors[i - 1] = this.factors[i].multiply(new Complex(i, 0));
		}

		return new ComplexPolynomial(factors);
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
		if (factors.length == 0) {
			return Complex.ZERO;
		}

		Complex result = factors[0];
		Complex coeff = z; // coeff = z^i
		for (int i = 1; i < factors.length; i++) {
			result = result.add(factors[i].multiply(coeff));
			coeff = coeff.multiply(z);
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder stringB = new StringBuilder();
		for (int i = factors.length - 1; i >= 0; i--) {
			if (factors[i].equals(Complex.ZERO)) {
				continue;
			}
			if (i < factors.length - 1) {
				stringB.append(" + ");
			}
			stringB.append("(" + factors[i] + ")");
			if (i > 0) {
				stringB.append("z^" + i);
			}
		}
		return stringB.toString();
	}
}
