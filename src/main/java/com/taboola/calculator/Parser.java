package com.taboola.calculator;

import java.util.Map;

/**
 * Parser evaluates expressions using recursive descent.
 * It relies on the Tokenizer to break the input into tokens.
 * Handles operator precedence, variables, and increments.
 */
public class Parser {
    private final Tokenizer tokenizer;           // Supplies tokens from the input string
    private final Map<String, Integer> variables; // Variables and their values

    public Parser(Tokenizer tokenizer, Map<String, Integer> variables) {
        this.tokenizer = tokenizer;
        this.variables = variables;
    }

    /**
     * parseExpression:
     * Entry point for evaluating an expression.
     * Handles addition (lowest precedence).
     * Grammar (simplified):
     *   Expression := Term (PLUS Term)*
     */
    public int parseExpression() {
        int value = parseTerm();  // Start with first term

        // Keep adding while we see a PLUS or MINUS token
        while (tokenizer.peek().getType() == TokenType.PLUS || tokenizer.peek().getType() == TokenType.MINUS) {
            TokenType type = tokenizer.peek().getType();

            if (type == TokenType.PLUS) {
                tokenizer.next();       // Consume '+'
                int rhs = parseTerm();  // Parse term after '+'
                value += rhs;
            } else if (type == TokenType.MINUS) {
                tokenizer.next();       // Consume '-'
                int rhs = parseTerm();  // Parse term after '-'
                value -= rhs;
            } else {
                break; // No more '+' or '-'
            }
        }

        return value;
    }

    /**
     * parseTerm:
     * Handles multiplication (higher precedence than addition).
     * Grammar:
     *   Term := Factor (MUL Factor)*
     */
    private int parseTerm() {
        int value = parseFactor();  // Start with first factor

        while (tokenizer.peek().getType() == TokenType.MUL || tokenizer.peek().getType() == TokenType.DIV) {
            if (tokenizer.peek().getType() == TokenType.MUL) {
                tokenizer.next();                 // Consume '*'
                int rhs = parseFactor();          // Parse the next factor
                value *= rhs;
            } else {
                tokenizer.next();                 // Consume '/'
                int rhs = parseFactor();          // Parse the next factor

                if (rhs == 0) {
                    throw new ArithmeticException("Division by zero");
                }

                value /= rhs;
            }
        }

        return value;
    }

    /**
     * parseFactor:
     * Handles the smallest building blocks:
     * - Numbers
     * - Variables
     * - Pre-increment (++i)
     * - Post-increment (i++)
     * Grammar:
     *   Factor := NUMBER
     *           | IDENTIFIER [POST_INC]
     *           | PRE_INC IDENTIFIER
     */
    private int parseFactor() {
        Token token = tokenizer.peek();

        switch (token.getType()) {
            case MINUS:
                // Handle unary minus (negation)
                tokenizer.next();  // Consume '-'
                int negatedValue = parseFactor();  // Recursively parse the next factor
                return -negatedValue;

            case NUMBER:
                tokenizer.next();  // Consume the number token
                return Integer.parseInt(token.getText());

            case PRE_INC:
                tokenizer.next();  // Consume '++'
                if (tokenizer.peek().getType() != TokenType.IDENTIFIER) {
                    throw new IllegalArgumentException("Expected variable after ++");
                }
                String varName = tokenizer.peek().getText();
                tokenizer.next(); // Consume variable

                int newVal = variables.getOrDefault(varName, 0) + 1;
                variables.put(varName, newVal);
                return newVal; // Pre-increment returns *new* value


            case PRE_DEC:
                tokenizer.next();
                if (tokenizer.peek().getType() != TokenType.IDENTIFIER) {
                    throw new IllegalArgumentException("Expected variable after --");
                }
                String varDec = tokenizer.peek().getText();
                tokenizer.next();
                int newDec = variables.getOrDefault(varDec, 0) - 1;
                variables.put(varDec, newDec);
                return newDec;

            case IDENTIFIER:  // Could be plain variable or "i++"
                tokenizer.next();  // Consume the variable
                String name = token.getText();
                int currentVal = variables.getOrDefault(name, 0);

                // Handle post-increment and post-decrement
                if (tokenizer.peek().getType() == TokenType.POST_INC) {
                    tokenizer.next();
                    variables.put(name, currentVal + 1);
                    return currentVal;
                } else if (tokenizer.peek().getType() == TokenType.POST_DEC) {
                    tokenizer.next();
                    variables.put(name, currentVal - 1);
                    return currentVal;
                }

                return currentVal;

            default:
                throw new IllegalArgumentException("Unexpected token in expression: " + token.getText());
        }
    }
}
