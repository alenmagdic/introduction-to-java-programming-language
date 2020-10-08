package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import hr.fer.zemris.java.hw03.prob1.LexerException;

public class LexerTest {

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
		String docBody = loader("src\\test\\resources\\testExample1.txt");

		Lexer lex = new Lexer(docBody);

		assertEquals(TokenType.TEXT, lex.nextToken().getType());
		assertEquals("some text ", lex.getToken().getValue());
		assertEquals(TokenType.OPEN_TAG, lex.nextToken().getType());
		assertEquals("{$", lex.getToken().getValue());
		assertEquals(TokenType.NAME, lex.nextToken().getType());
		assertEquals("tagName", lex.getToken().getValue());
		assertEquals(TokenType.NAME, lex.nextToken().getType());
		assertEquals("var123", lex.getToken().getValue());
		assertEquals(TokenType.DOUBLE_CONST, lex.nextToken().getType());
		assertEquals(12.34, (Double) lex.getToken().getValue(), 1E-6);
		assertEquals(TokenType.STRING, lex.nextToken().getType());
		assertEquals("\"a string\"", lex.getToken().getValue());
		assertEquals(TokenType.CLOSE_TAG, lex.nextToken().getType());
		assertEquals("$}", lex.getToken().getValue());
		assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void testExample2() {
		String docBody = loader("src\\test\\resources\\testExample2.txt");

		Lexer lex = new Lexer(docBody);

		assertEquals(TokenType.TEXT, lex.nextToken().getType());
		assertEquals("", lex.getToken().getValue());
		assertEquals(TokenType.OPEN_TAG, lex.nextToken().getType());
		assertEquals(TokenType.SYMBOL, lex.nextToken().getType());
		assertEquals(TokenType.SYMBOL, lex.nextToken().getType());
		assertEquals(TokenType.INT_CONST, lex.nextToken().getType());
		assertEquals(35, lex.getToken().getValue());
		assertEquals(TokenType.CLOSE_TAG, lex.nextToken().getType());
		assertEquals(TokenType.TEXT, lex.nextToken().getType());
		assertEquals(" {te\\xt", lex.getToken().getValue());
		assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void testExample3() {
		String docBody = loader("src\\test\\resources\\testExample3.txt");

		Lexer lex = new Lexer(docBody);

		lex.nextToken();
		lex.nextToken();
		assertEquals(TokenType.STRING, lex.nextToken().getType());
		assertEquals("\"this is    \n   \t    \" a str\\ing\"", lex.getToken().getValue());
	}

	@Test
	public void testExample4() {
		String docBody = loader("src\\test\\resources\\testExample4.txt");

		Lexer lex = new Lexer(docBody);

		lex.nextToken();
		lex.nextToken();
		assertEquals(TokenType.STRING, lex.nextToken().getType());
		assertEquals("\"@sin\"", lex.getToken().getValue());
		assertEquals(TokenType.FUNCTION, lex.nextToken().getType());
		assertEquals("@sin", lex.getToken().getValue());
	}

	@Test
	public void testExample5() {
		String docBody = loader("src\\test\\resources\\testExample5.txt");

		Lexer lex = new Lexer(docBody);

		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
		assertEquals(TokenType.SYMBOL, lex.nextToken().getType());
		assertEquals('?', lex.getToken().getValue());
	}

	@Test(expected = LexerException.class)
	public void testExample6() {
		String docBody = loader("src\\test\\resources\\testExample6.txt");

		Lexer lex = new Lexer(docBody);

		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
		System.out.println(((String) lex.nextToken().getValue()).length()); // ovdje
																			// treba
																			// puknuti
		// zbog nenavedenog
		// naziva funkcije
		System.out.println(lex.getToken().getType());
	}

	@Test(expected = LexerException.class)
	public void testExample7() {
		String docBody = loader("src\\test\\resources\\testExample7.txt");

		Lexer lex = new Lexer(docBody);

		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
	}

	@Test(expected = LexerException.class)
	public void testExample8() {
		String docBody = loader("src\\test\\resources\\testExample8.txt");

		Lexer lex = new Lexer(docBody);

		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
	}

	@Test
	public void testNotNull() {
		Lexer lexer = new Lexer("");

		assertNotNull(lexer.nextToken());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullInput() {
		// must throw!
		new Lexer(null);
	}

	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");

		assertEquals(TokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	public void testGetReturnsLastNext() {

		Lexer lexer = new Lexer("");

		Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken());
		assertEquals(token, lexer.getToken());
	}
}
