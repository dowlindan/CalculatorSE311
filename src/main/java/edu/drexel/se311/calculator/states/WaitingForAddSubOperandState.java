package edu.drexel.se311.calculator.states;

/**
 * WAITING FOR ADD/SUB OPERAND STATE
 *
 * A + or - was just pressed; we are waiting for the user to start
 * typing the right-hand operand.
 *
 * Diagram transitions:
 *   0-9   → begin collecting RHS → GettingAddSubOperand
 *   +,-   → replace pending operator (user changed their mind), stay here
 *   *,/   → switch to mul/div priority → WaitingForMulDivOperand
 *   =     → nothing meaningful yet, stay here
 *   C     → full reset → Start
 */
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
    public void onEquals(CalculatorContext ctx) { /* nothing to compute yet */ }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}