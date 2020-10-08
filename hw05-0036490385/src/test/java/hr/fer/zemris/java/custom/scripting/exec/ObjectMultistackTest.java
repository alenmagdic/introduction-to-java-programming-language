package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Assert;
import org.junit.Test;

public class ObjectMultistackTest {

	@Test
	public void pushSingleValue() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("year", new ValueWrapper(Integer.valueOf(2017)));
		Assert.assertEquals(2017, multiStack.peek("year").getValue());
	}

	@Test
	public void popSingleValue() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("year", new ValueWrapper(Integer.valueOf(2017)));
		ValueWrapper value = multiStack.pop("year");
		Assert.assertEquals(2017, value.getValue());
	}

	@Test
	public void pushMultipleValues() {
		ObjectMultistack multiStack = new ObjectMultistack();

		multiStack.push("year", new ValueWrapper(Integer.valueOf(2017)));
		multiStack.push("year", new ValueWrapper(Integer.valueOf(2008)));
		multiStack.push("year", new ValueWrapper(Integer.valueOf(2002)));
		multiStack.push("time length", new ValueWrapper(Double.valueOf(22.32)));
		multiStack.push("time length", new ValueWrapper(Double.valueOf(12.42)));

		Assert.assertEquals(2002, multiStack.pop("year").getValue());
		Assert.assertEquals(12.42, multiStack.pop("time length").getValue());
		Assert.assertEquals(2008, multiStack.pop("year").getValue());
		Assert.assertEquals(2017, multiStack.pop("year").getValue());
		Assert.assertEquals(22.32, multiStack.pop("time length").getValue());

	}

	@Test(expected = IllegalArgumentException.class)
	public void pushNullValue() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("year", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void pushNullKey() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push(null, new ValueWrapper(Integer.valueOf(2017)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void popNullKey() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.pop(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void peekNullKey() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.peek(null);
	}

	@Test(expected = EmptyStackException.class)
	public void popUnexistingElement() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("year", new ValueWrapper(Integer.valueOf(2017)));
		multiStack.pop("month");
	}

	@Test(expected = EmptyStackException.class)
	public void peekUnexistingElement() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("year", new ValueWrapper(Integer.valueOf(2017)));
		multiStack.peek("month");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsEmptyWithNullKey() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.isEmpty(null);
	}

	@Test
	public void testIsEmptyWhenItIsEmpty() {
		ObjectMultistack multiStack = new ObjectMultistack();
		Assert.assertEquals(true, multiStack.isEmpty("month"));
	}

	@Test
	public void testIsEmptyWhenNotEmpty() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("year", new ValueWrapper(Integer.valueOf(2017)));
		Assert.assertEquals(false, multiStack.isEmpty("year"));
	}

}
