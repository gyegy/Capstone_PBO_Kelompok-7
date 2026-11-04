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
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/**
 * Satu baris menu pada sidebar. Titik kecil di kiri berubah menjadi sorotan
 * ketika menu aktif, sama seperti komponen menu pada desain Figma.
 */
public class MenuItem extends JComponent {

    private String teks;
    private boolean aktif;
    private boolean diAtas;
    private Color aksen = Theme.ADMIN;
    private Runnable aksi;
    private boolean tampilTitik = true;

    public MenuItem(String teks) {
        this.teks = teks;
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(240, 38));
        setMinimumSize(new Dimension(120, 38));
        setMaximumSize(new Dimension(4000, 38));
        setFont(Theme.regular(13f));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                diAtas = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                diAtas = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (aksi != null && isEnabled()) {
                    aksi.run();
                }
            }
        });
    }

    public void setAksen(Color warna) {
        this.aksen = warna;
        repaint();
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
        repaint();
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAksi(Runnable aksi) {
        this.aksi = aksi;
    }

    public String getTeks() {
        return teks;
    }

    public void setText(String teks) {
        this.teks = teks;
        repaint();
    }

    public void setTampilTitik(boolean tampil) {
        this.tampilTitik = tampil;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (aktif) {
            g2.setColor(aksen);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 9, 9));
        } else if (diAtas) {
            g2.setColor(Theme.SIDEBAR_HOVER);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 9, 9));
        }

        int x = 14;

        if (tampilTitik) {
            int d = 7;
            int cy = h / 2 - d / 2;
            g2.setColor(aktif ? Theme.SURFACE
                    : new Color(0x64748B));
            g2.fill(new Ellipse2D.Float(x, cy, d, d));
            x += d + 11;
        } else {
            x = 13;
        }

        g2.setFont(aktif ? Theme.bold(13f) : Theme.regular(13f));
        g2.setColor(aktif ? Theme.SURFACE : Theme.TEXT_ON_DARK);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(teks, x, (h - fm.getHeight()) / 2 + fm.getAscent());

        g2.dispose();
    }

    /** Item menu "Logout" dengan ikon titik merah. */
    public static class Logout extends JComponent {

        private boolean diAtas;
        private Runnable aksi;

        public Logout() {
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(240, 34));
            setMaximumSize(new Dimension(4000, 34));
            setFont(Theme.regular(13f));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    diAtas = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    diAtas = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (aksi != null) {
                        aksi.run();
                    }
                }
            });
        }

        public void setAksi(Runnable aksi) {
            this.aksi = aksi;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (diAtas) {
                g2.setColor(Theme.SIDEBAR_HOVER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                        getHeight(), 9, 9));
            }

            g2.setColor(new Color(0xF87171));
            g2.fill(new Ellipse2D.Float(14, getHeight() / 2f - 3.5f, 7, 7));

            g2.setFont(getFont());
            g2.setColor(diAtas ? Theme.SURFACE : Theme.TEXT_ON_DARK);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString("Logout", 32,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    /** Lencana kecil "Peran: Admin" di bawah logo. */
    public static class LencanaPeran extends JComponent {

        private String peran = "Admin";

        public LencanaPeran() {
            setOpaque(false);
            setPreferredSize(new Dimension(110, 24));
            setMaximumSize(new Dimension(200, 24));
        }

        public void setPeran(String peran) {
            this.peran = peran;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String teks = "Peran: " + peran;
            g2.setFont(Theme.regular(11.5f));
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(teks) + 22;
            int h = getHeight() == 0 ? 24 : getHeight();

            g2.setColor(new Color(255, 255, 255, 22));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, h, h));
            g2.setColor(new Color(0xCBD5E1));
            g2.drawString(teks, 11, (h - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }
}
