package edu.drexel.se311.calculator.states;

// User just pressed +/- and we are waiting for the number
public class WaitingForAddSubOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        ctx.resetCurrentNumber();
        ctx.appendDigit(digit);
        ctx.transitionTo(new GettingAddSubOperandState());
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        // User changed mind (e.g. typed "5 + -") — replace the operator
        ctx.setPendingOp(op);
        // Stay in this state
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
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