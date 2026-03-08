package edu.drexel.se311.calculator.states;

/**
 * GETTING MUL/DIV OPERAND & CALC STATE
 *
 * The user is typing the right-hand operand for a * or / operation.
 * This state also "calculates" the mul/div result as soon as the user
 * moves on — hence the label "& Calc" on the diagram.
 *
 * Diagram transitions:
 *   0-9   → stay here (append digit)
 *   +,-   → apply pending mul/div NOW (higher precedence resolved),
 *             then store add/sub for later → WaitingForAddSubOperand
 *   *,/   → apply pending mul/div NOW, store new mul/div → WaitingForMulDivOperand
 *   =     → apply pending mul/div, then apply any pending add/sub → Calculate
 *   C     → full reset → Start
 */
public class GettingMulDivOperandState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        ctx.setCurrentInput(ctx.getCurrentInput() * 10 + digit);
        // Stay in this state
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        // e.g. "3 + 4 * 2 +"
        // First resolve the * 2  →  intermediate = 4 * 2 = 8
        // Then fold into accumulator via pending add/sub if any
        double mulDivResult = applyMulDiv(ctx);

        double newAccumulator;
        if (ctx.getPendingAddSub() != 0) {
            newAccumulator = applyAddSub(ctx.getAccumulator(),
                                         ctx.getPendingAddSub(),
                                         mulDivResult);
            ctx.setPendingAddSub((char) 0);
        } else {
            newAccumulator = mulDivResult;
        }

        ctx.setAccumulator(newAccumulator);
        ctx.setCurrentInput(0);
        ctx.setPendingMulDiv((char) 0);
        ctx.setPendingAddSub(op);
        ctx.transitionTo(ctx.waitingForAddSub);
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        // e.g. "6 * 3 *" — resolve current * 3 first, then wait for next operand
        double result = applyMulDiv(ctx);
        ctx.setAccumulator(result);
        ctx.setCurrentInput(0);
        ctx.setPendingMulDiv(op);
        ctx.transitionTo(ctx.waitingForMulDiv);
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        // Resolve mul/div first (higher precedence)
        double mulDivResult = applyMulDiv(ctx);

        // Then fold into any pending add/sub
        double finalResult;
        if (ctx.getPendingAddSub() != 0) {
            finalResult = applyAddSub(ctx.getAccumulator(),
                                      ctx.getPendingAddSub(),
                                      mulDivResult);
        } else {
            finalResult = mulDivResult;
        }

        ctx.setAccumulator(finalResult);
        ctx.setCurrentInput(0);
        ctx.setPendingMulDiv((char) 0);
        ctx.setPendingAddSub((char) 0);
        ctx.transitionTo(ctx.calculateState);
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private double applyMulDiv(CalculatorContext ctx) {
        double lhs = ctx.getAccumulator();
        double rhs = ctx.getCurrentInput();
        return switch (ctx.getPendingMulDiv()) {
            case '*' -> lhs * rhs;
            case '/' -> (rhs == 0) ? Double.NaN : lhs / rhs;
            default  -> rhs;
        };
    }

    private double applyAddSub(double lhs, char op, double rhs) {
        return switch (op) {
            case '+' -> lhs + rhs;
            case '-' -> lhs - rhs;
            default  -> rhs;
        };
    }
}