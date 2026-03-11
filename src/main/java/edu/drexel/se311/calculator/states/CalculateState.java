package edu.drexel.se311.calculator.states;

/**
 * CALCULATE STATE
 *
 * The final result is ready in ctx.getAccumulator().
 * This state is a momentary landing point — the GUI reads the result
 * and displays it.  From here:
 *
 * Diagram transitions:
 *   0-9   → start a brand-new expression → GettingFirstOperand
 *   +,-   → chain the result into a new add/sub expression → WaitingForAddSub
 *   *,/   → chain the result into a new mul/div expression → WaitingForMulDiv
 *   =     → stay here (repeated = keeps showing same result)
 *   C     → full reset → Start
 */
public class CalculateState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        // Start fresh: reset the calculator and begin typing the new number
        ctx.reset();
        ctx.appendDigit(digit);
        ctx.transitionTo(new GettingFirstOperandState());
    }

    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
        // Chain: use the displayed result as the new left-hand side
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
        // Repeated = — do nothing, result stays displayed
    }

    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}