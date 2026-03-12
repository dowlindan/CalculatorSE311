package edu.drexel.se311.calculator.states;

// User is actively typing the first number
public class GettingFirstOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        ctx.appendDigit(digit);
        // Stay in this state
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        // Store number, wait for next
        ctx.storeLeftOperand(op);
        ctx.resetCurrentNumber();
        ctx.setPendingOp(op);
        ctx.transitionTo(new WaitingForAddSubOperandState());
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        // Same as above
        ctx.storeLeftOperand(op);
        ctx.resetCurrentNumber();
        ctx.setPendingOp(op);
        ctx.transitionTo(new WaitingForMulDivOperandState());
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        // Evaluate what we have
        ctx.submitEquals();
        ctx.transitionTo(new CalculateState());
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}