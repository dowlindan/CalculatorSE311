package edu.drexel.se311.calculator.states;


public class CalculateState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        // Start fresh: reset the calculator and begin typing the new number
        ctx.reset();
        ctx.appendDigit(digit);
        ctx.transitionTo(new GettingFirstOperandState());
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        // Chain: use the displayed result as the new left-hand side
        ctx.storeLeftOperand(op);
        ctx.resetCurrentNumber();
        ctx.setPendingOp(op);
        ctx.transitionTo(new WaitingForAddSubOperandState());
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        ctx.storeLeftOperand(op);
        ctx.resetCurrentNumber();
        ctx.setPendingOp(op);
        ctx.transitionTo(new WaitingForMulDivOperandState());
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        // Do nothing
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}