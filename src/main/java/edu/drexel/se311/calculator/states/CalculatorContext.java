package edu.drexel.se311.calculator.states;

import java.util.ArrayList;
import java.util.List;

import edu.drexel.se311.calculator.expressions.ExpressionNode;
import edu.drexel.se311.calculator.observers.CalculatorObserver;
import edu.drexel.se311.calculator.visitors.EvaluatorVisitor;

/**
 * CALCULATOR CONTEXT — STATE PATTERN
 *
 * The context that owns:
 *   1. The current state — which determines how input is interpreted
 *   2. The expression engine — builds expression trees as the user types
 *   3. The list of observers — notified when results change
 *
 * The context directly manages state transitions and delegates input
 * handling to the current state. States never perform arithmetic;
 * they only tell the context what happened, and the context updates
 * the tree and transitions states accordingly.
 *
 * Single responsibility: coordinate state behavior, expression building,
 * and observer notifications.
 */
public class CalculatorContext {

    // ── Expression engine (tree building/evaluation) ──────────────────────
    private final ExpressionEngine engine = new ExpressionEngine();

    // ── STATE PATTERN: Current state ──────────────────────────────────────
    private CalculatorState currentState;

    // ── Network observers ─────────────────────────────────────────────────
    private List<CalculatorObserver> observers;

    public CalculatorContext() {
        currentState = new StartState();
        observers = new ArrayList<>();
    }

    // ── Observer wiring ───────────────────────────────────────────────────

    public void addObserver(CalculatorObserver o) {
        observers.add(o);
    }
    private void notifyDisplayUpdate() {
        observers.forEach(o -> o.onDisplayUpdate(getCurrentExpression()));
    }

    private void notifyResultReady(int result) {
        observers.forEach(o -> o.onResultReady(result));
    }
    // ── Tree building / evaluation — called by states ──────────────

    public void appendDigit(int digit) {
        engine.appendDigit(digit);
    }

    public void resetCurrentNumber() {
        engine.resetCurrentNumber();
    }

    public void setCurrentNumber(int value) {
        engine.setCurrentNumber(value);
    }

    public void setPendingOp(char op) {
        engine.setPendingOp(op);
    }

    public char getPendingOp() {
        return engine.getPendingOp();
    }

    public ExpressionNode getLeftOperand() {
        return engine.getLeftOperand();
    }

    public void setLeftOperand(ExpressionNode n) {
        engine.setLeftOperand(n);
    }

    public void storeLeftOperand(char op) {
        engine.storeLeftOperand(op);
    }

    /**
     * Called when the user presses "=". Builds the expression tree (respecting
     * operator precedence) and passes it to EvaluatorVisitor for calculation.
     *
     * CHAINING: After evaluation, the result stays in currentNumber so it
     * can be used as the left operand when the next operator is pressed.
     * Example: "3 + 3 =" gives 6, then "+ 3 =" gives 9 (6 + 3).
     */
    public void submitEquals() {
        // Build expression tree with proper precedence
        ExpressionNode tree = engine.evaluate();
        
        // Evaluate the tree using the visitor pattern
        int result = tree.accept(new EvaluatorVisitor());

        // For chaining support: keep the result in currentNumber so that
        // the next operator will use it as the left operand.
        engine.reset();
        engine.setCurrentNumber(result);

        notifyResultReady(result);
    }
    // ── State management ──────────────────────────────────────────────────

    public void transitionTo(CalculatorState next) {
        currentState = next;
    }

    public CalculatorState getCurrentState() {
        return currentState;
    }

    // ── Button-press entry points (called by the GUI) ─────────────────────

    @Override
    public String toString() {
        return "Current State: " + currentState.getClass().getSimpleName() +
               "\nCurrent Expr: " + engine.currentExpressionString();
    }

    public void onDigit(int digit) {
        currentState.onDigit(this, digit);
        notifyDisplayUpdate();
        System.out.println(this);
    }

    public void onAddSub(char op) {
        currentState.onAddSub(this, op);
        notifyDisplayUpdate();
        System.out.println(this);
    }

    public void onMulDiv(char op) {
        currentState.onMulDiv(this, op);
        notifyDisplayUpdate();
        System.out.println(this);
    }

    public void onEquals() {
        try {
            currentState.onEquals(this);
            notifyDisplayUpdate();
        } catch (ArithmeticException ex) {
            // arithmetic error (eg. division by zero) — let the observer
            // display "Error" and reset the state.
            notifyError(ex.getMessage());
            reset();
        }
        System.out.println(this);
    }

    public void onClear() {
        currentState.onClear(this);
        notifyDisplayUpdate();
        System.out.println(this);
    }

    // ── Accessors ─────────────────────────────────────────────────────────

    public String getCurrentExpression() {
        return engine.currentExpressionString();
    }

    // ── Full reset ────────────────────────────────────────────────────────

    public void reset() {
        engine.reset();
        transitionTo(new StartState());
    }

    // ── Error notification ───────────────────────────────────────────────

    private void notifyError(String message) {
        observers.forEach(o -> o.onError(message));
    }
}