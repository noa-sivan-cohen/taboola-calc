# Text-Based Calculator

A safe, modular, and testable **Java-based calculator** that evaluates assignment expressions, including:
- Arithmetic (`+`, `-`, `*`, `/`)
- Pre- and post-increment (`++i`, `i++`)
- Pre- and post-decrement (`--i`, `i--`)
- Unary negation (`-3`, `-i`)
- Compound assignment (`+=`, `-=`)
- Variable storage and reuse

The project is designed for clarity, modularity, and testability, with logging and full unit test coverage (JUnit 5).

---
## Running the Calculator

You can run the calculator in two modes:

1. **Interactive mode** (default):  
   Run without any arguments to enter expressions interactively in the console.  
   Type expressions and press Enter to evaluate.  
   Type `exit` to quit.

   ```bash
   mvn compile exec:java -Dexec.mainClass="com.taboola.calculator.Main"
   
2. **Batch mode** (file input):  
   Pass a file path as the first command-line argument to evaluate all lines in the file sequentially.
   After processing, the final variables are printed in a single line, e.g., (i=37,j=1,x=6,y=35).

   ```bash
   mvn compile exec:java -Dexec.mainClass="com.taboola.calculator.Main" -Dexec.args="expressions.txt"

Where expressions.txt is a text file containing one expression per line.  
   Errors in any line are printed but do not stop the processing of the rest of the file.

---
## Features

- Parses and evaluates a subset of Java-like numeric expressions.
- Supports:
    - Standard assignments (`x = 5 + 2`, `y = -x / 3`)
    - Compound assignments (`x += 3`, `y -= 2`)
    - Operator precedence (`*` and `/` before `+` and `-`)
    - Pre/post-increment (`++i`, `i++`) and pre/post-decrement (`--i`, `i--`)
    - Unary minus (`-3`, `-i`)
- Modular architecture:
    - **Calculator** (entry point)
    - **Parser** (recursive descent evaluator)
    - **Tokenizer** (lexical analyzer)
    - **Token & TokenType** (data structures)
- Extensive **unit tests** using JUnit 5.

---

## Assumptions

Since the original assignment didn’t specify every detail, I made the following assumptions:

1. **Whitespace Handling**
    - Every operator and operand must be separated by spaces.  
      Example: `"i = 5 + 3"`.  
      No compact inputs like `"3+4"`.
    - Lines may contain leading or trailing spaces, which the tokenizer ignores.

2. **Variables**
    - Must start with a letter, followed by letters, digits, or underscores (e.g., `abc_1`).
    - Undefined variables default to `0` when first referenced.

3. **Operators Supported**
    - Assignments: `=`, `+=`, `-=`
    - Arithmetic: `+`, `-`, `*`, `/`
    - Pre/post increment: `++i`, `i++`
    - Pre/post decrement: `--i`, `i--`
    - Unary negation: `-3`, `-i``
    - **Not allowed (will throw `IllegalArgumentException`):**
        - Triple or chained decrements on literals (e.g., `---i`, `--3`).

4. **Division**
    - Integer division only (fractional parts are truncated `7 / 3 = 2`).
    - Division by zero throws `IllegalArgumentException`.

5. **Error Handling**
    - Invalid syntax, illegal characters, chained decrements on literals, or division by zero throw `IllegalArgumentException`.
    - The interactive runner (`Main.java`) catches errors and prints messages without stopping the program.

6. **Overflow**
    - Calculations will never exceed `Integer.MAX_VALUE`.
    - Overflow handling is not required.

---

## Example Usage

### Input
```text
i = 0
j = ++i
x = i++ + 5
y = 5 + 3 * 10
i += y
a = 10
b = --a
c = a-- + 2
z = -b * 2 / 4
