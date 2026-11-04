package view.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Lencana status berwarna (Tinggi, Menunggu, Selesai, dan seterusnya)
 * dengan bentuk pil dan latar lembut seperti pada desain.
 */
public class BadgeLabel extends JComponent {

    private String teks = "";
    private Color latar = Theme.SLATE_SOFT;
    private Color teksWarna = Theme.TEXT_MUTED;
    private int paddingX = 11;
    private int tinggi = 22;

    public BadgeLabel() {
        setOpaque(false);
    }

    public BadgeLabel(String teks) {
        this();
        setTeks(teks);
    }

    public void setTeks(String teks) {
        this.teks = teks == null ? "" : teks;
        setLatar(Theme.latarStatus(teks));
        setTeksWarna(Theme.teksStatus(teks));
        revalidate();
        repaint();
    }

    public void setLatar(Color warna) {
        this.latar = warna;
        revalidate();
        repaint();
    }

    public void setTeksWarna(Color warna) {
        this.teksWarna = warna;
        revalidate();
        repaint();
    }

    public void setTinggiBadge(int tinggi) {
        this.tinggi = tinggi;
        revalidate();
        repaint();
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(Theme.bold(11.5f));
        int w = fm.stringWidth(teks) + paddingX * 2;
        return new java.awt.Dimension(w, tinggi);
    }

    @Override
    public java.awt.Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public java.awt.Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public void setFont(java.awt.Font font) {
        super.setFont(font);
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int h = Math.min(getHeight(), tinggi);
        java.awt.Dimension pref = getPreferredSize();
        int w = Math.min(getWidth(), pref.width);

        g2.setColor(latar);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, h, h));

        g2.setFont(Theme.bold(11.5f));
        g2.setColor(teksWarna);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(teks, paddingX,
                (h - fm.getHeight()) / 2 + fm.getAscent());

        g2.dispose();
    }

    /** Avatar bulat berisi inisial dan warna aksen peran. */
    public static class Avatar extends JComponent {

        private String inisial = "H";
        private Color warna = Theme.ADMIN;
        private int ukuran = 38;

        public Avatar() {
            setOpaque(false);
        }

        public Avatar(String nama, Color warna, int ukuran) {
            this();
            setInisial(UiKit.inisial(nama));
            this.warna = warna;
            this.ukuran = ukuran;
        }

        public void setInisial(String inisial) {
            this.inisial = inisial == null ? "?" : inisial;
            repaint();
        }

        public void setWarna(Color warna) {
            this.warna = warna;
            repaint();
        }

        @Override
        public java.awt.Dimension getPreferredSize() {
            return new java.awt.Dimension(ukuran, ukuran);
        }

        @Override
        public java.awt.Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public java.awt.Dimension getMaximumSize() {
            return getPreferredSize();
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

            g2.setColor(warna);
            g2.fill(new Ellipse2D.Float(x, y, sisi, sisi));

            g2.setFont(Theme.bold(sisi * 0.42f));
            g2.setColor(Theme.SURFACE);
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(inisial);
            g2.drawString(inisial, x + (sisi - tw) / 2f,
                    y + (sisi - fm.getHeight()) / 2f + fm.getAscent());
            g2.dispose();
        }
    }

    /** Tombol teks polos untuk aksi di dalam tabel (Ubah, Hapus, Detail...). */
    public static class TautanAksi extends JLabel {

        private Color warna;
        private boolean diAtas;

        public TautanAksi(String teks, Color warna) {
            super(teks);
            this.warna = warna;
            setOpaque(false);
            setFont(Theme.regular(12.5f));
            setForeground(warna);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
            });
        }

        public void setWarna(Color warna) {
            this.warna = warna;
            setForeground(warna);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(getFont());
            g2.setColor(diAtas ? warna.darker() : warna);
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), 0, y);
            if (diAtas) {
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(0, y + 2, fm.stringWidth(getText()), y + 2);
            }
            g2.dispose();
        }
    }

    /** Membuat panel transparan tanpa layout untuk penempatan bebas. */
    public static javax.swing.JPanel panelBening() {
        javax.swing.JPanel p = new javax.swing.JPanel(null);
        p.setOpaque(false);
        return p;
    }

    /** Mengatur ukuran satu komponen sekaligus. */
    public static void atur(Component c, int x, int y, int w, int h) {
        c.setBounds(x, y, w, h);
    }
}
