package edu.drexel.se311.calculator.expressions;

import java.io.Serializable;

import edu.drexel.se311.calculator.visitors.ExpressionVisitor;

/**
 * COMPONENT — Composite Pattern
 *
 * The common interface shared by every node in the expression tree,
 * whether it is a leaf (a number) or a branch (an operation).
 *
 * Two responsibilities:
 *   1. evaluate() — can compute its own numeric result recursively
 *   2. accept()   — entry point for the Visitor pattern; hands itself
 *                   to whatever visitor is currently walking the tree
 *
 * The generic <T> on accept() lets different visitors return different
 * types:
 *   EvaluatorVisitor   → Int
 */
public interface ExpressionNode extends Serializable {
    <T> T accept(ExpressionVisitor<T> visitor);
}