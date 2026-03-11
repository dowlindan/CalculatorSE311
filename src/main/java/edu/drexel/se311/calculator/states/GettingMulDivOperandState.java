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
        ctx.appendDigit(digit);
        // Stay in this state
    }

    // Handle lower precedence operator - evaluate pending * or / first
    @Override
    public void onAddSub(CalculatorContext ctx, char op) {
       // e.g. 3 * 4 +: Evaluate 3*4 first, store result, then apply +
       ctx.storeLeftOperand(op);
       ctx.resetCurrentNumber();
       ctx.setPendingOp(op);
       ctx.transitionTo(new WaitingForAddSubOperandState());
    }

    @Override
    public void onMulDiv(CalculatorContext ctx, char op) {
        // e.g. "3 * 4 *": Both are same precedence, so evaluate 3*4 first
        ctx.storeLeftOperand(op);
        ctx.resetCurrentNumber();
        ctx.setPendingOp(op);
        ctx.transitionTo(new WaitingForMulDivOperandState());
    }

    @Override
    public void onEquals(CalculatorContext ctx) {
        // evaluate the expression built so far
        ctx.submitEquals();
        ctx.transitionTo(new CalculateState());
    }


    @Override
    public void onClear(CalculatorContext ctx) {
        ctx.reset();
    }
}