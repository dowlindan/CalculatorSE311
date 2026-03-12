package edu.drexel.se311.calculator.visitors;

import edu.drexel.se311.calculator.expressions.BinaryOperationNode;
import edu.drexel.se311.calculator.expressions.NumberNode;


public class EvaluatorVisitor implements ExpressionVisitor<Integer> {

    @Override
    public Integer visitNumber(NumberNode node) {
        return node.getValue();
    }

    @Override
    public Integer visitBinary(BinaryOperationNode node) {
        int left  = node.getLeft().accept(this);
        int right = node.getRight().accept(this);

        return switch (node.getOperator()) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right; 
            default  -> throw new IllegalArgumentException(
                "Unknown operator: " + node.getOperator()
            );
        };
    }
}