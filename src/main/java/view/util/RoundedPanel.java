package view.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 * Panel putih bersudut tumpul dengan garis tepi halus dan bayangan tipis,
 * dipakai untuk kartu statistik, kartu tabel, dan panel formulir.
 */
public class RoundedPanel extends JPanel {

    private Color latar = Theme.SURFACE;
    private java.awt.Paint paintLatar;
    private Color garis = Theme.BORDER;
    private int radius = 16;
    private boolean garisTepi = true;
    private boolean bayangan = true;

    public RoundedPanel() {
        setOpaque(false);
        setLayout(null);
    }

    public RoundedPanel(int radius) {
        this();
        this.radius = radius;
    }

    public void setLatar(Color warna) {
        this.latar = warna;
        this.paintLatar = null;
        repaint();
    }

    public void setLatar(java.awt.Paint paint) {
        this.paintLatar = paint;
        repaint();
    }

    public Color getLatar() {
        return latar;
    }

    public void setGaris(Color warna) {
        this.garis = warna;
        repaint();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public void setGarisTepi(boolean tampil) {
        this.garisTepi = tampil;
        repaint();
    }

    public void setBayangan(boolean tampil) {
        this.bayangan = tampil;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int lebar = getWidth();
        int tinggi = getHeight();

        if (bayangan) {
            Color bayanganWarna = new Color(15, 23, 42, 12);
            g2.setColor(bayanganWarna);
            g2.fill(new RoundRectangle2D.Float(0, 2, lebar, tinggi - 2,
                    radius, radius));
        }

        if (paintLatar != null) {
            g2.setPaint(paintLatar);
        } else {
            g2.setColor(latar);
        }
        g2.fill(new RoundRectangle2D.Float(0, 0, lebar - 0.5f, tinggi - 0.5f,
                radius, radius));

        if (garisTepi && garis != null) {
            g2.setColor(garis);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, lebar - 1.5f,
                    tinggi - 1.5f, radius, radius));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
