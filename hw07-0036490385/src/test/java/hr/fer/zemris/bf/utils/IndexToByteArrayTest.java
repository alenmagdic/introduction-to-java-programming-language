package hr.fer.zemris.bf.utils;

import org.junit.Assert;
import org.junit.Test;

import hr.fer.zemris.bf.utils.Util;

public class IndexToByteArrayTest {

	@Test
	public void valueZero() {
		Assert.assertArrayEquals(new byte[] { 0, 0 }, Util.indexToByteArray(0, 2));
	}

	@Test
	public void valueOne() {
		Assert.assertArrayEquals(new byte[] { 0, 1 }, Util.indexToByteArray(1, 2));
	}

	@Test
	public void oneDigitPositiveNumber() {
		Assert.assertArrayEquals(new byte[] { 0, 1, 0, 1 }, Util.indexToByteArray(5, 4));
	}

	@Test
	public void oneDigitNegativeNumber() {
		Assert.assertArrayEquals(new byte[] { 1, 0, 1, 1 }, Util.indexToByteArray(-5, 4));
	}

	@Test
	public void overflow() {
		Assert.assertArrayEquals(new byte[] { 0, 0, 1, 1 }, Util.indexToByteArray(19, 4));
	}

	@Test
	public void negativeNumberOverflow() {
		Assert.assertArrayEquals(new byte[] { 1, 1, 0, 1 }, Util.indexToByteArray(-19, 4));
	}

	@Test
	public void negativeNumber32bits() {
		Assert.assertArrayEquals(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 0 }, Util.indexToByteArray(-2, 32));
	}

	@Test
	public void negativeNumber16bits() {
		Assert.assertArrayEquals(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
				Util.indexToByteArray(-2, 16));
	}

	@Test
	public void bigPositiveNumber() {
		Assert.assertArrayEquals(new byte[] { 0, 1, 0, 0, 0, 0, 1, 0, 0 }, Util.indexToByteArray(132, 9));
	}

	@Test
	public void bigNegativeNumber() {
		Assert.assertArrayEquals(new byte[] { 1, 0, 1, 1, 1, 1, 1, 0, 0 }, Util.indexToByteArray(-132, 9));
	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeNumOfBits() {
		Assert.assertArrayEquals(new byte[] { 1, 0, 1, 1, 1, 1, 1, 0, 0 }, Util.indexToByteArray(-132, -2));
	}

	@Test
	public void zeroBits() {
		Assert.assertArrayEquals(new byte[] {}, Util.indexToByteArray(-132, 0));
	}
}
