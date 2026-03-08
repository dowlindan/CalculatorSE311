package edu.drexel.se311.calculator.expressions;

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
public class NumberNode implements ExpressionNode {

    private final double value;

    public NumberNode(double value) {
        this.value = value;
    }

    // ── ExpressionNode ────────────────────────────────────────────────────

    @Override
    public double evaluate() {
        return value;   // base case — no children to recurse into
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitNumber(this);
    }

    // ── Accessor ──────────────────────────────────────────────────────────

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        // Print as integer when there is no fractional part
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}