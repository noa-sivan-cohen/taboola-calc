package com.taboola.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    @Test
    void testSimpleNumber() {
        Tokenizer tokenizer = new Tokenizer("123");
        assertEquals(TokenType.NUMBER, tokenizer.peek().getType());
        assertEquals("123", tokenizer.peek().getText());
    }

    @Test
    void testIdentifier() {
        Tokenizer tokenizer = new Tokenizer("foo");
        assertEquals(TokenType.IDENTIFIER, tokenizer.peek().getType());
        assertEquals("foo", tokenizer.peek().getText());
    }

    @Test
    void testAssignment() {
        Tokenizer tokenizer = new Tokenizer("x = 42");
        assertEquals(TokenType.IDENTIFIER, tokenizer.peek().getType());
        assertEquals("x", tokenizer.peek().getText());

        tokenizer.next(); // x
        assertEquals(TokenType.ASSIGN, tokenizer.peek().getType());
        tokenizer.next(); // =
        assertEquals(TokenType.NUMBER, tokenizer.peek().getType());
    }

    @Test
    void testSimpleExpression() {
        Tokenizer tokenizer = new Tokenizer("i = 5 + 3");
        assertEquals(TokenType.IDENTIFIER, tokenizer.peek().getType());
        assertEquals("i", tokenizer.peek().getText());
        tokenizer.next(); // i
        assertEquals(TokenType.ASSIGN, tokenizer.peek().getType());
        tokenizer.next(); // =
        assertEquals(TokenType.NUMBER, tokenizer.peek().getType());
        assertEquals("5", tokenizer.peek().getText());
        tokenizer.next(); // 5
        assertEquals(TokenType.PLUS, tokenizer.peek().getType());
        tokenizer.next(); // +
        assertEquals(TokenType.NUMBER, tokenizer.peek().getType());
        assertEquals("3", tokenizer.peek().getText());
        tokenizer.next(); // 5
    }

    @Test
    public void testMinusToken() {
        Tokenizer tokenizer = new Tokenizer("10 - 5");
        Token token1 = tokenizer.peek();
        assertEquals(TokenType.NUMBER, token1.getType());
        assertEquals("10", token1.getText());

        tokenizer.next();  // Consume '10'
        Token token2 = tokenizer.peek();
        assertEquals(TokenType.MINUS, token2.getType());
        assertEquals("-", token2.getText());

        tokenizer.next();  // Consume '-'
        Token token3 = tokenizer.peek();
        assertEquals(TokenType.NUMBER, token3.getType());
        assertEquals("5", token3.getText());
    }

    @Test
    public void testDivisionToken() {
        Tokenizer tokenizer = new Tokenizer("10 / 5");
        Token token1 = tokenizer.peek();
        assertEquals(TokenType.NUMBER, token1.getType());
        assertEquals("10", token1.getText());

        Token token2 = tokenizer.next();
        assertEquals(TokenType.DIV, token2.getType());
        assertEquals("/", token2.getText());

        Token token3 = tokenizer.next();
        assertEquals(TokenType.NUMBER, token3.getType());
        assertEquals("5", token3.getText());
    }

    @Test
    void testPreIncrement() {
        Tokenizer tokenizer = new Tokenizer("++i");
        assertEquals(TokenType.PRE_INC, tokenizer.peek().getType());
        tokenizer.next(); // consume ++
        assertEquals(TokenType.IDENTIFIER, tokenizer.peek().getType());
        assertEquals("i", tokenizer.peek().getText());
    }

    @Test
    void testPostIncrement() {
        Tokenizer tokenizer = new Tokenizer("i++");
        assertEquals(TokenType.IDENTIFIER, tokenizer.peek().getType());
        tokenizer.next(); // consume i
        assertEquals(TokenType.POST_INC, tokenizer.peek().getType());
        tokenizer.next(); // consume ++
        assertEquals(TokenType.EOF, tokenizer.peek().getType());
    }

    @Test
    void testMixedExpression() {
        Tokenizer tokenizer = new Tokenizer("x = i++ + 5 * 2");
        assertEquals(TokenType.IDENTIFIER, tokenizer.peek().getType()); // x
        tokenizer.next();
        assertEquals(TokenType.ASSIGN, tokenizer.peek().getType()); // =
        tokenizer.next();
        assertEquals(TokenType.IDENTIFIER, tokenizer.peek().getType()); // i
        tokenizer.next();
        assertEquals(TokenType.POST_INC, tokenizer.peek().getType()); // ++
        tokenizer.next();
        assertEquals(TokenType.PLUS, tokenizer.peek().getType()); // +
        tokenizer.next();
        assertEquals(TokenType.NUMBER, tokenizer.peek().getType()); // 5
        tokenizer.next();
        assertEquals(TokenType.MUL, tokenizer.peek().getType()); // *
        tokenizer.next();
        assertEquals(TokenType.NUMBER, tokenizer.peek().getType()); // 2
    }
}
