package hr.fer.zemris.java.hw01;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FactorialTest {

    @Test
    public void factorialOfOne() {
	assertEquals(1L, Factorial.factorial(1));
    }

    @Test
    public void factorialOfFive() {
	assertEquals(120L, Factorial.factorial(5));
    }

    @Test
    public void factorialOfTwenty() {
	assertEquals(2432902008176640000L, Factorial.factorial(20));
    }

    @Test
    public void factorialOfNegativeNumber() {
	assertEquals(-1L, Factorial.factorial(-10));
    }

}
