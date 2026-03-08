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
        ctx.setCurrentInput(ctx.getCurrentInput() * 10 + digit);
        // Stay in this state
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        // Apply the pending + or - now, then wait for next operand
        double result = applyAddSub(ctx);
        ctx.setAccumulator(result);
        ctx.setCurrentInput(0);
        ctx.setPendingAddSub(op);
        ctx.transitionTo(ctx.waitingForAddSub);
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        // e.g. "3 + 4 *": the 4 becomes the LHS of the upcoming * or /
        // The pending add/sub stays stored; mul/div takes priority next
        ctx.setAccumulator(ctx.getCurrentInput());
        ctx.setCurrentInput(0);
        ctx.setPendingMulDiv(op);
        ctx.transitionTo(ctx.waitingForMulDiv);
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        double result = applyAddSub(ctx);
        ctx.setAccumulator(result);
        ctx.setCurrentInput(0);
        ctx.setPendingAddSub((char) 0);
        ctx.transitionTo(ctx.calculateState);
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }

    // ── Helper ────────────────────────────────────────────────────────────
    private double applyAddSub(CalculatorContext ctx) {
        double lhs = ctx.getAccumulator();
        double rhs = ctx.getCurrentInput();
        return switch (ctx.getPendingAddSub()) {
            case '+' -> lhs + rhs;
            case '-' -> lhs - rhs;
            default  -> rhs;   // no operator stored — just use rhs
        };
    }
}