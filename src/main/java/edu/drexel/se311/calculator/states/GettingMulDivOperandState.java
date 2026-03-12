package edu.drexel.se311.calculator.states;

// User is actively typing right side of * or /
public class GettingMulDivOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        ctx.appendDigit(digit);
        // Stay in this state
    }

    // Handle lower precedence operator - evaluate pending * or / first
    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
       // Store what we have and move onto next number
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
        // evaluate the expression built so far
        ctx.submitEquals();
        ctx.transitionTo(new CalculateState());
    }


    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}