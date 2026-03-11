package edu.drexel.se311.calculator.expressions;

import java.io.Serializable;

import edu.drexel.se311.calculator.visitors.ExpressionVisitor;

/**
 * LEAF — Composite Pattern
 *
 * Represents a single numeric value in the expression tree.
 * Has no children — it is the base case of all recursive operations.
 *
 * Examples:
 *   NumberNode(3)    →  the literal 3
 *   NumberNode(4.5)  →  the literal 4.5
 */
public class NumberNode implements ExpressionNode, Serializable {

    private final int value;

    public NumberNode(int value) {
        this.value = value;
    }

    // ── ExpressionNode ────────────────────────────────────────────────────

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitNumber(this);
    }

    // ── Accessor ──────────────────────────────────────────────────────────

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        // Print as integer when there is no fractional part
        return String.valueOf(value);
    }
}