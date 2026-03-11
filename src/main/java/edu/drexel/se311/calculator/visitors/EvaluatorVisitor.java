package edu.drexel.se311.calculator.visitors;

import edu.drexel.se311.calculator.expressions.BinaryOperationNode;
import edu.drexel.se311.calculator.expressions.NumberNode;

/**
 * EVALUATOR VISITOR — Visitor Pattern
 *
 * Walks the expression tree and computes the final numeric result.
 * Always run ValidatorVisitor first so this visitor can assume the
 * tree is well-formed.
 *
 * Returns Integer — the computed result of the full expression.
 *
 * Example:
 *   BinaryOperationNode(NumberNode(3), '+',
 *       BinaryOperationNode(NumberNode(4), '*', NumberNode(2)))
 *
 *   visitBinary  →  3 + (visitBinary → 4 * 2 → 8.0)  →  11.0
 */
public class EvaluatorVisitor implements ExpressionVisitor<Integer> {

    @Override
    public Integer visitNumber(NumberNode node) {
        return node.getValue();   // base case
    }

    @Override
    public Integer visitBinary(BinaryOperationNode node) {
        int left  = node.getLeft().accept(this);    // recurse left
        int right = node.getRight().accept(this);   // recurse right

        return switch (node.getOperator()) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;   // division by zero already ruled out by ValidatorVisitor
            default  -> throw new IllegalArgumentException(
                "Unknown operator: " + node.getOperator()
            );
        };
    }
}