package edu.drexel.se311.calculator.states;

/**
 * START STATE
 *
 * The calculator has just been launched (or cleared).
 * Diagram: loops on +,-,*,/,=,C — all ignored.
 * Only a digit 0-9 moves us forward → GettingFirstOperandState.
 */
public class StartState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        ctx.setCurrentInput(digit);
        ctx.transitionTo(ctx.gettingFirstOperand);
    }

    // Everything below is a no-op in the Start state (see diagram loop)
    @Override public void onAddSub(CalculatorContext ctx, char op) { /* ignored */ }
    @Override public void onMulDiv(CalculatorContext ctx, char op) { /* ignored */ }
    @Override public void onEquals(CalculatorContext ctx)          { /* ignored */ }
    @Override public void onClear (CalculatorContext ctx)          { /* ignored */ }
}