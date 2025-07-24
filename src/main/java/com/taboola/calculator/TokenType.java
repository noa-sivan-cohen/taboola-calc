package com.taboola.calculator;

public enum TokenType {
    // Operators for arithmetic
    PLUS,           // '+' addition operator
    MINUS,          // '-' subtraction operator
    MUL,            // '*' multiplication operator
    DIV,            // '/' division operator

    // Assignment operators
    ASSIGN,         // '=' assignment (e.g., x = 5)
    PLUS_ASSIGN,    // '+=' compound assignment (e.g., x += 5)
    MINUS_ASSIGN,   // '-=' compound assignment (e.g., x -= 5)

    // Increment tokens
    PRE_INC,        // '++i' pre-increment (increment before using the variable)
    POST_INC,       // 'i++' post-increment (increment after using the variable)
    PRE_DEC,        // '--i' pre-decrement (decrement before using the variable)
    POST_DEC,       // 'i--' post-decrement (decrement after using the variable)

    // Literals and variables
    NUMBER,         // Numeric literal (e.g., 123)
    IDENTIFIER,     // Variable name (e.g., x, counter1)

    EOF             // End of input marker
}