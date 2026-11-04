package view.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Pemilih baris bergaya tombol radio: satu lingkaran dengan tanda titik ketika
 * terpilih. Dipakai sebagai sel tabel pada layar yang meminta pengguna memilih
 * satu baris, dan juga bisa dipasang langsung sebagai komponen biasa.
 */
public class TombolRadio extends JComponent implements TableCellRenderer {

    private boolean terpilih;
    private Color aksen = Theme.ADMIN;

    public TombolRadio() {
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTerpilih(!terpilih);
            }
        });
    }

    public TombolRadio(String teks, Color aksen) {
        this();
        this.aksen = aksen == null ? Theme.ADMIN : aksen;
    }

    public boolean isTerpilih() {
        return terpilih;
    }

    public void setTerpilih(boolean terpilih) {
        this.terpilih = terpilih;
        repaint();
    }

    public void setAksen(Color aksen) {
        this.aksen = aksen;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(22, 22);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        gambar(g2, getWidth(), getHeight());
        g2.dispose();
    }

    private void gambar(Graphics2D g2, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int d = 18;
        int x = (w - d) / 2;
        int y = (h - d) / 2;
        Ellipse2D lingkaran = new Ellipse2D.Float(x + 0.5f, y + 0.5f, d, d);

        if (terpilih) {
            g2.setColor(aksen);
            g2.fill(lingkaran);
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Float(x + 5f, y + 5f, d - 10f, d - 10f));
        } else {
            g2.setColor(Theme.SURFACE);
            g2.fill(lingkaran);
            g2.setColor(Theme.BORDER);
            g2.setStroke(new BasicStroke(1.4f));
            g2.draw(lingkaran);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable tabel, Object nilai,
            boolean dipilih, boolean fokus, int baris, int kolom) {
        PanelSel sel = new PanelSel();
        sel.setBackground(dipilih ? Theme.SLATE_SOFT : Theme.SURFACE);
        sel.add(this);
        return sel;
    }

    /** Pembungkus sel agar latar baris terpilih tetap terlihat. */
    private static class PanelSel extends javax.swing.JPanel {

        PanelSel() {
            super(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Theme.BORDER_SOFT);
            g.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
        }
    }

    /** Pembungkus bersudut tumpul untuk kotak centang pada tabel petugas. */
    public static class KotakCentang extends JComponent {

        private boolean terpilih;
        private Color aksen = Theme.SUPERVISOR;

        public KotakCentang() {
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public KotakCentang(Color aksen) {
            this();
            this.aksen = aksen == null ? Theme.SUPERVISOR : aksen;
        }

        public boolean isTerpilih() {
            return terpilih;
        }

        public void setTerpilih(boolean terpilih) {
            this.terpilih = terpilih;
            repaint();
        }

        public void setAksen(Color aksen) {
            this.aksen = aksen;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(18, 18);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            RoundRectangle2D kotak = new RoundRectangle2D.Float(
                    0.5f, 0.5f, 17f, 17f, 5, 5);
            if (terpilih) {
                g2.setColor(aksen);
                g2.fill(kotak);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                g2.drawLine(4, 9, 7, 12);
                g2.drawLine(7, 12, 13, 5);
            } else {
                g2.setColor(Theme.SURFACE);
                g2.fill(kotak);
                g2.setColor(Theme.BORDER);
                g2.setStroke(new BasicStroke(1.4f));
                g2.draw(kotak);
            }
            g2.dispose();
        }
    }
}
