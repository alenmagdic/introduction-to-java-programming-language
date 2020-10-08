package hr.fer.zemris.java.hw04.db;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.hw04.db.StudentDatabase;

public class StudentDatabaseTest {
	private StudentDatabase database;

	@Before
	public void setUp() {
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get("./src/test/resources/database.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			Assert.fail("Nije moguće učitati bazu podataka. Testovi ne mogu biti izvršeni.");
			return;
		}
		database = new StudentDatabase(lines);
	}

	@Test
	public void filterAcceptingAll() {
		assertEquals(0, database.filter(record -> false).size());
	}

	@Test
	public void filterNotAcceptingAnything() {
		assertEquals(63, database.filter(record -> true).size());
	}
}
