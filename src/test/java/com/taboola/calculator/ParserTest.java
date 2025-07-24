package com.taboola.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private Map<String, Integer> vars;

    @BeforeEach
    void setup() {
        vars = new HashMap<>();
    }

    /**
     * Helper to quickly parse a single expression.
     */
    private int parse(String expr) {
        Tokenizer tokenizer = new Tokenizer(expr);
        Parser parser = new Parser(tokenizer, vars);
        return parser.parseExpression();
    }

    @Test
    void testSimpleNumber() {
        assertEquals(42, parse("42"));
    }

    @Test
    void testVariableDefaultZero() {
        assertEquals(0, parse("x")); // x is not initialized yet, defaults to 0
    }

    @Test
    void testAddition() {
        assertEquals(7, parse("3 + 4"));
    }

    @Test
    void testMultiplicationPrecedence() {
        assertEquals(23, parse("3 + 4 * 5")); // Should do 4*5 first -> 3+20=23
    }

    @Test
    void testPreIncrement() {
        vars.put("i", 0);
        assertEquals(1, parse("++i"));  // Pre-increment → increase to 1 and return 1
        assertEquals(1, vars.get("i"));
    }

    @Test
    void testPostIncrement() {
        vars.put("i", 0);
        assertEquals(0, parse("i++"));  // Post-increment → return old 0, then increment to 1
        assertEquals(1, vars.get("i"));
    }

    @Test
    void testCombinationPrePost() {
        vars.put("i", 1);
        assertEquals(4, parse("++i + i++")); // ++i=2, i++=2, sum=4
        assertEquals(3, vars.get("i"));      // בסוף i=3
    }

    @Test
    public void testSimpleSubtraction() {
        Parser parser = new Parser(new Tokenizer("10 - 4"), new HashMap<>());
        int result = parser.parseExpression();
        assertEquals(6, result);
    }

    @Test
    public void testUnaryMinusNumber() {
        Parser parser = new Parser(new Tokenizer("-5"), new HashMap<>());
        int result = parser.parseExpression();
        assertEquals(-5, result);
    }

    @Test
    public void testUnaryMinusVariable() {
        HashMap<String, Integer> vars = new HashMap<>();
        vars.put("x", 8);
        Parser parser = new Parser(new Tokenizer("-x"), vars);
        int result = parser.parseExpression();
        assertEquals(-8, result);
    }

    @Test
    public void testUnaryMinusWithMultiplication() {
        Parser parser = new Parser(new Tokenizer("2 * -3"), new HashMap<>());
        int result = parser.parseExpression();  // 2 * (-3) = -6
        assertEquals(-6, result);
    }

    @Test
    public void testUnaryMinusWithDivision() {
        Parser parser = new Parser(new Tokenizer("-12 / 3"), new HashMap<>());
        int result = parser.parseExpression();  // (-12) / 3 = -4
        assertEquals(-4, result);
    }

    @Test
    public void testUnaryMinusMixedExpression() {
        HashMap<String, Integer> vars = new HashMap<>();
        vars.put("x", 4);
        Parser parser = new Parser(new Tokenizer("-x * 2 + 10"), vars);
        int result = parser.parseExpression();  // (-4) * 2 + 10 = 2
        assertEquals(2, result);
    }

    @Test
    public void testPreDecrement() {
        HashMap<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        Parser parser = new Parser(new Tokenizer("--x"), vars);
        int result = parser.parseExpression();
        assertEquals(4, result);
        assertEquals(4, vars.get("x"));
    }

    @Test
    public void testPostDecrement() {
        HashMap<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        Parser parser = new Parser(new Tokenizer("x--"), vars);
        int result = parser.parseExpression();
        assertEquals(5, result);   // returns old value
        assertEquals(4, vars.get("x"));  // x decremented after
    }

    @Test
    public void testPreAndPostDecrementMix() {
        HashMap<String, Integer> vars = new HashMap<>();
        vars.put("x", 3);
        Parser parser = new Parser(new Tokenizer("--x + x--"), vars);
        int result = parser.parseExpression(); // (--3=2) + (2, then 1) = 4
        assertEquals(4, result);
        assertEquals(1, vars.get("x"));
    }

    @Test
    public void testMixedAdditionAndSubtraction() {
        Parser parser = new Parser(new Tokenizer("10 + 5 - 3"), new HashMap<>());
        int result = parser.parseExpression();
        assertEquals(12, result);
    }

    @Test
    public void testSimpleDivision() {
        Parser parser = new Parser(new Tokenizer("20 / 4"), new HashMap<>());
        int result = parser.parseExpression();
        assertEquals(5, result);
    }

    @Test
    public void testDivisionByZero() {
        Parser parser = new Parser(new Tokenizer("10 / 0"), new HashMap<>());
        ArithmeticException ex = assertThrows(ArithmeticException.class, parser::parseExpression);
        assertEquals("Division by zero", ex.getMessage());
    }

    @Test
    public void testDivisionWithMultiplication() {
        Parser parser = new Parser(new Tokenizer("20 / 4 * 2"), new HashMap<>());
        int result = parser.parseExpression();
        assertEquals(10, result);
    }

    @Test
    public void testSubtractionWithVariables() {
        HashMap<String, Integer> vars = new HashMap<>();
        vars.put("x", 7);
        Parser parser = new Parser(new Tokenizer("x - 2"), vars);
        int result = parser.parseExpression();
        assertEquals(5, result);
    }

    @Test
    void testUnknownToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            Tokenizer tokenizer = new Tokenizer("@");
            Parser parser = new Parser(tokenizer, vars);
            parser.parseExpression();
        });
    }

    @Test
    void testMultipleOperationsWithVariables() {
        vars.put("x", 2);
        vars.put("y", 3);
        assertEquals(11, parse("x * y + 5")); // 2*3 +5 = 11
    }
}
