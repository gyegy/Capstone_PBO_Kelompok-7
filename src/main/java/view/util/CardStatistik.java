package view.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * Kartu statistik pada dashboard: judul kecil, angka besar berwarna, dan
 * keterangan pendukung di bawahnya. Digambar sendiri agar tipografinya
 * konsisten dengan desain.
 */
public class CardStatistik extends RoundedPanel {

    private String judul = "Total Fasilitas";
    private String nilai = "0";
    private String keterangan = "";
    private Color warnaNilai = Theme.TEXT;

    public CardStatistik() {
        super(14);
        setLatar(Theme.SURFACE);
        setGaris(Theme.BORDER);
        setLayout(null);
    }

    public void setJudul(String judul) {
        this.judul = judul;
        repaint();
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
        repaint();
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
        repaint();
    }

    public void setWarnaNilai(Color warna) {
        this.warnaNilai = warna;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setFont(Theme.regular(12.5f));
        g2.setColor(Theme.TEXT_MUTED);
        g2.drawString(judul, 18, 29);

        g2.setFont(Theme.bold(30f));
        g2.setColor(warnaNilai);
        g2.drawString(nilai, 18, 72);

        if (keterangan != null && !keterangan.isEmpty()) {
            g2.setFont(Theme.regular(11.5f));
            g2.setColor(Theme.TEXT_FAINT);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(keterangan, 18, getHeight() - 16 + fm.getAscent() / 2);
        }
        g2.dispose();
    }
}
