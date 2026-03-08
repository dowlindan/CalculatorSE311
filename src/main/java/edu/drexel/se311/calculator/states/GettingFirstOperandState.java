package edu.drexel.se311.calculator.states;

/**
 * GETTING FIRST OPERAND STATE
 *
 * The user is typing the first number (left-hand operand).
 * Diagram transitions:
 *   0-9   → stay here (append digit)
 *   +,-   → save input as accumulator, store operator → WaitingForAddSubOperand
 *   *,/   → save input as accumulator, store operator → WaitingForMulDivOperand
 *   =     → store as accumulator → Calculate  (e.g. user just hits "5 =")
 *   C     → full reset → Start
 */
public class GettingFirstOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        // Append digit:  currentInput = currentInput * 10 + digit
        ctx.setCurrentInput(ctx.getCurrentInput() * 10 + digit);
        // Stay in this state
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        ctx.setAccumulator(ctx.getCurrentInput());
        ctx.setCurrentInput(0);
        ctx.setPendingAddSub(op);
        ctx.transitionTo(ctx.waitingForAddSub);
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        ctx.setAccumulator(ctx.getCurrentInput());
        ctx.setCurrentInput(0);
        ctx.setPendingMulDiv(op);
        ctx.transitionTo(ctx.waitingForMulDiv);
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        // "5 =" — trivially move to Calculate with the number itself
        ctx.setAccumulator(ctx.getCurrentInput());
        ctx.setCurrentInput(0);
        ctx.transitionTo(ctx.calculateState);
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }

    // Private helper to avoid recursive context call
    private void onEquals(CalculatorContext ctx, double ignored) {}
}