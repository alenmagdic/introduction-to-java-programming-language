package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Klasa koja omata objekt. Ako se pohranjeni objekt može protumačiti kao broj,
 * klasa omogućuje korištenje metoda koje izvode aritmetičke operacije kao i
 * metodu usporedbe. Da bi korištenje tih metoda bilo moguća, omotani objekt
 * mora biti ili {@link Integer} ili {@link Double} ili null (pri čemu se on
 * tretira kao broj 0) ili {@link String} koji se može parsirati u
 * {@link Integer} ili u {@link Double}. Pri izvođenju aritmetičkih operacija,
 * ako je barem jedan od operanada realni broj, tada će i rezultat biti realni
 * broj. Samo pri računanju dva cijela broja rezultat je cijeli broj.
 *
 * @author Alen Magdić
 *
 */
public class ValueWrapper {
	/**
	 * Objekt omotan ovim omotačem.
	 */
	private Object value;

	/**
	 * Konstruktor.
	 *
	 * @param value
	 *            objekt koji treba omotati ovim omotačem
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Postavlja novi objekt koji će biti omotan ovim omotačem.
	 *
	 * @param value
	 *            novi objekt kojeg treba omotati
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Vraća objekt omotan ovim omotačem.
	 *
	 * @return objekt omotan ovim omotačem
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Izvodi operaciju zbrajanja gdje je objekt omotan ovim omotačem prvi
	 * operand, a drugi operand je zadan. Ako se dani objekt ne može protumačiti
	 * kao broj, baca se {@link RuntimeException}. Podržani objekti su
	 * {@link Integer}, {@link Double},<code>null</code> (vrijednost 0), i
	 * {@link String} koji se može parsirati u {@link Double} ili
	 * {@link Integer}.
	 *
	 * @param incValue
	 *            drugi operand zbrajanja
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
	 * Izvodi operaciju oduzimanja gdje je objekt omotan ovim omotačem prvi
	 * operand, a drugi operand je zadan. Ako se dani objekt ne može protumačiti
	 * kao broj, baca se {@link RuntimeException}. Podržani objekti su
	 * {@link Integer}, {@link Double},<code>null</code> (vrijednost 0), i
	 * {@link String} koji se može parsirati u {@link Double} ili
	 * {@link Integer}.
	 *
	 * @param incValue
	 *            drugi operand oduzimanja
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
	 * Izvodi operaciju množenja gdje je objekt omotan ovim omotačem prvi
	 * operand, a drugi operand je zadan. Ako se dani objekt ne može protumačiti
	 * kao broj, baca se {@link RuntimeException}. Podržani objekti su
	 * {@link Integer}, {@link Double},<code>null</code> (vrijednost 0), i
	 * {@link String} koji se može parsirati u {@link Double} ili
	 * {@link Integer}.
	 *
	 * @param incValue
	 *            drugi operand množenja
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
	 * Izvodi operaciju djeljenja gdje je objekt omotan ovim omotačem prvi
	 * operand, a drugi operand je zadan. Ako se dani objekt ne može protumačiti
	 * kao broj, baca se {@link RuntimeException}. Podržani objekti su
	 * {@link Integer}, {@link Double},<code>null</code> (vrijednost 0), i
	 * {@link String} koji se može parsirati u {@link Double} ili
	 * {@link Integer}.
	 *
	 * @param divValue
	 *            drugi operand djeljenja
	 * @throws ArithmeticException
	 *             ako je drugi operand 0
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
	 * Izvodi operaciju zbrajanja gdje je objekt omotan ovim omotačem prvi
	 * operand, a drugi operand je zadan. Ako se dani objekt ne može protumačiti
	 * kao broj, baca se {@link RuntimeException}. Podržani objekti su
	 * {@link Integer}, {@link Double},<code>null</code> (vrijednost 0), i
	 * {@link String} koji se može parsirati u {@link Double} ili
	 * {@link Integer}. Ako je prvi operand veći, vraća se pozitivan broj. Ako
	 * su jednaki vraća se 0. Inače se vraća negativan broj.
	 *
	 * @param withValue
	 *            drugi operand usporedbe
	 * @return pozitivan broj ako je prvi broj veći, 0 ako su jednaki, inače
	 *         negativan broj
	 */
	public int numCompare(Object withValue) {
		validateArgument(withValue);

		if (getResultType(withValue) == ResultType.INTEGER) {
			return Integer.compare(getAsInteger(value), getAsInteger(withValue));
		}

		return Double.compare(getAsDouble(value), getAsDouble(withValue));
	}

	/**
	 * Detektira tip rezultat pri izvođenju operacije između objekta omotanog
	 * ovim omotačem kao prvim operandom i zadanog objekta kao drugim operandom.
	 * Ako je barem jedan od njih {@link Double}, tip rezultata će biti
	 * {@link Double}. Inače će tip rezultati biti {@link Integer}.
	 *
	 * @param operand
	 *            drugi operand aritmetičke operacije
	 * @return tip rezultata aritmetičke operacije između objekta omotanog ovim
	 *         omotačem kao prvim operandom i zadanog objekta kao drugim
	 *         operandom
	 */
	private ResultType getResultType(Object operand) {
		if (isInteger(value) && isInteger(operand)) {
			return ResultType.INTEGER;
		} else {
			return ResultType.DOUBLE;
		}
	}

	/**
	 * Vraća broj tipa double nastao iz zadanog objekta. Da bi to bilo moguće,
	 * zadani objekt mora biti ili tipa {@link Integer} ili tipa {@link Double}
	 * ili <code>null</code> (pri čemu se vraća 0.0) ili {@link String} koji se
	 * može parsirati u {@link Double}.
	 *
	 * @param object
	 *            objekt koji sadrži broj
	 * @return broj tipa double nastao iz zadanog objekta
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
	 * Vraća broj tipa int nastao iz zadanog objekta. Da bi to bilo moguće,
	 * zadani objekt mora biti ili {@link Integer} ili <code>null</code> (pri
	 * čemu se vraća vrijednost 0) ili {@link String} koji se može parsirati u
	 * {@link Integer}.
	 *
	 * @param object
	 *            objekt koji sadrži cijeli broj
	 * @return broj tipa int nastao iz zadanog objekta
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
	 * Provjerava je li zadani argument ispravan za izvođenje aritmetičkih
	 * operacija. Da bi bio ispravan zadani objekt mora biti ili tipa
	 * {@link Integer} ili tipa {@link Double} ili <code>null</code> ili
	 * {@link String} koji se može parsirati u {@link Double}. Ako zadani
	 * argument nije ispravan, baca se {@link RuntimeException}.
	 *
	 * @param argument
	 *            argument koji treba provjeriti
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
	 * Metoda provjerava može li se zadani objekt protumačiti kao cijeli broj.
	 * Postoje dva prihvatljiva slučaja: objekt je instanca klase
	 * {@link Integer} ili je {@link String} koji se može parsirati u
	 * {@link Integer}.
	 *
	 * @param object
	 *            objekt koji treba provjeriti
	 * @return true ako se zadani objekt može protumačiti kao cijeli broj
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

	/** Enum koji predstavlja tip rezultata aritmetičke operacije. **/
	private enum ResultType {
		/** Reprezentira rezultat tipa {@link Integer} **/
		INTEGER,
		/** Reprezentira rezultat tipa {@link Double} **/
		DOUBLE;
	}
}
