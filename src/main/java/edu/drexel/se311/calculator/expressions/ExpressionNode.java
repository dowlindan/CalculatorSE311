package edu.drexel.se311.calculator.expressions;

import java.io.Serializable;

import edu.drexel.se311.calculator.visitors.ExpressionVisitor;


public interface ExpressionNode extends Serializable {
    <T> T accept(ExpressionVisitor<T> visitor);
}