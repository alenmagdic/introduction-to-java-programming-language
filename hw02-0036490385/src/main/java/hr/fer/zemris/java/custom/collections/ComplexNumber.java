package hr.fer.zemris.java.custom.collections;

/**
 * Klasa koja predstavlja kompleksan broj. Sadrži metode za zbrajanje,
 * oduzimanje, dijeljenje, množenje, potenciranje i korjenovanje. Promjena
 * vrijednosti koju objekt predstavlja nije moguća pa svaka od navedenih
 * operacija vraća novi rezultantni objekt kompleksnog broja. Klasa također
 * sadrži razne metode tvornice.
 *
 * @author Alen Magdić
 *
 */
public class ComplexNumber {
	/** Realni dio kompleksnog broja. */
	private double real;
	/** Imaginarni dio kompleksnog broja. */
	private double imaginary;

	/**
	 * Konstruktor. Prima realni i imaginarni dio kompleksnog broja.
	 *
	 * @param real
	 *            realni dio kompleksnog broja
	 * @param imaginary
	 *            imaginarni dio kompleksnog broja
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Metoda tvornica. Stvara kompleksan broj na temelju zadanog realnog
	 * dijela, a kompleksni dio postavlja na nulu.
	 *
	 * @param real
	 *            realni dio kompleksnog broja
	 * @return kompleksan broj sa zadanim realnim dijelom te nulom kao
	 *         imaginarnim dijelom
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}

	/**
	 * Metoda tvornica. Stvara kompleksan broj na temelju zadanog imaginarnog
	 * dijela, a realni dio postavlja na nulu.
	 *
	 * @param imaginary
	 *            imaginarni dio kompleksnog broja
	 * @return kompleksan broj sa zadanim imaginarnim dijelom te nulom kao
	 *         realnim dijelom
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}

	/**
	 * Metoda tvornica. Stvara kompleksan broj na temelju zadane apsolutne
	 * vrijednost te zadanog kuta u radijanima.
	 *
	 * @param magnitude
	 *            apsolutna vrijednost kompleksnog broja
	 * @param angle
	 *            kut kompleksnog broja u radijanima
	 * @return kompleksan broj izveden iz zadanih parametara
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		if (magnitude < 0) {
			throw new IllegalArgumentException("Apsolutna vrijednost kompleksnog broja ne može biti negativan broj.");
		}

		double real = magnitude * Math.cos(angle);
		double imaginary = magnitude * Math.sin(angle);
		return new ComplexNumber(real, imaginary);
	}

	/**
	 * Vraća kompleksni broj konstruiran iz zadanog izraza. Ako je izraz
	 * neispravan, baca se iznimka NumberFormatException. To uključuje i slučaj
	 * kada postoji više od jednog realnog ili više od jednog imaginarnog
	 * dijela, npr. "3+15+12i" ili "3+15i+14i" ili ako sadrži bilo što nakon
	 * znaka 'i'. Metoda će nepotrebne razmake ignorirati te oni neće izazvati
	 * iznimku i izraz će biti korektno parsiran.
	 *
	 * @param expr
	 *            izraz koji predstavlja stringovni zapis kompleksnog broja
	 * @return kompleksni broj konstruiran iz zadanog izraza
	 * @throws NumberFormatException
	 *             u slučaju bilo kakvog pogrešnog izraza, bilo da sadrži nešto
	 *             što se neda izparsirat u broj ili da sadrži višak podataka
	 */
	public static ComplexNumber parse(String expr) throws NumberFormatException {
		if (expr == null) {
			throw new IllegalArgumentException("Metoda ne prihvaća null kao argument");
		}

		StringBuilder stringB = new StringBuilder();
		char[] exprArr = expr.toCharArray();

		double real = 0;
		double imaginary = 0;
		boolean realParsed = false;
		boolean imgParsed = false;
		for (int i = 0; i < exprArr.length; i++) {
			if (exprArr[i] == ' ') {
				continue;
			}
			if ((exprArr[i] == '-' || exprArr[i] == '+') && stringB.toString().length() > 0) {
				// kraj realnog dijela
				real = Double.parseDouble(stringB.toString());
				stringB = new StringBuilder();
				stringB.append(exprArr[i]);
				realParsed = true;
			} else if (exprArr[i] == 'i') { // kraj imaginarnog dijela
				String string = stringB.toString();

				// razmatranje slučaja kada 'i' stoji samostalno
				if (string.length() == 0 || string.length() == 1 && string.equals("+")) {
					imaginary = 1;
				} else if (string.length() == 1 && string.equals("-")) {
					imaginary = -1;
				} else {
					imaginary = Double.parseDouble(stringB.toString());
				}
				imgParsed = true;

				// očekuje se da je 'i' na kraju zapisa - ako ima još nešto osim
				// razmaka, izraz nije ispravan
				if (expr.length() > i + 1 && expr.substring(i + 1).trim().length() > 0) {
					throw new NumberFormatException("Dani izraz sadrži višak podataka.");
				}
				break;
			} else {
				stringB.append(exprArr[i]);
			}
		}

		// ako je dan samo realni broj, prethodna petlja neće detektirati njegov
		// završetak (nema +/- nakon njega) pa on ostane u string builderu bez
		// parsiranja
		if (!realParsed && !imgParsed) {
			real = Double.parseDouble(stringB.toString());
		}

		// detekcija slučaja kada izraz sadrži dva realna dijela, npr.
		// "2.71-3.15"
		if (realParsed && !imgParsed && stringB.toString().length() > 0) {
			throw new NumberFormatException("Dani izraz sadrži višak podataka.");
		}
		return new ComplexNumber(real, imaginary);
	}

	/**
	 * Vraća realni dio ovog kompleksnog broja.
	 *
	 * @return realni dio kompleksnog broja
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Vraća imaginarni dio ovog kompleksnog broja.
	 *
	 * @return imaginarni dio kompleksnog broja
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Vraća apsolutnu vrijednost ovog kompleksnog broja.
	 *
	 * @return apsolutna vrijednost kompleksnog broja
	 */
	public double getMagnitude() {
		return Math.sqrt(Math.pow(real, 2) + Math.pow(imaginary, 2));
	}

	/**
	 * Vraća kut ovog kompleksnog broja.
	 *
	 * @return kut kompleksnog broja
	 */
	public double getAngle() {
		double angle = Math.atan2(imaginary, real);
		return angle >= 0 ? angle : angle + 2 * Math.PI;
	}

	/**
	 * Zbraja ovaj kompleksni broj sa zadanim te vraća rezultantni kompleksni
	 * broj.
	 *
	 * @param c
	 *            kompleksni broj s kojim se zbraja ovaj broj
	 * @return kompleksan broj nastao kao rezultat zbrajanja ovog i zadanog
	 *         kompleksnog broja
	 */
	public ComplexNumber add(ComplexNumber c) {
		if (c == null) {
			throw new IllegalArgumentException("Metoda ne prihvaća null kao argument");
		}
		return new ComplexNumber(real + c.real, imaginary + c.imaginary);
	}

	/**
	 * Oduzima zadani kompleksan broj od ovog kompleksnog broja te vraća
	 * rezultantni kompleksni broj. Dakle, izvršava se operacija ovaj-zadani
	 * broj.
	 *
	 * @param c
	 *            kompleksni broj koji se oduzima od ovog kompleksnog broja
	 * @return kompleksan broj nastao kao rezultat oduzimanja ovog i zadanog
	 *         kompleksnog broja
	 */
	public ComplexNumber sub(ComplexNumber c) {
		if (c == null) {
			throw new IllegalArgumentException("Metoda ne prihvaća null kao argument");
		}
		return new ComplexNumber(real - c.real, imaginary - c.imaginary);
	}

	/**
	 * Množi ovaj sa zadanim kompleksnim brojem te vraća rezultantni kompleksni
	 * broj.
	 *
	 * @param c
	 *            kompleksni broj s kojim se množi ovaj broj
	 * @return rezultat množenja ovoga broja sa zadanim kompleksnim brojem
	 */
	public ComplexNumber mul(ComplexNumber c) {
		if (c == null) {
			throw new IllegalArgumentException("Metoda ne prihvaća null kao argument");
		}
		return fromMagnitudeAndAngle(getMagnitude() * c.getMagnitude(), getAngle() + c.getAngle());
	}

	/**
	 * Dijeli ovaj kompleksni broj sa zadanim kompleksnim brojem te vraća
	 * rezultantni kompleksni broj. U slučaju da je dani kompleksni broj 0, baca
	 * se ArithmeticException.
	 *
	 * @param c
	 *            kompleksni broj s kojim se dijeli ovaj kompleksni broj
	 * @return rezultat dijeljenja ovoga broja sa zadanim kompleksnim brojem
	 */
	public ComplexNumber div(ComplexNumber c) {
		if (c == null) {
			throw new IllegalArgumentException("Metoda ne prihvaća null kao argument");
		}
		if (c.getMagnitude() == 0) {
			throw new ArithmeticException("Dijeljenje s nulom nije moguće.");
		}
		return fromMagnitudeAndAngle(getMagnitude() / c.getMagnitude(), getAngle() - c.getAngle());
	}

	/**
	 * Potencira ovaj kompleksni broj na zadanu potenciju te vraća rezultantni
	 * kompleksni broj. Dozvoljeni su eksponenti veći ili jednaki 0.
	 *
	 * @param n
	 *            eksponent potenciranja
	 * @return rezultat potenciranja ovoga broja na zadanu potenciju
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Eksponent treba biti >=0.");
		}
		return fromMagnitudeAndAngle(Math.pow(getMagnitude(), n), n * getAngle());
	}

	/**
	 * Korjenuje ovaj kompleksni broj na zadani korijen te vraća rezultat u
	 * obliku polja kompleksnih brojeva.
	 *
	 * @param n
	 *            redni broj korijena
	 * @return rezultat korjenovanja ovog kompleksnog broja
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Eksponent treba biti >0.");
		}

		ComplexNumber[] results = new ComplexNumber[n];
		double magn = Math.pow(getMagnitude(), 1.0 / n);
		double ang = getAngle();
		for (int k = 0; k < n; k++) {
			results[k] = fromMagnitudeAndAngle(magn, (ang + 2 * k * Math.PI) / n);
		}
		return results;
	}

	@Override
	public String toString() {
		return real + (imaginary > 0 ? " + " : " - ") + Math.abs(imaginary) + "i";
	}
}
