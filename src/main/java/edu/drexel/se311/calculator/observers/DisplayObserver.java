package edu.drexel.se311.calculator.observers;

import javax.swing.JTextField;


public class DisplayObserver implements CalculatorObserver {

    private final JTextField display;

    public DisplayObserver(JTextField display) {
        this.display = display;
    }

    @Override
    public void onDisplayUpdate(String text) {
        display.setText(text);
    }
    @Override
    public void onResultReady(int result) {
        display.setText(String.valueOf(result));
    }

    @Override
    public void onError(String message) {
        display.setText("Error");
    }
}