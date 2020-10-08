package hr.fer.zemris.java.hw04.db;

import org.junit.Assert;
import org.junit.Test;

public class FieldValueGettersTest {

	@Test
	public void getFirstName() {
		StudentRecord record = new StudentRecord("0036490385", "Magdić", "Alen", 5);
		Assert.assertEquals("Alen", FieldValueGetters.FIRST_NAME.get(record));
	}

	@Test
	public void getLastName() {
		StudentRecord record = new StudentRecord("0036490385", "Magdić", "Alen", 5);
		Assert.assertEquals("Magdić", FieldValueGetters.LAST_NAME.get(record));
	}

	@Test
	public void getJmbag() {
		StudentRecord record = new StudentRecord("0036490385", "Magdić", "Alen", 5);
		Assert.assertEquals("0036490385", FieldValueGetters.JMBAG.get(record));
	}
}
