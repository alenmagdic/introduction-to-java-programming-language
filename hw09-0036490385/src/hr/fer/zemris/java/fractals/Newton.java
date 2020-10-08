package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * This program is a Newton-Raphson iteration-based fractal viewer. It expects
 * from user to input the roots of some complex polynomial and then creates a
 * new window that displays a Newton-Raphson iteration-based fractal for the
 * specified complex polynomial. When the window shows, user can interact with
 * it by selecting an area that he wants to see better, i.e. the program will
 * provide a detail view into the selected area. In order for fractal to be
 * calculated, there has to be at least to roots given by user, otherwise the
 * program won't calculate the fractal.
 *
 * @author Alen Magdiæ
 *
 */
public class Newton {

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		Scanner sc = new Scanner(System.in);
		int inputIndex = 1;
		List<Complex> roots = new ArrayList<>();
		while (true) {
			System.out.print("Root " + inputIndex + "> ");

			String input = sc.nextLine().trim();
			if (input.equals("done")) {
				break;
			}

			try {
				roots.add(parseComplex(input));
				inputIndex++;
			} catch (NumberFormatException ex) {
				System.out.println("Invalid input!");
			}
		}

		sc.close();
		if (roots.size() >= 2) {
			System.out.println("Image of fractal will appear shortly. Thank you.");
		} else {
			System.out.println("There are not enough roots, expected at least two roots.");
			return;
		}

		FractalViewer.show(new MyProducer(roots));
	}

	/**
	 * This class represents a single job that is to be done by one of the
	 * threads.
	 *
	 * @author Alen Magdiæ
	 *
	 */
	private static class CalculationJob implements Callable<Void> {
		/**
		 * Minimum real part of complex number that is to be included in display
		 * of fractal.
		 **/
		private double reMin;
		/**
		 * Maximum real part of complex number that is to be included in display
		 * of fractal.
		 **/
		private double reMax;
		/**
		 * Minimum imaginary part of complex number that is to be included in
		 * display of fractal.
		 **/
		private double imMin;
		/**
		 * Maximum imaginary part of complex number that is to be included in
		 * display of fractal.
		 **/
		private double imMax;
		/**
		 * Display width.
		 **/
		private int width;
		/**
		 * Display height.
		 **/
		private int height;
		/**
		 * Minimum y coordinate that is included in this job.
		 */
		private int yMin;
		/**
		 * Maximum y coordinate that is included in this job.
		 */
		private int yMax;
		/**
		 * Maximum number of iterations before stopping the calculation of
		 * Z(n+1).
		 */
		private int maxIterations;
		/**
		 * Determines when does |Z(n+1)-Z(n)| become adequately small.
		 */
		private double convergenceTreshold;
		/**
		 * Derivation of the specified complex polynomial.
		 */
		private ComplexPolynomial derived;
		/**
		 * The complex polynomial used to calculate a fractal.
		 */
		private ComplexRootedPolynomial polynomialRooted;
		/**
		 * Array of root indexes. Every element of this array represents a
		 * single complex number, but instead of a complex number C=Z(0), there
		 * is an index of the closest root to the Z(n).
		 */
		private short[] data;

		/**
		 * Constructor.
		 *
		 * @param reMin
		 *            minimum real part of complex number that is to be included
		 *            in display of fractal
		 * @param reMax
		 *            maximum real part of complex number that is to be included
		 *            in display of fractal
		 * @param imMin
		 *            minimum imaginary part of complex number that is to be
		 *            included in display of fractal
		 * @param imMax
		 *            maximum imaginary part of complex number that is to be
		 *            included in display of fractal
		 * @param width
		 *            display width
		 * @param height
		 *            display height
		 * @param yMin
		 *            minimum y coordinate that is to be included in this job.
		 * @param yMax
		 *            maximum y coordinate that is to be included in this job.
		 * @param maxIterations
		 *            maximum number of iterations before stopping the
		 *            calculation of Z(n+1).
		 * @param data
		 *            array that will be used to store the job results, i.e.
		 *            indexes of the closest roots
		 * @param polynomial
		 *            the complex polynomial used to calculate a fractal
		 * @param convergenceTreshold
		 *            determines when does |Z(n+1)-Z(n)| become adequately
		 *            small.
		 */
		public CalculationJob(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, int maxIterations, short[] data, ComplexRootedPolynomial polynomial,
				double convergenceTreshold) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.maxIterations = maxIterations;
			this.data = data;
			this.derived = polynomial.toComplexPolynom().derive();
			this.polynomialRooted = polynomial;
			this.convergenceTreshold = convergenceTreshold;
		}

		@Override
		public Void call() throws Exception {
			calculate();
			return null;
		}

		/**
		 * Does the calculation. It calculates the indexes of the closest roots
		 * for the specified set of complex numbers (this set is not directly
		 * specified, but through various arguments in constructor, such as
		 * yMin,yMax,reMin,reMax,etc.).
		 *
		 */
		private void calculate() {
			int offset = width * yMin;
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
					double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
					Complex zn = new Complex(cre, cim);
					int iter = 0;
					Complex zn1;
					double module;
					do {
						Complex numerator = polynomialRooted.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);
						zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;
						iter++;
					} while (module > convergenceTreshold && iter < maxIterations);

					int index = polynomialRooted.indexOfClosestRootFor(zn1, 0.002);

					if (index == -1) {
						data[offset++] = 0;
					} else {
						data[offset++] = (short) (index + 1);
					}
				}
			}
		}

	}

	/**
	 * An implementation of fractal producer. It executes the calculation of the
	 * specified fractal (specified by roots of a complex polynomial) and calls
	 * the graphic user interface so that the calculated fractal gets displayed
	 * in a newly opened window.
	 *
	 * @author Alen Magdiæ
	 *
	 */
	private static class MyProducer implements IFractalProducer {
		/** A polynomial used to calculate a fractal. **/
		private ComplexPolynomial polynom;
		/** A polynomial used to calculate a fractal. **/
		private ComplexRootedPolynomial polynomRooted;
		/**
		 * Determines when does |Z(n+1)-Z(n)| become adequately small.
		 **/
		private static final double CONVERGENCE_TRESHOLD = 1E-3;

		/**
		 * Constructor.
		 *
		 * @param roots
		 *            list of roots of polynomial that is to be used to
		 *            calculate a fractal
		 */
		public MyProducer(List<Complex> roots) {
			polynomRooted = new ComplexRootedPolynomial(roots.toArray(new Complex[0]));
			polynom = polynomRooted.toComplexPolynom();
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer) {
			System.out.println("Starting calculation...");

			int maxIterations = 64;
			short[] data = new short[width * height];
			final int numOfTracks = 16;
			int trackHeight = height / numOfTracks;

			ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < numOfTracks; i++) {
				int yMin = i * trackHeight;
				int yMax = (i + 1) * trackHeight - 1;
				if (i == numOfTracks - 1) {
					yMax = height - 1;
				}
				CalculationJob job = new CalculationJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax,
						maxIterations, data, polynomRooted, CONVERGENCE_TRESHOLD);
				results.add(pool.submit(job));
			}

			long before = Complex.counter;
			for (Future<Void> posao : results) {
				try {
					posao.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}

			pool.shutdown();
			System.out.println(Complex.counter - before);
			System.out.println("Calculation done. Calling observer, i.e. GUI.");
			observer.acceptResult(data, (short) (polynom.order() + 1), requestNo);

		}

	}

	/**
	 * Parses the specified complex number. If the specified string does not
	 * contain a representation of a valid complex number, it throws
	 * {@link NumberFormatException}. A general example of acceptable argument:
	 * 'a+ib'. Coeffients a and b are real numbers, representing the real and
	 * the imaginary part of a complex number. So, it is expected for the
	 * imaginary part to come after the imaginary unit. The method will also
	 * accept arguments that include only real or only imaginary part or
	 * arguments that do not contain any coeffient next to the imaginary unit
	 * (then the imaginary part is set to 1).
	 *
	 * @param complex
	 *            a string containing a complex number
	 * @return a {@link Complex} number
	 * @throws NumberFormatException
	 *             if the specified string does not contain a complex number
	 */
	private static Complex parseComplex(String complex) throws NumberFormatException {
		// removing blanks
		StringBuilder stringB = new StringBuilder();
		for (int i = 0, n = complex.length(); i < n; i++) {
			char c = complex.charAt(i);
			if (c != ' ' && c != '\t') {
				if ((Character.isDigit(c) || c == '.') && stringB.length() > 0
						&& (complex.charAt(i - 1) == ' ' || complex.charAt(i - 1) == '\t')
						&& (Character.isDigit(stringB.charAt(stringB.length() - 1))
								|| stringB.charAt(stringB.length() - 1) == '.')) {
					throw new NumberFormatException("There are to many numbers.");
				}
				stringB.append(c);
			}
		}
		complex = stringB.toString();

		int indexOfI = complex.indexOf('i');
		if (indexOfI == -1) {
			return new Complex(Double.parseDouble(complex), 0);
		}

		double real;
		if (indexOfI == 0 || indexOfI == 1) {
			real = 0;
		} else {
			real = Double.parseDouble(complex.substring(0, indexOfI - 1));
		}

		int signOfImag;
		if (indexOfI == 0) {
			signOfImag = 1;
		} else {
			char c = complex.charAt(indexOfI - 1);
			if (c == '-') {
				signOfImag = -1;
			} else if (c == '+') {
				signOfImag = 1;
			} else {
				throw new NumberFormatException("Unexpected character at index" + (indexOfI - 1) + ". Character: " + c);
			}
		}

		if (indexOfI == complex.length() - 1) {
			return new Complex(real, signOfImag * 1);
		}
		return new Complex(real, signOfImag * Double.parseDouble(complex.substring(indexOfI + 1)));
	}
}
