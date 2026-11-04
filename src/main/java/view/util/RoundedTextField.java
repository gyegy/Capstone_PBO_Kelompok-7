package view.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Kolom teks bersudut tumpul dengan placeholder abu-abu dan sorot aksen
 * ketika sedang diisi, mengikuti gaya input pada desain Figma.
 */
public class RoundedTextField extends JTextField {

    private String placeholder = "";
    private Color garis = Theme.BORDER;
    private Color garisFokus = Theme.ADMIN;
    private int radius = 9;

    public RoundedTextField() {
        setOpaque(false);
        setBorder(new EmptyBorder(7, 13, 7, 13));
        setFont(Theme.regular(13f));
        setForeground(Theme.TEXT);
        setCaretColor(Theme.TEXT);
        setSelectionColor(Theme.ADMIN_SOFT);
        setSelectedTextColor(Theme.TEXT);
    }

    public void setPlaceholder(String teks) {
        this.placeholder = teks == null ? "" : teks;
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public void setWarnaGaris(Color warna) {
        this.garis = warna;
        repaint();
    }

    public void setWarnaGarisFokus(Color warna) {
        this.garisFokus = warna;
        repaint();
    }

    public void setTinggi(int tinggi) {
        setPreferredSize(new java.awt.Dimension(getPreferredSize().width, tinggi));
        setMinimumSize(new java.awt.Dimension(10, tinggi));
        revalidate();
    }

    @Override
    public Insets getInsets() {
        return new Insets(7, 13, 7, 13);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(Theme.SURFACE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, radius, radius));

        g2.setColor(hasFocus() ? garisFokus : garis);
        g2.setStroke(new BasicStroke(hasFocus() ? 1.6f : 1f));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f,
                radius, radius));

        g2.dispose();

        super.paintComponent(g);

        if (getText().isEmpty() && !placeholder.isEmpty() && !hasFocus()) {
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g3.setColor(Theme.TEXT_FAINT);
            g3.setFont(getFont());
            Insets ins = getInsets();
            int tinggiHuruf = g3.getFontMetrics().getAscent();
            int y = (h + tinggiHuruf) / 2 - 2;
            g3.drawString(placeholder, ins.left, y);
            g3.dispose();
        }
    }
}
