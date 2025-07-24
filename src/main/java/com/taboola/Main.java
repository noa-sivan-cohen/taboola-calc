package com.taboola;

import com.taboola.calculator.Calculator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        if (args.length > 0) {
            // Batch mode: read expressions from a file
            String filePath = args[0];
            try {
                for (String line : Files.readAllLines(Paths.get(filePath))) {
                    if (line.trim().isEmpty()) continue;
                    try {
                        calculator.evaluate(line);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error in line '" + line + "': " + e.getMessage());
                    }
                }
                printVariablesSingleLine(calculator.getVariables());
            } catch (Exception e) {
                System.out.println("Failed to read file: " + e.getMessage());
            }
        } else {
            // Interactive mode
            Scanner scanner = new Scanner(System.in);
            System.out.println("Taboola Calculator Interactive Mode");
            System.out.println("Type expressions or 'exit' to quit.");

            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine();
                if (line == null || line.trim().equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                if (line.trim().isEmpty()) continue;

                try {
                    calculator.evaluate(line);
                    printVariablesSingleLine(calculator.getVariables());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }

    private static void printVariablesSingleLine(Map<String, Integer> vars) {
        if (vars.isEmpty()) {
            System.out.println("()");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        boolean first = true;
        for (Map.Entry<String, Integer> entry : vars.entrySet()) {
            if (!first) sb.append(",");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        sb.append(")");
        System.out.println(sb.toString());
    }
}
