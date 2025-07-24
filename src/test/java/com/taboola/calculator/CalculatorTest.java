package com.taboola.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testSimpleAssignment() {
        calculator.evaluate("i = 5");
        assertEquals(Map.of("i", 5), calculator.getVariables());
    }

    @Test
    public void testDivisionInExpression() {
        Calculator calc = new Calculator();
        calc.evaluate("x = 20 / 4");
        assertEquals(5, calc.getVariables().get("x"));
    }

    @Test
    public void testDivisionByZeroThrows() {
        Calculator calc = new Calculator();
        Exception exception = assertThrows(ArithmeticException.class, () -> {
            calc.evaluate("x = 5 / 0");
        });
        assertEquals("Division by zero", exception.getMessage());
    }

    @Test
    void testAdditionAndMultiplication() {
        calculator.evaluate("a = 2 + 3 * 4");  // 2 + (3*4) = 14
        assertEquals(14, calculator.getVariables().get("a"));
    }

    @Test
    void testPreIncrement() {
        calculator.evaluate("i = 0");
        calculator.evaluate("j = ++i");  // pre-increment i (i=1), assign j=1
        assertEquals(1, calculator.getVariables().get("i"));
        assertEquals(1, calculator.getVariables().get("j"));
    }

    @Test
    void testPostIncrement() {
        calculator.evaluate("i = 0");
        calculator.evaluate("x = i++ + 5");  // x = 0+5=5, then i=1
        assertEquals(1, calculator.getVariables().get("i"));
        assertEquals(5, calculator.getVariables().get("x"));
    }

    @Test
    void testCompoundAssignment() {
        calculator.evaluate("i = 1");
        calculator.evaluate("i += 4");  // i = 1+4 = 5
        assertEquals(5, calculator.getVariables().get("i"));
    }

    @Test
    void testCompoundAssignment2() {
        calculator.evaluate("i = x + 2");
        calculator.evaluate("j = 10 - 2 * 5 + 2");
        assertEquals(2, calculator.getVariables().get("i"));
        assertEquals(2, calculator.getVariables().get("j"));
    }

    @Test
    void testRightOperationsOrder() {
        calculator.evaluate("i = 1 + 2 * 5 + 3");
        assertEquals(14, calculator.getVariables().get("i"));
    }

    @Test
    public void testSubtractionInExpression() {
        Calculator calc = new Calculator();
        calc.evaluate("x = 10 - 3");
        assertEquals(7, calc.getVariables().get("x"));
    }

    @Test
    public void testMinusAssignOperator() {
        Calculator calc = new Calculator();
        calc.evaluate("a = 5");
        calc.evaluate("a -= 20");
        calc.evaluate("b = -20");
        calc.evaluate("c = -a");
        assertEquals(-15, calc.getVariables().get("a"));
        assertEquals(-20, calc.getVariables().get("b"));
        assertEquals(15, calc.getVariables().get("c"));
    }

    @Test
    void testUnaryMinusIntegration() {
        calculator.evaluate("i = 5");
        calculator.evaluate("j = -i");
        calculator.evaluate("k = 2 * -3 + j - 1");
        calculator.evaluate("n = --i");  // double negation literal

        assertEquals(4, calculator.getVariables().get("i"));    // original value
        assertEquals(-5, calculator.getVariables().get("j"));   // negation of i
        assertEquals(-12, calculator.getVariables().get("k"));  // (2 * -3) + (-5) - 1
        assertEquals(4, calculator.getVariables().get("n"));   // double negation -(-(-i)) = -i = -5
    }

    @Test
    void testDecrementIntegration() {
        calculator.evaluate("a = 5");
        calculator.evaluate("b = --a");  // a=4, b=4
        calculator.evaluate("c = a-- + 10"); // c = 4 + 10 = 14, a=3
        calculator.evaluate("d = b + c");    // d = 4 + 14 = 18
        assertEquals(3, calculator.getVariables().get("a"));
        assertEquals(4, calculator.getVariables().get("b"));
        assertEquals(14, calculator.getVariables().get("c"));
        assertEquals(18, calculator.getVariables().get("d"));
    }

    @Test
    void testUnaryMinusWithAssignOps() {
        calculator.evaluate("a = 10");
        calculator.evaluate("a += -5");   // 10 + (-5) = 5
        calculator.evaluate("a -= -3");   // 5 - (-3) = 8

        Map<String, Integer> expected = Map.of(
                "a", 8
        );
        assertEquals(expected, calculator.getVariables());
    }

    @Test
    public void testMixedOperators() {
        Calculator calc = new Calculator();
        calc.evaluate("b = 5");
        calc.evaluate("b += 10 - 3");
        assertEquals(12, calc.getVariables().get("b"));
    }

    @Test
    void testIntegrationExample() {
        calculator.evaluate("i = 0");
        calculator.evaluate("j = ++i");
        calculator.evaluate("x = i++ + 5");
        calculator.evaluate("y = 5 + 3 * 10");
        calculator.evaluate("i += y");

        Map<String, Integer> expected = Map.of(
                "i", 37,
                "j", 1,
                "x", 6,
                "y", 35
        );
        assertEquals(expected, calculator.getVariables());
    }

    @Test
    void testInvalidVariableName() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                calculator.evaluate("123abc = 5")
        );
        assertTrue(ex.getMessage().contains("Invalid variable"));
    }

    @Test
    void testUnexpectedCharacters() {
        Exception ex = Assertions.<IllegalArgumentException>assertThrows(IllegalArgumentException.class, () ->
                calculator.evaluate("i = 5 @ 3")
        );
        assertTrue(ex.getMessage().contains("Unexpected character"));
    }
}
