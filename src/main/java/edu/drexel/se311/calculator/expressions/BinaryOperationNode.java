package edu.drexel.se311.calculator.expressions;

import java.io.Serializable;

import edu.drexel.se311.calculator.visitors.ExpressionVisitor;


public class BinaryOperationNode implements ExpressionNode, Serializable {

    private final ExpressionNode left;
    private final ExpressionNode right;
    private final char operator;

    public BinaryOperationNode(ExpressionNode left, char operator, ExpressionNode right) {
        this.left     = left;
        this.operator = operator;
        this.right    = right;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitBinary(this);
    }

    public ExpressionNode getLeft() {
        return left;     
    }

    public ExpressionNode getRight() {
        return right;    
    }
    
    public char getOperator() { 
        return operator; 
    }

    @Override
    public String toString() {
        return left.toString() + operator + right.toString();
    }
}