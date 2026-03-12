package edu.drexel.se311.calculator.observers;


public interface CalculatorObserver {
    void onDisplayUpdate(String text);
    void onResultReady(int result);
    void onError(String message);
}