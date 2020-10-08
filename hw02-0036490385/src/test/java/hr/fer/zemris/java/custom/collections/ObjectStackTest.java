package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ObjectStackTest {

	@Test
	public void isEmptyTrue() {
		ObjectStack stack = new ObjectStack();
		assertEquals(true, stack.isEmpty());
	}

	@Test
	public void isEmptyFalse() {
		ObjectStack stack = new ObjectStack();
		stack.push("Los Angeles");
		assertEquals(false, stack.isEmpty());
	}

	@Test
	public void pushAndPop() {
		ObjectStack stack = new ObjectStack();
		stack.push("Los Angeles");
		assertEquals(1, stack.size());
		assertEquals("Los Angeles", stack.pop());
	}

	@Test
	public void pushNull() {
		ObjectStack stack = new ObjectStack();
		try {
			stack.push(null);
			fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void popFromEmptyStack() {
		ObjectStack stack = new ObjectStack();
		try {
			stack.pop();
			fail();
		} catch (EmptyStackException ex) {
		}
	}

	@Test
	public void peekAndPop() {
		ObjectStack stack = new ObjectStack();
		stack.push("Los Angeles");
		stack.push(25);
		assertEquals(2, stack.size());
		assertEquals(25, stack.peek());
		assertEquals(2, stack.size());
		assertEquals(25, stack.pop());
		assertEquals(1, stack.size());
	}

	@Test
	public void clear() {
		ObjectStack stack = new ObjectStack();
		stack.push("Los Angeles");
		stack.push(25);
		stack.clear();
		assertEquals(0, stack.size());
	}

}
