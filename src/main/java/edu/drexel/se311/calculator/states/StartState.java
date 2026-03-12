package edu.drexel.se311.calculator.states;

// Fresh state on launch or clear
public class StartState implements CalculatorState {

    @Override
    public void onDigit(CalculatorContext ctx, int digit) {
        ctx.appendDigit(digit);
        ctx.transitionTo(new GettingFirstOperandState());
    }

    // Do nothing for all below
    @Override public void onAddSub(CalculatorContext ctx, char op) {}
    @Override public void onMulDiv(CalculatorContext ctx, char op) {}
    @Override public void onEquals(CalculatorContext ctx)          {}
    @Override public void onClear (CalculatorContext ctx)          {}
}