package edu.drexel.se311.calculator.states;

import java.util.ArrayList;
import java.util.List;

import edu.drexel.se311.calculator.expressions.ExpressionBuilder;
import edu.drexel.se311.calculator.expressions.ExpressionNode;

// Handles Expressions and Precedence 
public class ExpressionEngine {

    private int currentNumber = 0;
    private List<Integer> operands = new ArrayList<>();
    private List<Character> operators = new ArrayList<>();

    private boolean inputActive = false;

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

    // Math operations
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
        if (node != null) {
            operands.clear();
            operators.clear();
        }
    }

    public void storeLeftOperand(char newOp) {
        operands.add(currentNumber);
        operators.add(newOp);
    }


    // Convert lists to expression nodes
    public ExpressionNode evaluate() {
        if (operands.isEmpty()) {
            return ExpressionBuilder.number(currentNumber);
        }

        List<ExpressionNode> allOperands = new ArrayList<>();
        for (int operand : operands) {
            allOperands.add(ExpressionBuilder.number(operand));
        }
        allOperands.add(ExpressionBuilder.number(currentNumber));

        return buildTreeWithPrecedence(allOperands, operators);
    }

    // Build tree from expression nodes
    private ExpressionNode buildTreeWithPrecedence(List<ExpressionNode> allOperands, List<Character> allOperators) {
        
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

        // Build left side
        List<ExpressionNode> leftOperands = new ArrayList<>(
            allOperands.subList(0, rootIdx + 1)
        );
        List<Character> leftOperators = new ArrayList<>(
            allOperators.subList(0, rootIdx)
        );
        ExpressionNode left = buildTreeWithPrecedence(leftOperands, leftOperators);

        // Build right side
        List<ExpressionNode> rightOperands = new ArrayList<>(
            allOperands.subList(rootIdx + 1, allOperands.size())
        );
        List<Character> rightOperators = new ArrayList<>(
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