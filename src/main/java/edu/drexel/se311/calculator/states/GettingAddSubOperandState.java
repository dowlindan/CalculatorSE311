package edu.drexel.se311.calculator.states;

// User is actively typing right hand side of +/o
public class GettingAddSubOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        // ex: 3 + 3(3) => 3 + 33
       ctx.appendDigit(digit);
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
       // Store number and move onto next
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
        ctx.submitEquals();
        ctx.transitionTo(new CalculateState());
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}