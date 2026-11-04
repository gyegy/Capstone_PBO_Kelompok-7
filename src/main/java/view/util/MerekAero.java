package view.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/**
 * Mark merek AEROCARE: kotak biru bersudut tumpul dengan huruf "A" putih,
 * seperti pada sidebar dan halaman masuk.
 */
public class MerekAero extends JComponent {

    private Color aksen = Theme.ADMIN;
    private int radius = 12;

    public MerekAero() {
        setOpaque(false);
    }

    /** Mengganti warna kotak mengikuti aksen peran. */
    public void setAksen(Color aksen) {
        this.aksen = aksen == null ? Theme.ADMIN : aksen;
        repaint();
    }

    public Color getAksen() {
        return aksen;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int sisi = Math.min(getWidth(), getHeight());
        int x = (getWidth() - sisi) / 2;
        int y = (getHeight() - sisi) / 2;
        g2.setColor(aksen);
        g2.fill(new RoundRectangle2D.Float(x, y, sisi, sisi, radius, radius));

        String huruf = "A";
        g2.setFont(Theme.bold(sisi * 0.56f));
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int tx = x + (sisi - fm.stringWidth(huruf)) / 2;
        int ty = y + (sisi - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(huruf, tx, ty);
        g2.dispose();
    }
}
