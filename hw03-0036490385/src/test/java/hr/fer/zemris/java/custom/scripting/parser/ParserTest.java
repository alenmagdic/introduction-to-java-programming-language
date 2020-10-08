package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.hw03.SmartScriptTester;

public class ParserTest {

	private String loader(String filename) {
		try {
			return new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			Assert.fail("Nije moguće pročitati testnu datoteku '" + filename + "'.");
			return null;
		}
	}

	@Test
	public void testExample1() {
		String docBody = loader("src\\test\\resources\\parseTest1.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = SmartScriptTester.createOriginalDocumentBody(document);
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();

		assertEquals(document.numberOfChildren(), document2.numberOfChildren());
		for (int i = 0, n = document.numberOfChildren(); i < n; i++) {
			assertEquals(document.getChild(i).getClass(), document2.getChild(i).getClass());

			if (document.getChild(i).numberOfChildren() > 0) {
				for (int j = 0, n2 = document.getChild(i).numberOfChildren(); j < n2; j++) {
					assertEquals(document.getChild(i).getChild(j).getClass(),
							document2.getChild(i).getChild(j).getClass());
				}
			}
		}
	}

	@Test
	public void testExample2() {
		String docBody = loader("src\\test\\resources\\parseTest2.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = SmartScriptTester.createOriginalDocumentBody(document);
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();

		assertEquals(document.numberOfChildren(), document2.numberOfChildren());
		for (int i = 0, n = document.numberOfChildren(); i < n; i++) {
			assertEquals(document.getChild(i).getClass(), document2.getChild(i).getClass());

			if (document.getChild(i).numberOfChildren() > 0) {
				for (int j = 0, n2 = document.getChild(i).numberOfChildren(); j < n2; j++) {
					assertEquals(document.getChild(i).getChild(j).getClass(),
							document2.getChild(i).getChild(j).getClass());
				}
			}
		}
	}

	@Test(expected = SmartScriptParserException.class)
	public void testExample3() {
		String docBody = loader("src\\test\\resources\\parseTest3.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testExample4() {
		String docBody = loader("src\\test\\resources\\parseTest4.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testExample5() {
		String docBody = loader("src\\test\\resources\\parseTest5.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testExample6() {
		String docBody = loader("src\\test\\resources\\parseTest6.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testExample7() {
		String docBody = loader("src\\test\\resources\\parseTest7.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testExample8() {
		String docBody = loader("src\\test\\resources\\parseTest8.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testExample9() {
		String docBody = loader("src\\test\\resources\\parseTest9.txt");

		SmartScriptParser parser = new SmartScriptParser(docBody);
	}
}
