package hr.fer.zemris.math;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class ComplexTest {

	@Test
	public void getModule() {
		Complex num = new Complex(25.42, 348.16);
		assertEquals(349.0867542, num.module(), 1e-3);
	}

	@Test
	public void add() {
		Complex num1 = new Complex(-225.42, -348.16);
		Complex num2 = new Complex(245.42, 358.16);
		Complex res = num1.add(num2);
		assertEquals(20, res.getReal(), 1e-3);
		assertEquals(10, res.getImaginary(), 1e-3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addNull() {
		Complex num1 = new Complex(-225.42, -348.16);
		num1.add(null);
	}

	@Test
	public void sub() {
		Complex num1 = new Complex(15, 36);
		Complex num2 = new Complex(5, 6);
		Complex res = num1.sub(num2);
		assertEquals(10, res.getReal(), 1e-3);
		assertEquals(30, res.getImaginary(), 1e-3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void subNull() {
		Complex num1 = new Complex(-225.42, -348.16);
		num1.sub(null);
	}

	@Test
	public void mul() {
		Complex num1 = new Complex(-1, -5);
		Complex num2 = new Complex(10, 12);
		Complex res = num1.multiply(num2);
		assertEquals(50, res.getReal(), 1e-3);
		assertEquals(-62, res.getImaginary(), 1e-3);
	}

	@Test
	public void div() {
		Complex num1 = new Complex(3, 13);
		Complex num2 = new Complex(7, 17);
		Complex res = num1.divide(num2);
		assertEquals(0.7159763, res.getReal(), 1e-3);
		assertEquals(0.1183431, res.getImaginary(), 1e-3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void divideNull() {
		Complex num1 = new Complex(-225.42, -348.16);
		num1.divide(null);
	}

	@Test
	public void power() {
		Complex num = new Complex(2, 3);
		Complex res = num.power(3);
		assertEquals(-46, res.getReal(), 1e-3);
		assertEquals(9, res.getImaginary(), 1e-3);

	}

	@Test(expected = IllegalArgumentException.class)
	public void powerWithNegativeExponent() {
		Complex num = new Complex(2, 3);
		num.power(-3);

	}

	@Test
	public void root() {
		Complex num = new Complex(2, 3);
		List<Complex> res = num.root(3);
		assertEquals(3, res.size());
		assertEquals(1.4518566, res.get(0).getReal(), 1e-3);
		assertEquals(0.4934035, res.get(0).getImaginary(), 1e-3);
		assertEquals(-1.1532283, res.get(1).getReal(), 1e-3);
		assertEquals(1.0106429, res.get(1).getImaginary(), 1e-3);
		assertEquals(-0.2986283, res.get(2).getReal(), 1e-3);
		assertEquals(-1.5040464, res.get(2).getImaginary(), 1e-3);
	}

}
