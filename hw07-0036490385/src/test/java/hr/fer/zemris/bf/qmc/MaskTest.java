package hr.fer.zemris.bf.qmc;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

public class MaskTest {

	@Test
	public void createSimpleMask() {
		Mask mask = new Mask(12, 4, false);
		byte[] bytes = new byte[] { 1, 1, 0, 0 };

		Assert.assertEquals(bytes.length, mask.size());
		for (int i = 0; i < mask.size(); i++) {
			Assert.assertEquals(bytes[i], mask.getValueAt(i));
		}
	}

	@Test
	public void createDontCareMask() {
		Mask mask = new Mask(12, 4, true);
		Assert.assertEquals(true, mask.isDontCare());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createMaskWithNegativeIndex() {
		new Mask(-12, 4, true);

	}

	@Test(expected = IllegalArgumentException.class)
	public void createMaskWithNegativeNumOfVars() {
		new Mask(12, -4, true);

	}

	@Test
	public void createMask() {
		Set<Integer> indexes = new TreeSet<Integer>();
		indexes.add(9);
		indexes.add(13);
		Mask mask = new Mask(new byte[] { 1, 2, 0, 1 }, indexes, false);

		byte[] bytes = new byte[] { 1, 2, 0, 1 };
		Assert.assertEquals(bytes.length, mask.size());
		for (int i = 0, n = mask.size(); i < n; i++) {
			Assert.assertEquals(bytes[i], mask.getValueAt(i));
		}

		Iterator<Integer> indexesIt = mask.getIndexes().iterator();
		Assert.assertEquals((Integer) 9, (Integer) indexesIt.next());
		Assert.assertEquals((Integer) 13, (Integer) indexesIt.next());
		Assert.assertEquals(false, indexesIt.hasNext());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createMaskWithNullArray() {
		Set<Integer> indexes = new TreeSet<Integer>();
		indexes.add(9);
		new Mask(null, indexes, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createMaskWithNullIndexes() {
		new Mask(new byte[] { 1, 1 }, null, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createMaskWithEmptyArray() {
		new Mask(new byte[] {}, new TreeSet<Integer>(), false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createMaskWithEmptySetOfIndexes() {
		new Mask(new byte[] { 1, 2 }, new TreeSet<Integer>(), false);
	}

	@Test
	public void countOfOnes() {
		Mask mask = new Mask(12, 4, false);
		Assert.assertEquals(2, mask.countOfOnes());
	}

	@Test
	public void countOfOnes2() {
		Mask mask = new Mask(77, 10, false);
		Assert.assertEquals(4, mask.countOfOnes());
	}

	@Test
	public void countOfOnesWhenThereAreNoOnes() {
		Set<Integer> indexes = new TreeSet<Integer>();
		indexes.add(9); // this is not important for this test, so it is added
						// just to be able to create mask
		Mask mask = new Mask(new byte[] { 2, 2, 2, 2 }, indexes, false);
		Assert.assertEquals(0, mask.countOfOnes());
	}

	@Test
	public void toStringForMaskThatIsNotDontCareAndNotCombined() {
		Set<Integer> indexes = new TreeSet<Integer>();
		indexes.add(9);
		indexes.add(13);
		Mask mask = new Mask(new byte[] { 1, 2, 0, 1 }, indexes, false);
		Assert.assertEquals("1-01 .   [9, 13]", mask.toString());
	}

	@Test
	public void toStringForMaskThatIsDontCareAndCombined() {
		Set<Integer> indexes = new TreeSet<Integer>();
		indexes.add(9);
		indexes.add(13);
		Mask mask = new Mask(new byte[] { 1, 2, 0, 1 }, indexes, true);
		mask.setCombined(true);
		Assert.assertEquals("1-01 D * [9, 13]", mask.toString());
	}

	@Test
	public void maskToString() {
		Mask mask = new Mask(77, 10, false);
		Assert.assertEquals("0001001101 .   [77]", mask.toString());
	}

	@Test
	public void combineEqualMasks() {
		Mask mask1 = new Mask(5, 3, false);
		Mask mask2 = new Mask(5, 3, false);
		Assert.assertEquals(mask1, mask1.combineWith(mask2).get());
	}

	@Test
	public void combineIncompatibleMasks() {
		Mask mask1 = new Mask(5, 3, false); // 101
		Mask mask2 = new Mask(3, 3, false);// 011
		Optional<Mask> combinedMask = mask1.combineWith(mask2);
		Assert.assertEquals(false, combinedMask.isPresent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void combineMasksWithDiffSize() {
		Mask mask1 = new Mask(5, 3, false); // 101
		Mask mask2 = new Mask(3, 4, false);// 0011
		Optional<Mask> combinedMask = mask1.combineWith(mask2);
		Assert.assertEquals(false, combinedMask.isPresent());
	}

	@Test
	public void combineMasks() {
		Mask mask1 = new Mask(5, 3, false); // 101
		Mask mask2 = new Mask(4, 3, false);// 100
		Mask combinedMask = mask1.combineWith(mask2).get();

		Assert.assertEquals(mask1.size(), combinedMask.size());
		Assert.assertEquals(1, combinedMask.getValueAt(0));
		Assert.assertEquals(0, combinedMask.getValueAt(1));
		Assert.assertEquals(2, combinedMask.getValueAt(2));
	}

	@Test
	public void combineMasks2() {
		Mask mask1 = new Mask(13, 4, false); // 1101
		Mask mask2 = new Mask(9, 4, true);// 1001
		Mask combinedMask = mask1.combineWith(mask2).get();

		Assert.assertEquals(mask1.size(), combinedMask.size());
		Assert.assertEquals(1, combinedMask.getValueAt(0));
		Assert.assertEquals(2, combinedMask.getValueAt(1));
		Assert.assertEquals(0, combinedMask.getValueAt(2));
		Assert.assertEquals(1, combinedMask.getValueAt(3));
	}

	@Test
	public void testIndexesForSimpleComb() {
		Mask mask1 = new Mask(5, 3, false); // 101
		Mask mask2 = new Mask(4, 3, false);// 100
		Mask combinedMask = mask1.combineWith(mask2).get();

		Iterator<Integer> indexesIt = combinedMask.getIndexes().iterator();
		Assert.assertEquals((Integer) 4, (Integer) indexesIt.next());
		Assert.assertEquals((Integer) 5, (Integer) indexesIt.next());
		Assert.assertEquals(false, indexesIt.hasNext());
	}

	@Test
	public void testIndexesForComplexComb() {
		Set<Integer> indexes = new TreeSet<>();
		indexes.add(9);
		indexes.add(13);
		Mask mask1 = new Mask(new byte[] { 1, 2, 0, 1 }, indexes, false);

		indexes.clear();
		indexes.add(8);
		indexes.add(9);
		indexes.add(12);
		indexes.add(13);
		Mask mask2 = new Mask(new byte[] { 1, 2, 0, 2 }, indexes, false);

		Mask combinedMask = mask1.combineWith(mask2).get();

		Iterator<Integer> indexesIt = combinedMask.getIndexes().iterator();
		Assert.assertEquals((Integer) 8, (Integer) indexesIt.next());
		Assert.assertEquals((Integer) 9, (Integer) indexesIt.next());
		Assert.assertEquals((Integer) 12, (Integer) indexesIt.next());
		Assert.assertEquals((Integer) 13, (Integer) indexesIt.next());
		Assert.assertEquals(false, indexesIt.hasNext());
	}

	@Test
	public void testDontCaresForCombOfNotDontCareMasks() {
		Mask mask1 = new Mask(5, 3, false);
		Mask mask2 = new Mask(4, 3, false);
		Mask combinedMask = mask1.combineWith(mask2).get();
		Assert.assertEquals(false, combinedMask.isDontCare());
	}

	@Test
	public void testDontCaresForCombOfDontCareAndNotDontCareMask() {
		Mask mask1 = new Mask(5, 3, true);
		Mask mask2 = new Mask(4, 3, false);
		Mask combinedMask = mask1.combineWith(mask2).get();
		Assert.assertEquals(false, combinedMask.isDontCare());
	}

	@Test
	public void testDontCaresForCombOfNotDontCareAndDontCareMask() {
		Mask mask1 = new Mask(5, 3, false);
		Mask mask2 = new Mask(4, 3, true);
		Mask combinedMask = mask1.combineWith(mask2).get();
		Assert.assertEquals(false, combinedMask.isDontCare());
	}

	@Test
	public void testDontCaresForCombOfDontCareMasks() {
		Mask mask1 = new Mask(5, 3, true);
		Mask mask2 = new Mask(4, 3, true);
		Mask combinedMask = mask1.combineWith(mask2).get();
		Assert.assertEquals(true, combinedMask.isDontCare());
	}
}
