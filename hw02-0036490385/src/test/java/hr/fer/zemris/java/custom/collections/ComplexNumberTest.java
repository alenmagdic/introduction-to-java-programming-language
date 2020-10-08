package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ComplexNumberTest {

	@Test
	public void fromReal() {
		ComplexNumber num = ComplexNumber.fromReal(15.23);
		assertEquals(15.23, num.getReal(), 1e-6);
		assertEquals(0, num.getImaginary(), 1e-6);
	}

	@Test
	public void fromImaginary() {
		ComplexNumber num = ComplexNumber.fromImaginary(15.23);
		assertEquals(0, num.getReal(), 1e-6);
		assertEquals(15.23, num.getImaginary(), 1e-6);
	}

	@Test
	public void fromMagnitudeAndAngle() {
		ComplexNumber num = ComplexNumber.fromMagnitudeAndAngle(56, 0.47);
		assertEquals(49.9278241, num.getReal(), 1e-3);
		assertEquals(25.3616319, num.getImaginary(), 1e-3);
	}

	@Test
	public void parseFullExpression() {
		ComplexNumber num = ComplexNumber.parse("25.25+14.14i");
		assertEquals(25.25, num.getReal(), 1e-6);
		assertEquals(14.14, num.getImaginary(), 1e-6);
	}

	@Test
	public void parseRealNumber() {
		ComplexNumber num = ComplexNumber.parse("34.18");
		assertEquals(34.18, num.getReal(), 1e-6);
		assertEquals(0, num.getImaginary(), 1e-6);
	}

	@Test
	public void parseOnlyImaginaryPart() {
		ComplexNumber num = ComplexNumber.parse("34.18i");
		assertEquals(0, num.getReal(), 1e-6);
		assertEquals(34.18, num.getImaginary(), 1e-6);
	}

	@Test
	public void parseI() {
		ComplexNumber num = ComplexNumber.parse("i");
		assertEquals(0, num.getReal(), 1e-6);
		assertEquals(1, num.getImaginary(), 1e-6);
	}

	@Test
	public void parse15PlusI() {
		ComplexNumber num = ComplexNumber.parse("15+i");
		assertEquals(15, num.getReal(), 1e-6);
		assertEquals(1, num.getImaginary(), 1e-6);
	}

	@Test
	public void parseInvalidExpression() {
		try {
			ComplexNumber.parse("15k+i");
			fail();
		} catch (NumberFormatException ex) {
		}
	}

	@Test
	public void getReal() {
		ComplexNumber num = new ComplexNumber(25.42, 348.16);
		assertEquals(25.42, num.getReal(), 1e-6);
	}

	@Test
	public void getImaginary() {
		ComplexNumber num = new ComplexNumber(25.42, 348.16);
		assertEquals(348.16, num.getImaginary(), 1e-6);
	}

	@Test
	public void getMagnitude() {
		ComplexNumber num = new ComplexNumber(25.42, 348.16);
		assertEquals(349.0867542, num.getMagnitude(), 1e-3);
	}

	@Test
	public void getAngle() {
		ComplexNumber num = new ComplexNumber(225.42, 348.16);
		assertEquals(0.9962081, num.getAngle(), 1e-3);
	}

	@Test
	public void getAngle2() {
		ComplexNumber num = new ComplexNumber(-225.42, -348.16);
		assertEquals(4.1378007, num.getAngle(), 1e-3);
	}

	@Test
	public void add() {
		ComplexNumber num1 = new ComplexNumber(-225.42, -348.16);
		ComplexNumber num2 = new ComplexNumber(245.42, 358.16);
		ComplexNumber res = num1.add(num2);
		assertEquals(20, res.getReal(), 1e-3);
		assertEquals(10, res.getImaginary(), 1e-3);
	}

	@Test
	public void addNull() {
		ComplexNumber num1 = new ComplexNumber(-225.42, -348.16);
		try {
			num1.add(null);
			fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void sub() {
		ComplexNumber num1 = new ComplexNumber(15, 36);
		ComplexNumber num2 = new ComplexNumber(5, 6);
		ComplexNumber res = num1.sub(num2);
		assertEquals(10, res.getReal(), 1e-3);
		assertEquals(30, res.getImaginary(), 1e-3);
	}

	@Test
	public void subNull() {
		ComplexNumber num = new ComplexNumber(15, 36);
		try {
			num.sub(null);
			fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void mul() {
		ComplexNumber num1 = new ComplexNumber(-1, -5);
		ComplexNumber num2 = new ComplexNumber(10, 12);
		ComplexNumber res = num1.mul(num2);
		assertEquals(50, res.getReal(), 1e-3);
		assertEquals(-62, res.getImaginary(), 1e-3);
	}

	@Test
	public void div() {
		ComplexNumber num1 = new ComplexNumber(3, 13);
		ComplexNumber num2 = new ComplexNumber(7, 17);
		ComplexNumber res = num1.div(num2);
		assertEquals(0.7159763, res.getReal(), 1e-3);
		assertEquals(0.1183431, res.getImaginary(), 1e-3);
	}

	@Test
	public void divNull() {
		ComplexNumber num = new ComplexNumber(3, 13);
		try {
			num.div(null);
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void divByZero() {
		ComplexNumber num1 = new ComplexNumber(3, 13);
		ComplexNumber num2 = new ComplexNumber(0, 0);
		try {
			num1.div(num2);
		} catch (ArithmeticException ex) {
		}

	}

	@Test
	public void power() {
		ComplexNumber num = new ComplexNumber(2, 3);
		ComplexNumber res = num.power(3);
		assertEquals(-46, res.getReal(), 1e-3);
		assertEquals(9, res.getImaginary(), 1e-3);

	}

	@Test
	public void powerWithNegativeExponent() {
		ComplexNumber num = new ComplexNumber(2, 3);
		try {
			num.power(-3);
		} catch (IllegalArgumentException ex) {
		}

	}

	@Test
	public void root() {
		ComplexNumber num = new ComplexNumber(2, 3);
		ComplexNumber[] res = num.root(3);
		assertEquals(3, res.length);
		assertEquals(1.4518566, res[0].getReal(), 1e-3);
		assertEquals(0.4934035, res[0].getImaginary(), 1e-3);
		assertEquals(-1.1532283, res[1].getReal(), 1e-3);
		assertEquals(1.0106429, res[1].getImaginary(), 1e-3);
		assertEquals(-0.2986283, res[2].getReal(), 1e-3);
		assertEquals(-1.5040464, res[2].getImaginary(), 1e-3);
	}

	@Test
	public void toStringTest() {
		ComplexNumber num = new ComplexNumber(2.52, 3.14);
		assertEquals("2.52 + 3.14i", num.toString());
	}

}
