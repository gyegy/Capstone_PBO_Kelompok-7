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
import javax.swing.JComponent;

/**
 * Tombol pilihan berbentuk pil yang dipakai untuk memilih prioritas laporan
 * atau status progres. Hanya satu chip dalam satu kelompok yang aktif; kelas
 * ini juga menyediakan pengelompokan tersebut lewat {@link #hubungkan}.
 */
public class ChipPilihan extends JComponent {

    private String teks = "";
    private boolean aktif;
    private boolean diAtas;
    private Color aksen = Theme.ADMIN;
    private int tinggi = 36;
    private int paddingX = 18;
    private final java.util.List<Runnable> pendengar = new java.util.ArrayList<>();

    public ChipPilihan() {
        setOpaque(false);
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

            @Override
            public void mouseClicked(MouseEvent e) {
                for (Runnable aksi : pendengar) {
                    aksi.run();
                }
            }
        });
    }

    public ChipPilihan(String teks, Color aksen) {
        this();
        this.teks = teks == null ? "" : teks;
        this.aksen = aksen;
    }

    public void setTeks(String teks) {
        this.teks = teks == null ? "" : teks;
        revalidate();
        repaint();
    }

    public String getTeks() {
        return teks;
    }

    public void setAksen(Color aksen) {
        this.aksen = aksen;
        repaint();
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
        repaint();
    }

    public void setTinggiChip(int tinggi) {
        this.tinggi = tinggi;
        revalidate();
        repaint();
    }

    /** Menambahkan aksi yang dijalankan ketika chip diklik. */
    public void tambahAksi(Runnable aksi) {
        pendengar.add(aksi);
    }

    /**
     * Menjadikan chip-chip ini saling eksklusif: mengklik satu chip akan
     * menonaktifkan sisanya.
     */
    public static void hubungkan(ChipPilihan... kelompok) {
        for (ChipPilihan chip : kelompok) {
            chip.tambahAksi(() -> {
                for (ChipPilihan lain : kelompok) {
                    lain.setAktif(lain == chip);
                }
            });
        }
    }

    /** Mengembalikan chip pertama dalam kelompok yang sedang aktif. */
    public static String nilaiAktif(ChipPilihan... kelompok) {
        for (ChipPilihan chip : kelompok) {
            if (chip.isAktif()) {
                return chip.getTeks();
            }
        }
        return "";
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(Theme.font(1, 12.5f));
        return new Dimension(fm.stringWidth(teks) + paddingX * 2, tinggi);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = Math.min(getHeight(), tinggi);
        RoundRectangle2D bentuk = new RoundRectangle2D.Float(
                0.5f, 0.5f, w - 1f, h - 1f, 9, 9);

        Color isi;
        Color garis;
        Color warnaTeks;
        if (aktif) {
            isi = campur(aksen, Color.WHITE, 0.90f);
            garis = aksen;
            warnaTeks = aksen;
        } else if (diAtas) {
            isi = Theme.SLATE_SOFT;
            garis = Theme.BORDER;
            warnaTeks = Theme.TEXT;
        } else {
            isi = Theme.SURFACE;
            garis = Theme.BORDER;
            warnaTeks = Theme.TEXT;
        }

        g2.setColor(isi);
        g2.fill(bentuk);
        g2.setColor(garis);
        g2.setStroke(new BasicStroke(aktif ? 1.4f : 1f));
        g2.draw(bentuk);

        java.awt.Font font = Theme.font(1, 12.5f);
        FontMetrics fm = g2.getFontMetrics(font);
        int teksW = fm.stringWidth(teks);
        // Kecilkan huruf bila teks tidak muat agar tidak terpotong.
        while (teksW > w - 16 && font.getSize2D() > 9f) {
            font = font.deriveFont(font.getSize2D() - 0.5f);
            fm = g2.getFontMetrics(font);
            teksW = fm.stringWidth(teks);
        }
        g2.setFont(font);
        g2.setColor(warnaTeks);
        g2.drawString(teks, (w - teksW) / 2,
                (h - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
    }

    /** Mencampur dua warna; rasio 1 berarti warna kedua. */
    static Color campur(Color dasar, Color tambahan, float rasio) {
        float r = 1f - rasio;
        return new Color(
                Math.round(dasar.getRed() * r + tambahan.getRed() * rasio),
                Math.round(dasar.getGreen() * r + tambahan.getGreen() * rasio),
                Math.round(dasar.getBlue() * r + tambahan.getBlue() * rasio));
    }
}
