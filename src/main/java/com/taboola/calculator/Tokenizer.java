package com.taboola.calculator;

import java.util.LinkedList;
import java.util.Queue;

public class Tokenizer {

    private final String input;
    private int pos = 0;
    private final Queue<Token> bufferedTokens = new LinkedList<>();
    private Token currentToken;

    public Tokenizer(String input) {
        this.input = input;
        next(); // Initialize first token
    }

    public Token peek() {
        return currentToken;
    }

    public Token next() {
        if (!bufferedTokens.isEmpty()) {
            currentToken = bufferedTokens.poll();
            return currentToken;
        }

        skipWhitespace();

        if (pos >= input.length()) {
            currentToken = new Token(TokenType.EOF, "");
            return currentToken;
        }

        char ch = input.charAt(pos);

        // Numbers
        if (Character.isDigit(ch)) {
            StringBuilder sb = new StringBuilder();
            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                sb.append(input.charAt(pos++));
            }
            currentToken = new Token(TokenType.NUMBER, sb.toString());
            return currentToken;
        }

        // Identifiers (variables)
        if (Character.isLetter(ch)) {
            StringBuilder sb = new StringBuilder();
            while (pos < input.length() &&
                    (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
                sb.append(input.charAt(pos++));
            }

            // Disallow identifiers ending with an underscore
            if (sb.charAt(sb.length() - 1) == '_') {
                throw new IllegalArgumentException("Invalid variable name: cannot end with underscore (" + sb + ")");
            }

            // Look ahead for post-increment (i++)
            if (pos + 1 <= input.length() && pos < input.length()
                    && input.charAt(pos) == '+' && pos + 1 < input.length()
                    && input.charAt(pos + 1) == '+') {
                pos += 2;
                bufferedTokens.add(new Token(TokenType.POST_INC, "++"));
            }

            // Look ahead for post-decrement (i--)
            if (pos + 1 <= input.length() && pos < input.length()
                    && input.charAt(pos) == '-' && pos + 1 < input.length()
                    && input.charAt(pos + 1) == '-') {
                pos += 2;
                bufferedTokens.add(new Token(TokenType.POST_DEC, "--"));
            }

            currentToken = new Token(TokenType.IDENTIFIER, sb.toString());
            return currentToken;
        }

        // Operators
        switch (ch) {
            case '+':
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '+') {
                    pos += 2;
                    currentToken = new Token(TokenType.PRE_INC, "++");
                } else {
                    pos++;
                    currentToken = new Token(TokenType.PLUS, "+");
                }
                return currentToken;

            case '-':
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '-') {
                    pos += 2;
                    currentToken = new Token(TokenType.PRE_DEC, "--");
                } else {
                    pos++;
                    currentToken = new Token(TokenType.MINUS, "-");
                }
                return currentToken;

            case '*':
                pos++;
                currentToken = new Token(TokenType.MUL, "*");
                return currentToken;

            case '/':
                pos++;
                currentToken = new Token(TokenType.DIV, "/");
                return currentToken;

            case '=':
                pos++;
                currentToken = new Token(TokenType.ASSIGN, "=");
                return currentToken;

            default:
                throw new IllegalArgumentException("Unexpected character: " + ch);        }
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }
}
