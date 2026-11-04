package view.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Panel konten dengan latar abu-abu terang khas AEROCARE. Semua halaman
 * memakai panel ini sebagai dasar sehingga warna latar seragam.
 */
public class PanelIsi extends JPanel {

    private Color warna = Theme.CANVAS;

    public PanelIsi() {
        super(null);
        setOpaque(false);
    }

    public PanelIsi(int lebar, int tinggi) {
        this();
        setPreferredSize(new java.awt.Dimension(lebar, tinggi));
    }

    public void setWarna(Color warna) {
        this.warna = warna;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(warna);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
