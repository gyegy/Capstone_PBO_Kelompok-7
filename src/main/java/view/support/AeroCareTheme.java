package view.support;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Shared palette, typography and reusable Swing styling helpers. */
public final class AeroCareTheme {
    public static final Color NAVY = new Color(16, 25, 48);
    public static final Color PAGE = new Color(241, 245, 250);
    public static final Color BLUE = new Color(37, 99, 235);
    public static final Color PURPLE = new Color(124, 58, 237);
    public static final Color ORANGE = new Color(234, 88, 12);
    public static final Color INK = new Color(28, 39, 58);
    public static final Color MUTED = new Color(111, 125, 147);
    public static final Color LINE = new Color(226, 232, 240);
    private AeroCareTheme() {}
    public static void styleButton(JButton b, Color color) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 12)); b.setForeground(Color.WHITE);
        b.setBackground(color); b.setFocusPainted(false); b.setBorderPainted(false);
        b.setOpaque(true); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10,16,10,16));
    }
    public static JLabel heading(String text) {
        JLabel l=new JLabel(text); l.setFont(new Font("Segoe UI",Font.BOLD,22)); l.setForeground(INK); return l;
    }
    public static JLabel muted(String text) {
        JLabel l=new JLabel(text); l.setFont(new Font("Segoe UI",Font.PLAIN,12)); l.setForeground(MUTED); return l;
    }
}
