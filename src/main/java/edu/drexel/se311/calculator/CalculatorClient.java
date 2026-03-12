package edu.drexel.se311.calculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import edu.drexel.se311.calculator.observers.DisplayObserver;
import edu.drexel.se311.calculator.observers.ServerConnection;
import edu.drexel.se311.calculator.states.CalculatorContext;


public class CalculatorClient extends JFrame {

    private final JTextField display;

    private static final Color BG           = new Color(0xCCDDEE);
    private static final Color BTN_FACE     = new Color(0x6699CC);
    private static final Color BTN_HOVER    = new Color(0x7AAAD8);
    private static final Color BTN_PRESS    = new Color(0x4477AA);
    private static final Color BTN_TEXT     = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0x4477AA);

    private static final String[][] BUTTON_LABELS = {
        { "1", "2", "3", "+" },
        { "4", "5", "6", "-" },
        { "7", "8", "9", "*" },
        { "0", "=", "C", "/" }
    };

    public CalculatorClient() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        display = new JTextField("0");
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setForeground(new Color(0x223344));
        display.setFont(new Font("Monospaced", Font.PLAIN, 24));
        display.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        display.setPreferredSize(new Dimension(0, 52));
        root.add(display, BorderLayout.NORTH);

        ServerConnection connection = new ServerConnection(
            CalculatorServer.HOST, CalculatorServer.PORT
        );

        CalculatorContext context = new CalculatorContext();
        context.addObserver(new DisplayObserver(display));
        context.addObserver(connection);

        JPanel grid = new JPanel(new GridLayout(4, 4, 6, 6));
        grid.setBackground(BG);

        for (String[] row : BUTTON_LABELS) {
            for (String label : row) {
                JButton btn = makeButton(label);
                btn.addActionListener(e -> routeInput(context, label));
                grid.add(btn);
            }
        }

        root.add(grid, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(280, 320));
    }

    private void routeInput(CalculatorContext context, String label) {
        System.out.println("Label: " + label);
        switch (label) {
            case "0","1","2","3","4","5","6","7","8","9"
                             -> context.onDigit(Integer.parseInt(label));
            case "+", "-"   -> context.onAddSub(label.charAt(0));
            case "*", "/"   -> context.onMulDiv(label.charAt(0));
            case "="        -> context.onEquals();
            case "C"        -> {
                context.onClear();
                display.setText("0");
            }
        }
    }

    private JButton makeButton(String label) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                Color face = BTN_FACE;
                ButtonModel m = getModel();
                if      (m.isPressed())  face = BTN_PRESS;
                else if (m.isRollover()) face = BTN_HOVER;

                g2.setColor(face);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() / 2, 6, 6);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setForeground(BTN_TEXT);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(60, 52));

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new CalculatorClient().setVisible(true);
        });
    }
}