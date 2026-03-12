package edu.drexel.se311.calculator.states;


public interface CalculatorState {
    void onDigit  (CalculatorContext ctx, int digit);
    void onAddSub (CalculatorContext ctx, char op);
    void onMulDiv (CalculatorContext ctx, char op);
    void onEquals (CalculatorContext ctx);
    void onClear  (CalculatorContext ctx);
}