package edu.drexel.se311.calculator.states;

import java.util.ArrayList;
import java.util.List;

import edu.drexel.se311.calculator.expressions.ExpressionNode;
import edu.drexel.se311.calculator.observers.CalculatorObserver;
import edu.drexel.se311.calculator.visitors.EvaluatorVisitor;


public class CalculatorContext {

    private final ExpressionEngine engine = new ExpressionEngine();
    private CalculatorState currentState;
    private List<CalculatorObserver> observers;

    public CalculatorContext() {
        currentState = new StartState();
        observers = new ArrayList<>();
    }

    // Observer operations
    public void addObserver(CalculatorObserver o) {
        observers.add(o);
    }

    private void notifyDisplayUpdate() {
        observers.forEach(o -> o.onDisplayUpdate(getCurrentExpression()));
    }

    private void notifyResultReady(int result) {
        observers.forEach(o -> o.onResultReady(result));
    }

    private void notifyError(String message) {
        observers.forEach(o -> o.onError(message));
    }

    // Expression Operations
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

    public void submitEquals() {
        ExpressionNode tree = engine.evaluate();
        
        int result = tree.accept(new EvaluatorVisitor());

        engine.reset();
        engine.setCurrentNumber(result);

        notifyResultReady(result);
    }

    public String getCurrentExpression() {
        return engine.currentExpressionString();
    }

    public void reset() {
        engine.reset();
        transitionTo(new StartState());
    }

    // State operations
    public void transitionTo(CalculatorState next) {
        currentState = next;
    }

    public CalculatorState getCurrentState() {
        return currentState;
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
}