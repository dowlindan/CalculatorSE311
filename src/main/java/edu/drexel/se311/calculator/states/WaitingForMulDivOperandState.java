package edu.drexel.se311.calculator.states;

// User just entered * or / and we're waiting for a number
public class WaitingForMulDivOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        ctx.setCurrentNumber(0);
        ctx.appendDigit(digit);
        ctx.transitionTo(new GettingMulDivOperandState());
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        // User changed their mind before typing anything
        ctx.setPendingOp(op);
        ctx.transitionTo(new WaitingForAddSubOperandState());
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        // Replace the pending operator (e.g. "6 * /" → just use /)
        ctx.setPendingOp(op);
        // Stay in this state
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        // do nothing
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}