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
        ctx.appendDigit(digit);
        // Stay in this state
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
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
        // "5 =" — simply evaluate what we've got (single number)
        ctx.submitEquals();
        ctx.transitionTo(new CalculateState());
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}