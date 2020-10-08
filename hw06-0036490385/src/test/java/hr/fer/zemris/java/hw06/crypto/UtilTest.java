package hr.fer.zemris.java.hw06.crypto;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

	@Test(expected = IllegalArgumentException.class)
	public void hexToByteStringWithIllegalLength() {
		Util.hextobyte("5f6aa78");
	}

	@Test
	public void hexToByte() {
		Assert.assertArrayEquals(Util.hextobyte("5f6aa78d"), new byte[] { 95, 106, (byte) 167, (byte) 141 });
	}

	@Test
	public void hexToByteSinglePair() {
		Assert.assertArrayEquals(Util.hextobyte("5f"), new byte[] { 95 });
	}

	@Test
	public void hexToByteMixedCase() {
		Assert.assertArrayEquals(Util.hextobyte("5F6Aa78d"), new byte[] { 95, 106, (byte) 167, (byte) 141 });
	}

	@Test
	public void byteToHex() {
		Assert.assertEquals(Util.bytetohex(new byte[] { 95, 106, (byte) 167, (byte) 141 }), "5f6aa78d");
	}

	@Test
	public void byteToHexSingleByte() {
		Assert.assertEquals(Util.bytetohex(new byte[] { 95 }), "5f");
	}

	@Test(expected = IllegalArgumentException.class)
	public void byteToHexWithNull() {
		Util.bytetohex(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void hexToByteWithNull() {
		Util.hextobyte(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void hexToByteInvalidString() {
		Util.hextobyte("a4hgf8");
	}

	@Test
	public void hexToByteEmptyString() {
		Assert.assertEquals(0, Util.hextobyte("").length);
	}

}
