package hr.fer.zemris.java.hw04.collections;

import org.junit.Assert;
import org.junit.Test;

public class SimpleHashtableTest {

	@Test
	public void test() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();
		map.put("kurac", 33);
		map.remove("kurac");
		Assert.assertEquals(false, map.containsKey("kurac"));
	}
}
