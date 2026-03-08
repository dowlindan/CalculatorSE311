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

public class CalculatorClient extends JFrame {

    private final JTextField display;

    // ── Colour palette ────────────────────────────────────────────────────
    private static final Color BG           = new Color(0xCCDDEE);
    private static final Color BTN_FACE     = new Color(0x6699CC);
    private static final Color BTN_HOVER    = new Color(0x7AAAD8);
    private static final Color BTN_PRESS    = new Color(0x4477AA);
    private static final Color BTN_TEXT     = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0x4477AA);

    // ── Button layout ─────────────────────────────────────────────────────
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

        // ── Root panel ───────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        // ── Display ──────────────────────────────────────────────────────
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

        // ── Button grid ──────────────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(4, 4, 6, 6));
        grid.setBackground(BG);

        for (String[] row : BUTTON_LABELS) {
            for (String label : row) {
                grid.add(makeButton(label));
            }
        }

        root.add(grid, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(280, 320));
    }

    // ── Styled button — no ActionListener attached ────────────────────────
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

                // subtle inner highlight
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

        // No ActionListener — buttons are intentionally dead.
        // Logic will be wired in later via State + Observer patterns.

        return btn;
    }

    // ── Entry point ───────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new CalculatorClient().setVisible(true);
        });
    }
}