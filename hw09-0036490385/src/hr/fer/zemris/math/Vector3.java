package hr.fer.zemris.math;

/**
 * This class is a representation of a three dimensional unmodifiable vector. It
 * contains methods for addition and subtraction of vectors, and also methods
 * that calculate the scalar product and the vector product. There are also
 * methods that return a normalised version of this vector and a method that
 * calculate a cosinus of angle between this and the specified vector.
 *
 * @author Alen Magdiæ
 *
 */
class Vector3 {
	/** X-component of this vector. **/
	private double x;
	/** Y-component of this vector. **/
	private double y;
	/** Z-component of this vector. **/
	private double z;
	/** Norm of this vector. **/
	private double norm;

	/**
	 * Constructor.
	 *
	 * @param x
	 *            x-component of this vector
	 * @param y
	 *            y-component of this vector
	 * @param z
	 *            z-component of this vector
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		norm = Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Norm of this vector.
	 *
	 * @return norm of this vector
	 */
	public double norm() {
		return norm;
	}

	/**
	 * Returns a new vector that is a result of normalisation of this vector.
	 *
	 * @return a new vector that is a result of normalisation of this vector
	 */
	public Vector3 normalized() {
		return scale(1 / norm);
	}

	/**
	 * Gets a result of addition of this vector with the specified vector.
	 *
	 * @param other
	 *            the other operand of addition
	 * @return a new vector that is the result of addition of this vector with
	 *         the specified vector
	 */
	public Vector3 add(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Gets a result of subtraction of this vector with the specified vector.
	 *
	 * @param other
	 *            the second operand of subtraction
	 * @return a new vector that is the result of subtraction of this vector
	 *         with the specified vector
	 */
	public Vector3 sub(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Calculates the scalar product of this and the specified vector.
	 *
	 * @param other
	 *            the other operand of the scalar product
	 * @return the scalar product of this and the specified vector
	 */
	public double dot(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Gets the vector product of this and the specified vector.
	 *
	 * @param other
	 *            the second member of vector product
	 * @return a new vector that is the result of vector product of this vector
	 *         and the specified vector
	 **/
	public Vector3 cross(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return new Vector3(y * other.z - z * other.y, x * other.z - z * other.x, x * other.y - y * other.x);
	}

	/**
	 * Returns the result of multiplication of this vector with the specified
	 * scalar.
	 *
	 * @param s
	 *            a scalar
	 * @return a new vector given as a result of multiplication of this vector
	 *         with the specified scalar
	 */
	public Vector3 scale(double s) {
		return new Vector3(s * x, s * y, s * z);
	}

	/**
	 * Calculates a cosinus of angle between this and the specified vector.
	 *
	 * @param other
	 *            a vector
	 * @return a cosinus of angle between this and the specified vector
	 */
	public double cosAngle(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		return dot(other) / (norm * other.norm);
	}

	/**
	 * Gets the x-component of this vector.
	 *
	 * @return x-component of this vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y-component of this vector.
	 *
	 * @return y-component of this vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the z-component of this vector.
	 *
	 * @return z-component of this vector
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns an array representation of this vector. The array consists of
	 * three elements: x,y and z-component of this vector.
	 *
	 * @return an array containing the x,y and z-component of this vector
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
}