package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents an unmodifiable complex number. It contains methods
 * for calculating with complex numbers, such as method for addition,
 * subtraction, multiplication, divison, etc.
 *
 * @author Alen Magdiæ
 *
 */
public class Complex {
	/** Real part of this complex number. **/
	private double re;
	/** Imaginary part of this complex number. **/
	private double im;
	/** The module of this complex number. **/
	private double module;
	/** Complex number 0 **/
	public static final Complex ZERO = new Complex(0, 0);
	/** Complex number 1 **/
	public static final Complex ONE = new Complex(1, 0);
	/** Complex number -1 **/
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/** Complex number i **/
	public static final Complex IM = new Complex(0, 1);
	/** Complex number -i **/
	public static final Complex IM_NEG = new Complex(0, -1);
	public static long counter;

	/**
	 * Default constructor. Creates a complex number 0.
	 *
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * Constructor.
	 *
	 * @param re
	 *            real part of complex number
	 * @param im
	 *            imaginary part of complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
		module = Math.sqrt(re * re + im * im);
		counter++;
	}

	/**
	 * Gets the module of this complex number.
	 *
	 * @return the module of this complex number
	 */
	public double module() {
		return module;
	}

	/**
	 * Gets a result of multiplication of this complex number with the specified
	 * complex number.
	 *
	 * @param c
	 *            the operand of multiplication
	 * @return a complex number that is the result of multiplication of this and
	 *         the specified complex number
	 */
	public Complex multiply(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
	}

	/**
	 * Gets a result of divison of this complex number by the specified complex
	 * number.
	 *
	 * @param c
	 *            the second operand of division
	 * @return a complex number that is the result of division of this complex
	 *         number by the specified complex number
	 */
	public Complex divide(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		double real = (re * c.re + im * c.im) / (c.re * c.re + c.im * c.im);
		double imag = (im * c.re - re * c.im) / (c.re * c.re + c.im * c.im);
		return new Complex(real, imag);
	}

	/**
	 * Gets a result of addition of this complex number with the specified
	 * complex number.
	 *
	 * @param c
	 *            the operand of addition
	 * @return a complex number that is the result of addition of this and the
	 *         specified complex number
	 */
	public Complex add(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return new Complex(re + c.re, im + c.im);
	}

	/**
	 * Gets a result of subtraction of this complex number with the specified
	 * complex number.
	 *
	 * @param c
	 *            the second operand of subtraction
	 * @return a complex number that is the result of subtraction of this and
	 *         the specified complex number
	 */
	public Complex sub(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return new Complex(re - c.re, im - c.im);
	}

	/**
	 * Gets a complex number that would be given as a result of multiplication
	 * of this complex number with -1.
	 *
	 * @return a complex number that would be given as a result of
	 *         multiplication of this complex number with -1
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/**
	 * Returns Z^n where Z is this complex number. Legal arguments are numbers
	 * that are positive or equal to zero.
	 *
	 * @param n
	 *            an exponent
	 * @return Z^n, where Z is this complex number
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Argument n can not be negative. n: " + n);
		}

		double m = Math.pow(module, n);
		double angleOfThis = Math.atan2(im, re);
		double angle = n * (angleOfThis < 0 ? angleOfThis + 2 * Math.PI : angleOfThis);
		return new Complex(m * Math.cos(angle), m * Math.sin(angle));
	}

	/**
	 * Returns a list of roots of this complex number for the specified degree
	 * of the root.
	 *
	 * @param n
	 *            degree of the root
	 * @return a list of roots of this complex number for the specified degree
	 *         of the root
	 */
	public List<Complex> root(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Argument n can not be negative. n: " + n);
		}

		double m = Math.pow(module, 1.0 / n);
		double angleOfThis = Math.atan2(im, re);
		double angle = angleOfThis < 0 ? angleOfThis + 2 * Math.PI : angleOfThis;
		List<Complex> results = new ArrayList<>();
		for (int k = 0; k < n; k++) {
			results.add(new Complex(m * Math.cos((angle + 2 * k * Math.PI) / n),
					m * Math.sin((angle + 2 * k * Math.PI) / n)));
		}
		return results;
	}

	/**
	 * Gets the real part of this complex number.
	 *
	 * @return the real part of this complex number
	 */
	public double getReal() {
		return re;
	}

	/**
	 * Gets the imaginary part of this complex number.
	 *
	 * @return the imaginary part of this complex number
	 */
	public double getImaginary() {
		return im;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(im);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(re);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Complex)) {
			return false;
		}
		Complex other = (Complex) obj;
		if (Double.doubleToLongBits(im) != Double.doubleToLongBits(other.im)) {
			return false;
		}
		if (Double.doubleToLongBits(re) != Double.doubleToLongBits(other.re)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return re + (im > 0 ? "+" : "-") + Math.abs(im) + "i";
	}
}