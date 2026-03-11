package edu.drexel.se311.calculator.observers;

/**
 * OBSERVER INTERFACE — Observer Pattern
 *
 * The contract every UI component must implement to receive
 * notifications from the server.
 *
 * The server knows only about this interface — it never imports
 * any Swing or client code directly.
 *
 * Two events:
 *   onResultReady  — a valid result was computed
 *   onError        — something went wrong (e.g. division by zero)
 */
public interface CalculatorObserver {
    void onDisplayUpdate(String text);
    void onResultReady(int result);
    void onError(String message);
}