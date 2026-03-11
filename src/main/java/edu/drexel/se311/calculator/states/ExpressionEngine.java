package edu.drexel.se311.calculator.states;

import edu.drexel.se311.calculator.expressions.ExpressionBuilder;
import edu.drexel.se311.calculator.expressions.ExpressionNode;

/**
 * EXPRESSION ENGINE
 *
 * Responsible for tracking the current expression being typed and building
 * an expression tree when "=" is pressed. The engine does NOT perform any
 * arithmetic — that is strictly the responsibility of EvaluatorVisitor.
 *
 * The engine tracks:
 *   - operands (numbers): collected as the user types
 *   - operators: recorded as the user presses operator buttons
 *   - currentNumber: the number being actively typed
 *
 * When evaluate() is called, it builds an ExpressionNode tree that respects
 * operator precedence, then passes it to EvaluatorVisitor for calculation.
 *
 * CHAINING:
 * After "=" completes, the result stays in currentNumber so that the next
 * operator will use it as the left operand.
 * Example: "3 + 3 =" gives 6, then "+ 3 =" gives 9.
 */
public class ExpressionEngine {

    private int currentNumber = 0;
    private java.util.List<Integer> operands = new java.util.ArrayList<>();
    private java.util.List<Character> operators = new java.util.ArrayList<>();

    // whether the user has typed at least one digit for the currentNumber
    // (otherwise currentNumber==0 may simply be the default value)
    private boolean inputActive = false;

    // ── Helper method to determine operator precedence ────────────────────
    /**
     * Returns the precedence level of an operator.
     * Higher values = higher precedence.
     */
    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    // --- basic operations used by states --------------------------------

    public void appendDigit(int digit) {
        currentNumber = currentNumber * 10 + digit;
        inputActive = true;
    }

    public void resetCurrentNumber() {
        currentNumber = 0;
        inputActive = false;
    }

    public void setCurrentNumber(int value) {
        currentNumber = value;
        inputActive = true;
    }

    public void setPendingOp(char op) {
        if (!operators.isEmpty()) {
            // Replace the last operator
            operators.set(operators.size() - 1, op);
        } else {
            operators.add(op);
        }
    }

    public char getPendingOp() {
        if (operators.isEmpty()) {
            return 0;
        }
        return operators.get(operators.size() - 1);
    }

    public ExpressionNode getLeftOperand() {
        if (operands.isEmpty()) {
            return null;
        }
        return ExpressionBuilder.number(operands.get(0));
    }

    public void setLeftOperand(ExpressionNode node) {
        // For compatibility with existing code
        // This is called during equals to set up chaining
        if (node != null) {
            operands.clear();
            operators.clear();
            // Extract the value from the NumberNode if possible
            // For now, this will be called with a NumberNode representing the result
        }
    }

    /**
     * Called when an operator button is hit. Records the current number as an
     * operand and prepares to collect the next number.
     *
     * @param newOp the operator that was just pressed
     */
    public void storeLeftOperand(char newOp) {
        // Add the current number as an operand
        operands.add(currentNumber);
        // Add the new operator
        operators.add(newOp);
    }

    /**
     * Legacy version without precedence handling.
     * Used for backward compatibility if needed.
     */
    public void storeLeftOperand() {
        storeLeftOperand('+'); // Default to lowest precedence
    }

    /**
     * Build an expression tree that respects operator precedence, then return it
     * for the EvaluatorVisitor to evaluate. This engine does NOT perform any
     * arithmetic — only tree construction.
     *
     * @return an ExpressionNode tree ready for evaluation
     */
    public ExpressionNode evaluate() {
        if (operands.isEmpty()) {
            // Just a single number
            return ExpressionBuilder.number(currentNumber);
        }

        // Build lists of all operands (including the current one) and operators
        java.util.List<ExpressionNode> allOperands = new java.util.ArrayList<>();
        for (int operand : operands) {
            allOperands.add(ExpressionBuilder.number(operand));
        }
        allOperands.add(ExpressionBuilder.number(currentNumber));

        java.util.List<Character> allOperators = new java.util.ArrayList<>(operators);

        // Build tree from the list, respecting precedence
        return buildTreeWithPrecedence(allOperands, allOperators);
    }

    /**
     * Recursively build a tree from operands and operators, respecting precedence.
     * This is a classic shunting-yard style approach applied to tree building.
     */
    private ExpressionNode buildTreeWithPrecedence(
            java.util.List<ExpressionNode> allOperands,
            java.util.List<Character> allOperators) {
        
        if (allOperands.size() == 1) {
            return allOperands.get(0);
        }

        // Find the lowest-precedence operator (rightmost if tied)
        // This will be the root of our tree
        int rootIdx = 0;
        int minPrecedence = precedence(allOperators.get(0));
        for (int i = 1; i < allOperators.size(); i++) {
            int prec = precedence(allOperators.get(i));
            if (prec <= minPrecedence) {
                minPrecedence = prec;
                rootIdx = i;
            }
        }

        // Split into left and right subtrees at the root operator
        char rootOp = allOperators.get(rootIdx);

        // Left side: operands[0..rootIdx] and operators[0..rootIdx-1]
        java.util.List<ExpressionNode> leftOperands = new java.util.ArrayList<>(
            allOperands.subList(0, rootIdx + 1)
        );
        java.util.List<Character> leftOperators = new java.util.ArrayList<>(
            allOperators.subList(0, rootIdx)
        );
        ExpressionNode left = buildTreeWithPrecedence(leftOperands, leftOperators);

        // Right side: operands[rootIdx+1..end] and operators[rootIdx+1..end]
        java.util.List<ExpressionNode> rightOperands = new java.util.ArrayList<>(
            allOperands.subList(rootIdx + 1, allOperands.size())
        );
        java.util.List<Character> rightOperators = new java.util.ArrayList<>(
            allOperators.subList(rootIdx + 1, allOperators.size())
        );
        ExpressionNode right = buildTreeWithPrecedence(rightOperands, rightOperators);

        return ExpressionBuilder.binary(left, rootOp, right);
    }

    public String currentExpressionString() {
        String expr = "";
        
        // Add all operands and operators
        for (int i = 0; i < operands.size(); i++) {
            expr += operands.get(i);
            if (i < operators.size()) {
                expr += operators.get(i);
            }
        }
        
        // Add the current number being typed
        if (inputActive || operators.isEmpty()) {
            expr += currentNumber;
        }
        
        return expr;
    }

    public void reset() {
        currentNumber = 0;
        operands.clear();
        operators.clear();
        inputActive = false;
    }
}