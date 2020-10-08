package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Assert;
import org.junit.Test;

public class ValueWrapperTest {

	@Test
	public void createWrapperOfNull() {
		ValueWrapper wrap = new ValueWrapper(null);
		Assert.assertEquals(null, wrap.getValue());
	}

	@Test
	public void createWrapperOfInteger() {
		ValueWrapper wrap = new ValueWrapper(Integer.valueOf(15));
		Assert.assertEquals(15, wrap.getValue());
	}

	@Test
	public void createWrapperOfDouble() {
		ValueWrapper wrap = new ValueWrapper(15.15);
		Assert.assertEquals(15.15, (Double) wrap.getValue(), 1E-6);
	}

	@Test
	public void createWrapperOfDoubleInString() {
		ValueWrapper wrap = new ValueWrapper("52.35");
		Assert.assertEquals("52.35", wrap.getValue());
	}

	@Test
	public void createWrapperOfIntegerInString() {
		ValueWrapper wrap = new ValueWrapper("52");
		Assert.assertEquals("52", wrap.getValue());
	}

	@Test(expected = RuntimeException.class)
	public void doAtithmeticOperationWithWrapperThatWrapsAnInvalidStringValue() {
		new ValueWrapper("5a2").add(50);
	}

	@Test(expected = RuntimeException.class)
	public void doArithmeticOperationWithAnIllegalObject() {
		new ValueWrapper(new Exception()).add(50);
	}

	@Test
	public void additionOfIntegers() {
		ValueWrapper wrap1 = new ValueWrapper(52);
		wrap1.add(8);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(60, wrap1.getValue());
	}

	@Test
	public void additionOfDoubles() {
		ValueWrapper wrap1 = new ValueWrapper(52.32);
		wrap1.add(8.55);
		Assert.assertEquals(60.87, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfIntegerAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper(52);
		wrap1.add(8.55);
		Assert.assertEquals(60.55, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper(52.55);
		wrap1.add(8);
		Assert.assertEquals(60.55, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfStringIntegers() {
		ValueWrapper wrap1 = new ValueWrapper("52");
		wrap1.add("8");
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(60, wrap1.getValue());
	}

	@Test
	public void additionOfStringDoubles() {
		ValueWrapper wrap1 = new ValueWrapper("52.25");
		wrap1.add("8.25");
		Assert.assertEquals(60.5, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfStringDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper("52.25");
		wrap1.add(8);
		Assert.assertEquals(60.25, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfIntegerAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(52);
		wrap1.add(7.5);
		Assert.assertEquals(59.5, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfDoubleAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(52.45);
		wrap1.add("7.5");
		Assert.assertEquals(59.95, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfStringDoubleAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.add(7.5);
		Assert.assertEquals(59.95, (double) wrap1.getValue(), 1E-6);
	}

	@Test(expected = RuntimeException.class)
	public void additionWithInvalidOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.add(new Exception());
	}

	@Test(expected = RuntimeException.class)
	public void additionWithInvalidStringOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.add("123a3");
	}

	@Test
	public void additionWithNull() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.add(null);
		Assert.assertEquals(52.45, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void additionOfIntegerWithNull() {
		ValueWrapper wrap1 = new ValueWrapper("52");
		wrap1.add(null);
		Assert.assertEquals(52, wrap1.getValue());
	}

	@Test
	public void additionOfNulls() {
		ValueWrapper wrap1 = new ValueWrapper(null);
		wrap1.add(null);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(0, wrap1.getValue());
	}

	@Test
	public void subtractionOfIntegers() {
		ValueWrapper wrap1 = new ValueWrapper(52);
		wrap1.subtract(8);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(44, wrap1.getValue());
	}

	@Test
	public void subtractionOfDoubles() {
		ValueWrapper wrap1 = new ValueWrapper(52.55);
		wrap1.subtract(1.5);

		Assert.assertEquals(51.05, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfIntegerAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper(52);
		wrap1.subtract(1.55);
		Assert.assertEquals(50.45, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper(52.55);
		wrap1.subtract(2);
		Assert.assertEquals(50.55, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfStringIntegers() {
		ValueWrapper wrap1 = new ValueWrapper("52");
		wrap1.subtract("2");
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(50, wrap1.getValue());
	}

	@Test
	public void subtractionOfStringDoubles() {
		ValueWrapper wrap1 = new ValueWrapper("52.25");
		wrap1.subtract("1.25");
		Assert.assertEquals(51.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfStringDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper("52.25");
		wrap1.subtract(2);
		Assert.assertEquals(50.25, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfIntegerAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(52);
		wrap1.subtract(1.5);
		Assert.assertEquals(50.5, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfDoubleAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(52.45);
		wrap1.subtract("1.5");
		Assert.assertEquals(50.95, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfStringDoubleAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.subtract(1.5);
		Assert.assertEquals(50.95, (double) wrap1.getValue(), 1E-6);
	}

	@Test(expected = RuntimeException.class)
	public void subtractionWithInvalidOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.subtract(new Exception());
	}

	@Test(expected = RuntimeException.class)
	public void subtractionWithInvalidStringOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.subtract("123a3");
	}

	@Test
	public void subtractionWithNull() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.subtract(null);
		Assert.assertEquals(52.45, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void subtractionOfIntegerWithNull() {
		ValueWrapper wrap1 = new ValueWrapper("52");
		wrap1.subtract(null);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(52, wrap1.getValue());
	}

	@Test
	public void subtractionOfNulls() {
		ValueWrapper wrap1 = new ValueWrapper(null);
		wrap1.subtract(null);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(0, wrap1.getValue());
	}

	@Test
	public void multiplicationOfIntegers() {
		ValueWrapper wrap1 = new ValueWrapper(2);
		wrap1.multiply(2);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(4, wrap1.getValue());
	}

	@Test
	public void multiplicationOfDoubles() {
		ValueWrapper wrap1 = new ValueWrapper(2.55);
		wrap1.multiply(1.5);

		Assert.assertEquals(3.825, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfIntegerAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper(2);
		wrap1.multiply(1.5);
		Assert.assertEquals(3.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper(2.5);
		wrap1.multiply(2);
		Assert.assertEquals(5.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfStringIntegers() {
		ValueWrapper wrap1 = new ValueWrapper("12");
		wrap1.multiply("2");
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(24, wrap1.getValue());
	}

	@Test
	public void multiplicationOfStringDoubles() {
		ValueWrapper wrap1 = new ValueWrapper("2.5");
		wrap1.multiply("1.5");
		Assert.assertEquals(3.75, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfStringDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper("2.5");
		wrap1.multiply(2);
		Assert.assertEquals(5.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfIntegerAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(2);
		wrap1.multiply(1.5);
		Assert.assertEquals(3.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfDoubleAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(2.45);
		wrap1.multiply("1.5");
		Assert.assertEquals(3.675, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfStringDoubleAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper("2.45");
		wrap1.multiply(1.5);
		Assert.assertEquals(3.675, (double) wrap1.getValue(), 1E-6);
	}

	@Test(expected = RuntimeException.class)
	public void multiplicationWithInvalidOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.multiply(new Exception());
	}

	@Test(expected = RuntimeException.class)
	public void multiplicationWithInvalidStringOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.multiply("123a3");
	}

	@Test
	public void multiplicationByNull() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.multiply(null);
		Assert.assertEquals(0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void multiplicationOfIntegerByNull() {
		ValueWrapper wrap1 = new ValueWrapper("52");
		wrap1.multiply(null);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(0, wrap1.getValue());
	}

	@Test
	public void multiplicationOfNulls() {
		ValueWrapper wrap1 = new ValueWrapper(null);
		wrap1.multiply(null);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(0, wrap1.getValue());
	}

	@Test
	public void divisionOfIntegers() {
		ValueWrapper wrap1 = new ValueWrapper(4);
		wrap1.divide(3);
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(1, wrap1.getValue());
	}

	@Test
	public void divisionOfDoubles() {
		ValueWrapper wrap1 = new ValueWrapper(5.5);
		wrap1.divide(2.0);

		Assert.assertEquals(2.75, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void divisionOfIntegerAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper(2.5);
		wrap1.divide(2.0);
		Assert.assertEquals(1.25, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void divisionOfDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper(2.5);
		wrap1.divide(2);
		Assert.assertEquals(1.25, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void divisionOfStringIntegers() {
		ValueWrapper wrap1 = new ValueWrapper("12");
		wrap1.divide("5");
		Assert.assertEquals(true, wrap1.getValue() instanceof Integer);
		Assert.assertEquals(2, wrap1.getValue());
	}

	@Test
	public void divisionOfStringDoubles() {
		ValueWrapper wrap1 = new ValueWrapper("2.5");
		wrap1.divide("2.0");
		Assert.assertEquals(1.25, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void divisionOfStringDoubleAndInteger() {
		ValueWrapper wrap1 = new ValueWrapper("2.5");
		wrap1.divide(2);
		Assert.assertEquals(1.25, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void divisionOfIntegerAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(3);
		wrap1.divide(1.5);
		Assert.assertEquals(2.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void divisionOfDoubleAndStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper(2.5);
		wrap1.divide("1.25");
		Assert.assertEquals(2.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test
	public void divisionOfStringDoubleAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper("2.5");
		wrap1.divide(1.25);
		Assert.assertEquals(2.0, (double) wrap1.getValue(), 1E-6);
	}

	@Test(expected = RuntimeException.class)
	public void divisionWithInvalidOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.divide(new Exception());
	}

	@Test(expected = RuntimeException.class)
	public void divisionWithInvalidStringOperand() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.divide("123a3");
	}

	@Test(expected = ArithmeticException.class)
	public void divisionByNull() {
		ValueWrapper wrap1 = new ValueWrapper("52.45");
		wrap1.divide(null);
	}

	@Test(expected = ArithmeticException.class)
	public void divisionOfIntegerByNull() {
		ValueWrapper wrap1 = new ValueWrapper("52");
		wrap1.divide(null);
	}

	@Test(expected = ArithmeticException.class)
	public void divisionOfNulls() {
		ValueWrapper wrap1 = new ValueWrapper(null);
		wrap1.divide(null);
	}

	@Test
	public void compareIntegerWithGreaterValue() {
		ValueWrapper wrap1 = new ValueWrapper(2);

		Assert.assertEquals(true, wrap1.numCompare(5) < 0);
	}

	@Test
	public void compareIntegerWithLowerValue() {
		ValueWrapper wrap1 = new ValueWrapper(5);

		Assert.assertEquals(true, wrap1.numCompare(2) > 0);
	}

	@Test
	public void compareIntegerWithEqualValue() {
		ValueWrapper wrap1 = new ValueWrapper(5);

		Assert.assertEquals(true, wrap1.numCompare(5) == 0);
	}

	@Test
	public void compareDoubleWithLowerValue() {
		ValueWrapper wrap1 = new ValueWrapper(5.25);

		Assert.assertEquals(true, wrap1.numCompare(5.2) > 0);
	}

	@Test
	public void compareDoubleWithGreaterValue() {
		ValueWrapper wrap1 = new ValueWrapper(5.25);

		Assert.assertEquals(true, wrap1.numCompare(5.3) < 0);
	}

	@Test
	public void compareNullValues() {
		ValueWrapper wrap1 = new ValueWrapper(null);

		Assert.assertEquals(true, wrap1.numCompare(null) == 0);
	}

	@Test
	public void compareNullAndPositiveNumber() {
		ValueWrapper wrap1 = new ValueWrapper(null);

		Assert.assertEquals(true, wrap1.numCompare(5) < 0);
	}

	@Test
	public void compareNullAndNegativeNumber() {
		ValueWrapper wrap1 = new ValueWrapper(null);

		Assert.assertEquals(true, wrap1.numCompare(-5) > 0);
	}

	@Test
	public void compareIntegerWithGreaterDoubleValue() {
		ValueWrapper wrap1 = new ValueWrapper(2);

		Assert.assertEquals(true, wrap1.numCompare(5.33) < 0);
	}

	@Test
	public void compareIntegerWithLowerDoubleValue() {
		ValueWrapper wrap1 = new ValueWrapper(12);

		Assert.assertEquals(true, wrap1.numCompare(5.33) > 0);
	}

	@Test
	public void compareDoubleWithLowerIntegerValue() {
		ValueWrapper wrap1 = new ValueWrapper(12.33);

		Assert.assertEquals(true, wrap1.numCompare(5) > 0);
	}

	@Test
	public void compareDoubleWithGreaterIntegerValue() {
		ValueWrapper wrap1 = new ValueWrapper(12.33);

		Assert.assertEquals(true, wrap1.numCompare(15) < 0);
	}

	@Test
	public void compareStringDoubleWithGreaterIntegerValue() {
		ValueWrapper wrap1 = new ValueWrapper("12.33");

		Assert.assertEquals(true, wrap1.numCompare(15) < 0);
	}

	@Test
	public void compareStringDoubleWithLowerIntegerValue() {
		ValueWrapper wrap1 = new ValueWrapper("12.33");

		Assert.assertEquals(true, wrap1.numCompare(5) > 0);
	}

	@Test
	public void compareStringDoubleWithLowerStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper("12.33");

		Assert.assertEquals(true, wrap1.numCompare("5.33") > 0);
	}

	@Test
	public void compareStringDoubleWithGreaterStringDouble() {
		ValueWrapper wrap1 = new ValueWrapper("12.33");

		Assert.assertEquals(true, wrap1.numCompare("15.33") < 0);
	}

	@Test
	public void compareStringIntegerWithGreaterStringInteger() {
		ValueWrapper wrap1 = new ValueWrapper("12");

		Assert.assertEquals(true, wrap1.numCompare("15") < 0);
	}

	@Test
	public void compareStringIntegerWithLowerStringInteger() {
		ValueWrapper wrap1 = new ValueWrapper("12");

		Assert.assertEquals(true, wrap1.numCompare("5") > 0);
	}

}
