package edu.drexel.se311.calculator.expressions;

import edu.drexel.se311.calculator.visitors.ExpressionVisitor;

/**
 * COMPOSITE (branch) — Composite Pattern
 *
 * Represents a binary operation: an operator applied to two sub-expressions.
 * Both children can themselves be any ExpressionNode — a leaf NumberNode
 * or another BinaryOperationNode — enabling arbitrarily nested expressions.
 *
 * Examples:
 *   BinaryOperationNode(NumberNode(2), '+', NumberNode(3))
 *       →  2 + 3  →  5.0
 *
 *   BinaryOperationNode(
 *       NumberNode(3), '+',
 *       BinaryOperationNode(NumberNode(4), '*', NumberNode(2))
 *   )
 *       →  3 + (4 * 2)  →  11.0
 *
 * Supported operators: + - * /
 */
public class BinaryOperationNode implements ExpressionNode {

    private final ExpressionNode left;
    private final ExpressionNode right;
    private final char operator;

    public BinaryOperationNode(ExpressionNode left, char operator, ExpressionNode right) {
        this.left     = left;
        this.operator = operator;
        this.right    = right;
    }

    // ── ExpressionNode ────────────────────────────────────────────────────

    @Override
    public double evaluate() {
        double l = left.evaluate();    // recurse left subtree
        double r = right.evaluate();   // recurse right subtree

        return switch (operator) {
            case '+' -> l + r;
            case '-' -> l - r;
            case '*' -> l * r;
            case '/' -> {
                if (r == 0) throw new ArithmeticException("Division by zero");
                yield l / r;
            }
            default -> throw new IllegalArgumentException(
                "Unknown operator: " + operator
            );
        };
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitBinary(this);
    }

    // ── Accessors ─────────────────────────────────────────────────────────

    public ExpressionNode getLeft()  { return left;     }
    public ExpressionNode getRight() { return right;    }
    public char getOperator()        { return operator; }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}