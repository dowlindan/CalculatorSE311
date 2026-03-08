package edu.drexel.se311.calculator.states;

/**
 * Common contract for every state in the calculator state machine.
 *
 * Each concrete state decides how to react to the four possible
 * input categories that the UI can emit:
 *
 *   onDigit(d)      – user pressed 0-9
 *   onAddSub(op)    – user pressed + or -
 *   onMulDiv(op)    – user pressed * or /
 *   onEquals()      – user pressed =
 *   onClear()       – user pressed C
 *
 * The context object (CalculatorContext) is passed into every method
 * so that a state can read/write shared data and trigger a transition.
 */
public interface CalculatorState {
    void onDigit  (CalculatorContext ctx, int digit);
    void onAddSub (CalculatorContext ctx, char op);
    void onMulDiv (CalculatorContext ctx, char op);
    void onEquals (CalculatorContext ctx);
    void onClear  (CalculatorContext ctx);
}