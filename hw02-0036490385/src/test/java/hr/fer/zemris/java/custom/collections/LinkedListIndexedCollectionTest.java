package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class LinkedListIndexedCollectionTest {
	@Test
	public void addingOneValue() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(55);
		assertEquals(55, col.get(0));
		assertEquals(1, col.size());
	}

	@Test
	public void addingManyValues() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		int i;
		for (i = 0; i < 1000; i += 2) {
			col.add(i);
			col.add("New York");
		}
		assertEquals(0, col.get(0));
		assertEquals("New York", col.get(1));
		assertEquals(i / 2, col.get(i / 2));
		assertEquals("New York", col.get(i - 1));
		assertEquals(1000, col.size());
	}

	@Test
	public void addingNull() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		try {
			col.add(null);
			fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void gettingFirstElement() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add("Paris");
		assertEquals("Paris", col.get(0));
	}

	@Test
	public void gettingSomeElement() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		int i;
		for (i = 0; i < 1000; i++) {
			col.add(i);
		}
		assertEquals(25, col.get(25));
	}

	@Test
	public void gettingElementOutOfBounds() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(55);
		try {
			col.get(1);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	@Test
	public void gettingNegativeElement() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(34.56);
		try {
			col.get(-1);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	@Test
	public void clearing() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 1000; i++) {
			col.add(i);
		}
		col.clear();
		assertEquals(0, col.size());
		try {
			col.get(0);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	@Test
	public void insertToPosition0() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 10; i++) {
			col.add(i);
		}
		col.insert("San Francisco", 0);
		assertEquals("San Francisco", col.get(0));
		for (int i = 0; i < 10; i++) {
			assertEquals(i, col.get(i + 1));
		}
	}

	@Test
	public void insertToMiddle() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		int i;
		for (i = 0; i < 10; i++) {
			col.add(i);
		}
		col.insert("London", 2);
		assertEquals(0, col.get(0));
		assertEquals(1, col.get(1));
		assertEquals("London", col.get(2));
		assertEquals(2, col.get(3));
	}

	@Test
	public void insertManyElements() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 10; i++) {
			col.add(i);
		}
		for (int i = 20; i < 1000; i++) {
			col.insert(i, 5);
		}
		assertEquals(990, col.size());
		assertEquals(9, col.get(989));
	}

	@Test
	public void insertingNull() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		try {
			col.insert(null, 6);
			fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void insertingToPositionOutOfBounds() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		try {
			col.insert(10, 1);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	@Test
	public void insertingToNegativePostion() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		try {
			col.insert(10, -1);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	@Test
	public void gettingIndex() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 10; i++) {
			col.add(i);
		}
		col.add("Spain");
		assertEquals(0, col.indexOf(0));
		assertEquals(5, col.indexOf(5));
		assertEquals(10, col.indexOf("Spain"));
	}

	@Test
	public void removingFirstElement() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 10; i++) {
			col.add(i);
		}
		col.remove(0);
		assertEquals(1, col.get(0));
		assertEquals(2, col.get(1));
		assertEquals(9, col.size());
	}

	@Test
	public void removingMiddleElement() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 10; i++) {
			col.add(i);
		}
		col.remove(5);
		assertEquals(4, col.get(4));
		assertEquals(6, col.get(5));
		assertEquals(7, col.get(6));
		assertEquals(9, col.size());
	}

	@Test
	public void removingMoreElements() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 10; i++) {
			col.add(i);
		}
		for (int i = 0; i < 5; i++) {
			col.remove(3);
		}
		assertEquals(5, col.size());
	}

	@Test
	public void removingElementOutOfBound() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		for (int i = 0; i < 10; i++) {
			col.add(i);
		}
		try {
			col.remove(15);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	@Test
	public void removingNegativeElement() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(5);
		try {
			col.remove(-10);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	@Test
	public void isEmpty() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		assertEquals(true, col.isEmpty());
	}

	@Test
	public void isEmptyFalse() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(11);
		assertEquals(false, col.isEmpty());
	}

	@Test
	public void clear() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(1);
		col.add(2);
		col.clear();
		assertEquals(true, col.isEmpty());
	}

	@Test
	public void contains() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(1);
		col.add(2);
		col.add("San Francisco");
		assertEquals(true, col.contains("San Francisco"));
	}

	@Test
	public void removingObject() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add("London");
		col.add(255);
		col.add("San Francisco");
		col.remove(new Integer(255));
		assertEquals(2, col.size());
		assertEquals("San Francisco", col.get(1));
	}

	@Test
	public void toArray() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add("London");
		col.add(255);
		col.add("San Francisco");
		Object[] array = col.toArray();
		assertEquals(3, array.length);
		assertEquals("London", array[0]);
		assertEquals(255, array[1]);
		assertEquals("San Francisco", array[2]);
	}

	@Test
	public void addAll() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add("London");
		col.add(255);
		col.add("San Francisco");
		LinkedListIndexedCollection col2 = new LinkedListIndexedCollection();
		col2.addAll(col);
		assertEquals(3, col2.size());
		assertEquals("London", col2.get(0));
		assertEquals(255, col2.get(1));
		assertEquals("San Francisco", col2.get(2));
	}
}
