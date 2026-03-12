package edu.drexel.se311.calculator.expressions;


public class ExpressionBuilder {

    private ExpressionBuilder() {}

    public static NumberNode number(int value) {
        return new NumberNode(value);
    }

    public static BinaryOperationNode binary(int left, char operator, int right) {
        return new BinaryOperationNode(number(left), operator, number(right));
    }

    public static BinaryOperationNode binary(ExpressionNode left, char operator, int right) {
        return new BinaryOperationNode(left, operator, number(right));
    }

    public static BinaryOperationNode binary(ExpressionNode left, char operator, ExpressionNode right) {
        return new BinaryOperationNode(left, operator, right);
    }
}