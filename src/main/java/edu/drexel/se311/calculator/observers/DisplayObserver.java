package edu.drexel.se311.calculator.observers;

import javax.swing.JTextField;

/**
 * DISPLAY OBSERVER — Observer Pattern
 *
 * Updates the main number screen on the calculator UI.
 *
 * On result  → shows the numeric result
 * On error   → shows "Error"
 *
 * Knows about: its own JTextField only.
 * Knows nothing about: states, visitors, expressions, or the server.
 */
public class DisplayObserver implements CalculatorObserver {

    private final JTextField display;

    public DisplayObserver(JTextField display) {
        this.display = display;
    }

    @Override
    public void onResultReady(double result, String expression) {
        // Show as a clean integer when there is no fractional part
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            display.setText(String.valueOf((long) result));
        } else {
            display.setText(String.valueOf(result));
        }
    }

    @Override
    public void onError(String message) {
        display.setText("Error");
    }
}