package edu.drexel.se311.calculator.visitors;

import edu.drexel.se311.calculator.expressions.BinaryOperationNode;
import edu.drexel.se311.calculator.expressions.NumberNode;

/**
 * VISITOR INTERFACE — Visitor Pattern
 *
 * Declares one visit method per concrete node type in the expression tree.
 * Every concrete visitor implements this interface and provides its own
 * logic for each node type.
 *
 * The generic <T> is the return type of each visit — different visitors
 * return different things:
 *
 *   EvaluatorVisitor    → Double  (the computed result)
 *   PrettyPrintVisitor  → String  (formatted expression e.g. "3 + 4 * 2")
 *   ValidatorVisitor    → Void    (throws on invalid input, else silent)
 */
public interface ExpressionVisitor<T> {
    T visitNumber(NumberNode node);
    T visitBinary(BinaryOperationNode node);
}