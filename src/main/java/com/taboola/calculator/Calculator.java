package com.taboola.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Calculator {

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);

    private final Map<String, Integer> variables = new LinkedHashMap<>();

    public void evaluate(String line) {
        logger.info("Evaluating line: {}", line);
        line = line.trim();
        if (line.isEmpty()) {
            logger.warn("Empty input line");
            return;
        }

        String[] parts;
        String varName;
        String operator;

        if (line.contains("+=")) {
            parts = line.split("\\+=");
            operator = "+=";
        } else if (line.contains("-=")) {
            parts = line.split("-=");
            operator = "-=";
        } else if (line.contains("=")) {
            parts = line.split("=");
            operator = "=";
        } else {
            throw new IllegalArgumentException("Line must contain '=', '+=', or '-=' operator: " + line);
        }

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid assignment expression: " + line);
        }

        varName = parts[0].trim();
        if (!isValidVariableName(varName)) {
            throw new IllegalArgumentException("Invalid variable name: " + varName);
        }
        String expr = parts[1].trim();

        Parser parser = new Parser(new Tokenizer(expr), variables);
        int rightValue = parser.parseExpression();

        int newValue;
        if ("=".equals(operator)) {
            newValue = rightValue;
        } else if ("+=".equals(operator)) {
            int oldValue = variables.getOrDefault(varName, 0);
            newValue = oldValue + rightValue;
        } else { // "-="
            int oldValue = variables.getOrDefault(varName, 0);
            newValue = oldValue - rightValue;
        }

        variables.put(varName, newValue);
        logger.info("Assigned {} = {}", varName, newValue);
    }

    public Map<String, Integer> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    private boolean isValidVariableName(String var) {
        return var.matches("[a-zA-Z][a-zA-Z0-9_]*");
    }
}