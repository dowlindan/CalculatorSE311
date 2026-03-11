package edu.drexel.se311.calculator.states;

/**
 * GETTING ADD/SUB OPERAND STATE
 *
 * The user is typing the right-hand operand for a + or - operation.
 *
 * Diagram transitions:
 *   0-9   → stay here (append digit)
 *   +,-   → apply pending add/sub, store new operator → WaitingForAddSubOperand
 *   *,/   → keep add/sub pending, store mul/div → WaitingForMulDivOperand
 *             (precedence: the * or / binds tighter and goes first)
 *   =     → apply pending add/sub → Calculate
 *   C     → full reset → Start
 */
public class GettingAddSubOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        // ex: 3 + 3(3) => 3 + 33
       ctx.appendDigit(digit);
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
       // e.g. 3 + 4 +: Check precedence; lower-prec causes evaluation
       ctx.storeLeftOperand(op);
       ctx.resetCurrentNumber();
       ctx.setPendingOp(op);
       ctx.transitionTo(new WaitingForAddSubOperandState());
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        // e.g. "3 + 4 *": + has lower precedence so defer evaluation;
        // * will be applied to 4 first, then + will apply the result to 3
        ctx.storeLeftOperand(op);
        ctx.resetCurrentNumber();
        ctx.setPendingOp(op);
        ctx.transitionTo(new WaitingForMulDivOperandState());
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        // e.g. (expr) + 4 =
        ctx.submitEquals();
        ctx.transitionTo(new CalculateState());
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}