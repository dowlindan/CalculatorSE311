package edu.drexel.se311.calculator.expressions;

/**
 * Convenience factory used by the State machine to assemble expression trees
 * without the states needing to directly instantiate node classes.
 *
 * Examples:
 *   ExpressionBuilder.number(5)
 *       → NumberNode(5)
 *
 *   ExpressionBuilder.binary(3, '+', 4)
 *       → BinaryOperationNode(NumberNode(3), '+', NumberNode(4))
 *
 *   ExpressionBuilder.binary(
 *       ExpressionBuilder.number(3), '+',
 *       ExpressionBuilder.binary(4, '*', 2)
 *   )
 *       → 3 + (4 * 2)
 */
public class ExpressionBuilder {

    // Prevent instantiation — static factory only
    private ExpressionBuilder() {}

    // ── Leaf ──────────────────────────────────────────────────────────────

    public static NumberNode number(double value) {
        return new NumberNode(value);
    }

    // ── Binary ────────────────────────────────────────────────────────────

    /** Wrap two raw doubles in a BinaryOperationNode */
    public static BinaryOperationNode binary(double left, char operator, double right) {
        return new BinaryOperationNode(number(left), operator, number(right));
    }

    /** Wrap a node and a raw double (LHS is already a tree) */
    public static BinaryOperationNode binary(ExpressionNode left, char operator, double right) {
        return new BinaryOperationNode(left, operator, number(right));
    }

    /** Wrap two existing nodes */
    public static BinaryOperationNode binary(ExpressionNode left, char operator, ExpressionNode right) {
        return new BinaryOperationNode(left, operator, right);
    }
}