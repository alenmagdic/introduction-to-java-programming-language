package hr.fer.zemris.java.custom.scripting.exec;

/**
 * This class wraps an object. It provides some methods that can execute some
 * arithmetic operations if the wrapped object is a number. So, it executes this
 * operations with the value wrapped by this wrapper as the first operand, and
 * the specified number as the second operand. The value that is wrapped is not
 * read-only, i.e. there is a value setter so that it can be changed eventually.
 * The supported objects for arithmetic operations are {@link Integer},
 * {@link Double}, a {@link String} that contains a number and a null reference
 * which represents a number 0. If the operands of an arithmetic operation are
 * both integers, the result of the operation is an integer. Otherwise the
 * result of the operation is a double.
 *
 * @author Alen Magdić
 *
 */
public class ValueWrapper {
	/**
	 * A value that is wrapped by this wrapper.
	 */
	private Object value;

	/**
	 * Constructor.
	 *
	 * @param value
	 *            a value that is to be wrapped by this wrapper
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Sets a new value that is to be wrapped.
	 *
	 * @param value
	 *            a new value that is to be wrapped
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Gets the value that is wrapped by this wrapper.
	 *
	 * @return the value that is wrapped by this wrapper
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Executes an operation of addition with the value wrapped by this wrapper
	 * as the first operand, and the specified value as the second operand. If
	 * the given object does not contain a supported value, a
	 * {@link RuntimeException} is thrown. The supported objects are
	 * {@link Integer}, {@link Double}, a {@link String} that contains a number
	 * or a null reference which represents a number 0.
	 *
	 * @param incValue
	 *            the second operand of addition
	 */
	public void add(Object incValue) {
		validateArgument(incValue);
		if (getResultType(incValue) == ResultType.INTEGER) {
			value = getAsInteger(value) + getAsInteger(incValue);
		} else {
			value = getAsDouble(value) + getAsDouble(incValue);
		}
	}

	/**
	 * Executes an operation of subtraction with the value wrapped by this
	 * wrapper as the first operand, and the specified value as the second
	 * operand. If the given object does not contain a supported value, a
	 * {@link RuntimeException} is thrown. The supported objects are
	 * {@link Integer}, {@link Double}, a {@link String} that contains a number
	 * or a null reference which represents a number 0.
	 *
	 * @param incValue
	 *            the second operand of subtraction
	 */
	public void subtract(Object decValue) {
		validateArgument(decValue);
		if (getResultType(decValue) == ResultType.INTEGER) {
			value = getAsInteger(value) - getAsInteger(decValue);
		} else {
			value = getAsDouble(value) - getAsDouble(decValue);
		}
	}

	/**
	 * Executes an operation of multiplication with the value wrapped by this
	 * wrapper as the first operand, and the specified value as the second
	 * operand. If the given object does not contain a supported value, a
	 * {@link RuntimeException} is thrown. The supported objects are
	 * {@link Integer}, {@link Double}, a {@link String} that contains a number
	 * or a null reference which represents a number 0.
	 *
	 * @param incValue
	 *            the second operand of multiplication
	 */
	public void multiply(Object mulValue) {
		validateArgument(mulValue);
		if (getResultType(mulValue) == ResultType.INTEGER) {
			value = getAsInteger(value) * getAsInteger(mulValue);
		} else {
			value = getAsDouble(value) * getAsDouble(mulValue);
		}
	}

	/**
	 * Executes an operation of division with the value wrapped by this wrapper
	 * as the first operand, and the specified value as the second operand. If
	 * the given object does not contain a supported value, a
	 * {@link RuntimeException} is thrown. The supported objects are
	 * {@link Integer}, {@link Double}, a {@link String} that contains a number
	 * or a null reference which represents a number 0.
	 *
	 * @param divValue
	 *            the second operand of division
	 * @throws ArithmeticException
	 *             if the specified operand is a number 0
	 */
	public void divide(Object divValue) throws ArithmeticException {
		validateArgument(divValue);

		if (getAsDouble(divValue) == 0) {
			throw new ArithmeticException("Can not divide by zero!");
		}

		if (getResultType(divValue) == ResultType.INTEGER) {
			value = getAsInteger(value) / getAsInteger(divValue);
		} else {
			value = getAsDouble(value) / getAsDouble(divValue);
		}
	}

	/**
	 * Executes an operation of comparison with the value wrapped by this
	 * wrapper as the first operand, and the specified value as the second
	 * operand. If the given object does not contain a supported value, a
	 * {@link RuntimeException} is thrown. The supported objects are
	 * {@link Integer}, {@link Double}, a {@link String} that contains a number
	 * or a null reference which represents a number 0. If the first operand is
	 * greater than the second one, a positive number is returned. If the
	 * operands are equal, 0 is returned. Otherwise, a negative number is
	 * returned.
	 *
	 * @param withValue
	 *            the second operand of comparison
	 * @return positive number if the first operand is greater, 0 if the
	 *         operands are equal, a negative number otherwise
	 */
	public int numCompare(Object withValue) {
		validateArgument(withValue);

		if (getResultType(withValue) == ResultType.INTEGER) {
			return Integer.compare(getAsInteger(value), getAsInteger(withValue));
		}

		return Double.compare(getAsDouble(value), getAsDouble(withValue));
	}

	/**
	 * Detects the result type of an arithmetic operation with the value wrapped
	 * by this wrapper as the first operation, and the specified value as the
	 * second operand. The result type is specified by {@link ResultType} enum.
	 * If both operands are integers, then the result type is integer. Otherwise
	 * the result type is double.
	 *
	 * @param operand
	 *            the second operand of an arithmetic operation
	 * @return the result type of an arithmetic operation with the specified
	 *         operands
	 */
	private ResultType getResultType(Object operand) {
		if (isInteger(value) && isInteger(operand)) {
			return ResultType.INTEGER;
		} else {
			return ResultType.DOUBLE;
		}
	}

	/**
	 * Returns a number of type double from the specified object. The specified
	 * object can be {@link Integer}, {@link Double}, a {@link String} that
	 * contains a number or a null reference which represents a number 0.
	 *
	 * @param object
	 *            an object that contains a number
	 * @return a double extracted from the specified object
	 */
	private double getAsDouble(Object object) {
		if (object == null) {
			return 0.0;
		}

		if (object instanceof String) {
			return Double.parseDouble((String) object);
		}

		if (object instanceof Integer) {
			return ((Integer) object).doubleValue();
		}

		return (double) object;
	}

	/**
	 * Returns a number of type int from the specified object. The specified
	 * object can be {@link Integer}, a {@link String} that contains an integer
	 * or a null reference which represents a number 0.
	 *
	 * @param object
	 *            an object that contains an integer
	 * @return a integer extracted from the specified object
	 */
	private int getAsInteger(Object object) {
		if (object == null) {
			return 0;
		}

		if (object instanceof String) {
			return Integer.parseInt((String) object);
		}

		return (int) object;
	}

	/**
	 * Checks if the specified argument is valid. It is valid if it is
	 * {@link Integer}, {@link Double}, a {@link String} that contains a number
	 * or a null reference which represents a number 0. If the argument is not
	 * valid, a {@link RuntimeException} is thrown. *
	 *
	 * @param argument
	 *            an argument that is to be checked
	 */
	private void validateArgument(Object argument) {
		if (argument == null) {
			return;
		}

		if (argument instanceof String) {
			try {
				Double.parseDouble((String) argument);
				return;
			} catch (NumberFormatException ex) {
				throw new RuntimeException("The specified value can not be interpreted as a number.");
			}
		}

		if (!(argument instanceof Double || argument instanceof Integer)) {
			throw new RuntimeException(
					"Invalid argument. A ValueWrapper object can store values of type Integer or Double or a String that can be interpreted as a number.");
		}
	}

	/**
	 * This method checks if the specified object can be interpreted as an
	 * integer value. There are two acceptable cases: object is an instance of
	 * Integer class or object is an instance of String class but it's content
	 * can be interpreted as an integer.
	 *
	 * @param object
	 *            object that is to be checked
	 * @return true if the specified object can be interpreted as an integer
	 *         value
	 */
	private boolean isInteger(Object object) {
		if (object instanceof Integer || object == null) {
			return true;
		}

		if (object instanceof String) {
			try {
				Integer.parseInt((String) object);
				return true;
			} catch (NumberFormatException ex) {
				return false;
			}
		}

		return false;
	}

	/** An enum that represents a type of result of an arithmetic operation. **/
	private enum ResultType {
		/** Represents a result of type Integer. **/
		INTEGER,
		/** Represents a result of type Double. **/
		DOUBLE;
	}
}
