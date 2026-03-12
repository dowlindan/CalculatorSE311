package edu.drexel.se311.calculator.visitors;

import edu.drexel.se311.calculator.expressions.BinaryOperationNode;
import edu.drexel.se311.calculator.expressions.NumberNode;


public interface ExpressionVisitor<T> {
    T visitNumber(NumberNode node);
    T visitBinary(BinaryOperationNode node);
}