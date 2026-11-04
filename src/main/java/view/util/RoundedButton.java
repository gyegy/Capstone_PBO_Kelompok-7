package view.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/**
 * Tombol bersudut tumpul dengan tiga gaya sesuai Figma: primer (warna aksen
 * penuh), sekunder (putih bergaris), dan gelap. Teks dan ikon digambar
 * sendiri agar tampilan sama di semua Look and Feel.
 */
public class RoundedButton extends JButton {

    public enum Gaya {
        PRIMER, SEKUNDER, GELAP, BAHaya, MERAH
    }

    private Color latar;
    private Color latarHover;
    private Color latarTekan;
    private Color warnaTeks;
    private Color warnaGaris;
    private int radius = 10;
    private boolean diAtas = false;
    private boolean ditekan = false;
    private boolean bayangan = false;

    public RoundedButton() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(Theme.font(1, 13f));
        setForeground(Theme.SURFACE);
        terapkan(Theme.ADMIN, Theme.ADMIN_DARK, Theme.SURFACE, null);
        pasangListener();
    }

    public RoundedButton(String teks, Gaya gaya, Color aksen) {
        this();
        setText(teks);
        pakaiGaya(gaya, aksen);
    }

    public void setTinggi(int tinggi) {
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension(d.width, tinggi));
        setMinimumSize(new Dimension(d.width, tinggi));
        revalidate();
        repaint();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public void setBayanganTombol(boolean tampil) {
        this.bayangan = tampil;
        repaint();
    }

    public final void pakaiGaya(Gaya gaya, Color aksen) {
        switch (gaya) {
            case PRIMER:
                terapkan(aksen, aksen.darker(), Theme.SURFACE, null);
                break;
            case SEKUNDER:
                terapkan(Theme.SURFACE, Theme.SLATE_SOFT, Theme.TEXT, Theme.BORDER);
                break;
            case GELAP:
                terapkan(Theme.SIDEBAR, Theme.SIDEBAR_HOVER, Theme.SURFACE, null);
                break;
            case BAHaya:
                terapkan(Theme.SURFACE, Theme.SLATE_SOFT, aksen, Theme.BORDER);
                break;
            case MERAH:
                terapkan(Theme.RED, new Color(0x991B1B), Theme.SURFACE, null);
                break;
            default:
                terapkan(aksen, aksen.darker(), Theme.SURFACE, null);
                break;
        }
    }

    private void terapkan(Color latarDasar, Color latarGelap, Color teks,
            Color garis) {
        this.latar = latarDasar;
        this.latarHover = latarGelap;
        this.latarTekan = latarGelap.darker();
        this.warnaTeks = teks;
        this.warnaGaris = garis;
        setForeground(teks);
        repaint();
    }

    public void setWarnaTeks(Color warna) {
        this.warnaTeks = warna;
        repaint();
    }

    public void setWarnaLatar(Color warna) {
        this.latar = warna;
        this.latarHover = warna.darker();
        this.latarTekan = warna.darker().darker();
        repaint();
    }

    private void pasangListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                diAtas = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                diAtas = false;
                ditekan = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ditekan = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ditekan = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (!isEnabled()) {
            g2.setColor(new Color(latar.getRed(), latar.getGreen(),
                    latar.getBlue(), 110));
        } else if (ditekan) {
            g2.setColor(latarTekan);
        } else if (diAtas) {
            g2.setColor(latarHover);
        } else {
            g2.setColor(latar);
        }

        if (bayangan) {
            g2.setColor(new Color(37, 99, 235, 40));
            g2.fill(new RoundRectangle2D.Float(0, 2, w, h - 2, radius, radius));
        }

        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, radius, radius));

        if (warnaGaris != null) {
            g2.setColor(warnaGaris);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f,
                    radius, radius));
        }

        if (getText() != null && !getText().isEmpty()) {
            g2.setColor(isEnabled() ? warnaTeks : Theme.TEXT_FAINT);
            java.awt.Font font = getFont();
            FontMetrics fm = g2.getFontMetrics(font);
            int teksW = fm.stringWidth(getText());
            // Bila teks lebih lebar dari tombol, kecilkan hurufnya bertahap
            // supaya tidak terpotong di tepi.
            while (teksW > w - 20 && font.getSize2D() > 9f) {
                font = font.deriveFont(font.getSize2D() - 0.5f);
                fm = g2.getFontMetrics(font);
                teksW = fm.stringWidth(getText());
            }
            g2.setFont(font);
            int x = (w - teksW) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (getText() != null && !getText().isEmpty()) {
            FontMetrics fm = getFontMetrics(getFont());
            d.width = fm.stringWidth(getText()) + 36;
        }
        if (d.height < 32) {
            d.height = 32;
        }
        return d;
    }
}
