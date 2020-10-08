package hr.fer.zemris.math;

import org.junit.Assert;
import org.junit.Test;

public class Vector3Test {

	@Test
	public void vectorNorm() {
		Vector3 v = new Vector3(3, 4, 5);
		Assert.assertEquals(7.071067, v.norm(), 1E-2);
	}

	@Test
	public void vectorNormalized() {
		Vector3 v = new Vector3(3, 4, 5).normalized();
		Assert.assertEquals(0.4242, v.getX(), 1E-2);
		Assert.assertEquals(0.5656, v.getY(), 1E-2);
		Assert.assertEquals(0.7071, v.getZ(), 1E-2);
	}

	@Test
	public void addingVectors() {
		Vector3 v1 = new Vector3(3, 4, 5);
		Vector3 v2 = new Vector3(1, 2, 7);
		Vector3 v = v1.add(v2);
		Assert.assertEquals(4, v.getX(), 1E-2);
		Assert.assertEquals(6, v.getY(), 1E-2);
		Assert.assertEquals(12, v.getZ(), 1E-2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addingNull() {
		new Vector3(3, 4, 5).add(null);
	}

	@Test
	public void subtractionOfVectors() {
		Vector3 v1 = new Vector3(3, 4, 5);
		Vector3 v2 = new Vector3(1, -2, 7);
		Vector3 v = v1.sub(v2);
		Assert.assertEquals(2, v.getX(), 1E-2);
		Assert.assertEquals(6, v.getY(), 1E-2);
		Assert.assertEquals(-2, v.getZ(), 1E-2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void subtractionWithNull() {
		new Vector3(3, 4, 5).sub(null);
	}

	@Test
	public void scalarProduct() {
		Vector3 v1 = new Vector3(0, 5, 5);
		Vector3 v2 = new Vector3(0, 1, 0);
		Assert.assertEquals(5, v1.dot(v2), 1E-2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void scalarProductWithNull() {
		new Vector3(3, 4, 5).dot(null);
	}

	@Test
	public void vectorProduct() {
		Vector3 v1 = new Vector3(1, 0, 0);
		Vector3 v2 = new Vector3(0, 1, 0);
		Vector3 v = v1.cross(v2);
		Assert.assertEquals(0, v.getX(), 1E-2);
		Assert.assertEquals(0, v.getY(), 1E-2);
		Assert.assertEquals(1, v.getZ(), 1E-2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void vectorProductWithNull() {
		new Vector3(3, 4, 5).cross(null);
	}

	@Test
	public void scaleVector() {
		Vector3 v1 = new Vector3(3, 2, 5);
		Vector3 v = v1.scale(10);
		Assert.assertEquals(30, v.getX(), 1E-2);
		Assert.assertEquals(20, v.getY(), 1E-2);
		Assert.assertEquals(50, v.getZ(), 1E-2);
	}

	@Test
	public void cosAngleTest() {
		Vector3 v1 = new Vector3(3, 2, 5);
		Vector3 v2 = new Vector3(4, 1, 8);
		Assert.assertEquals(0.9733, v1.cosAngle(v2), 1E-2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void cosAngleWithNull() {
		new Vector3(3, 4, 5).cosAngle(null);
	}

	@Test
	public void vectorToArray() {
		Vector3 v = new Vector3(3, 2, 5);
		double[] array = v.toArray();
		Assert.assertArrayEquals(new double[] { 3, 2, 5 }, array, 1E-2);
	}
}
