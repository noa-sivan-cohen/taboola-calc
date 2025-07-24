package com.taboola.calculator;

public class Token {
    private final TokenType type;
    private final String text;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", text='" + text + '\'' + '}';
    }
}