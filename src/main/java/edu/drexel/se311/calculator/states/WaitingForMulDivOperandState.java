package edu.drexel.se311.calculator.states;

/**
 * WAITING FOR MUL/DIV OPERAND STATE
 *
 * A * or / was just pressed; waiting for the user to start typing
 * the right-hand operand for the higher-priority operation.
 *
 * Diagram transitions:
 *   0-9   → begin collecting RHS → GettingMulDivOperand
 *   +,-   → replace with add/sub operator → WaitingForAddSubOperand
 *   *,/   → replace pending mul/div operator, stay here
 *   =     → nothing meaningful yet, stay here
 *   C     → full reset → Start
 */
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
    public void onEquals(CalculatorContext ctx) { /* nothing to compute yet */ }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}