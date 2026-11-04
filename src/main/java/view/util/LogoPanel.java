package view.util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Menampilkan logo AeroCare. Gambar asli diambil dari
 * src/main/resources/images/logo.png.
 */
public class LogoPanel extends JComponent {

    private static BufferedImage cache;

    private int ukuran = 44;

    public LogoPanel() {
        setOpaque(false);
    }

    public void setUkuran(int ukuran) {
        this.ukuran = ukuran;
        setPreferredSize(new Dimension(ukuran, ukuran));
        setMinimumSize(new Dimension(ukuran, ukuran));
        revalidate();
        repaint();
    }

    private static BufferedImage muat() {
        if (cache != null) {
            return cache;
        }
        try (InputStream masuk = LogoPanel.class.getResourceAsStream(
                "/images/logo.png")) {
            if (masuk != null) {
                cache = ImageIO.read(masuk);
            }
        } catch (IOException e) {
            System.out.println("Logo tidak dapat dimuat: " + e.getMessage());
        }
        return cache;
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage gambar = muat();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int sisi = Math.min(getWidth(), getHeight());
        int x = (getWidth() - sisi) / 2;
        int y = (getHeight() - sisi) / 2;

        if (gambar == null) {
            // Cadangan bila gambar tidak ditemukan: kotak bergaris dengan huruf A
            g2.setColor(Theme.SURFACE);
            g2.fillRoundRect(x, y, sisi, sisi, 10, 10);
            g2.setColor(Theme.ADMIN);
            g2.setFont(Theme.bold(sisi * 0.5f));
            g2.drawString("A", x + sisi / 3f, y + sisi * 0.7f);
        } else {
            Image skala = gambar.getScaledInstance(sisi, sisi,
                    Image.SCALE_SMOOTH);
            g2.drawImage(skala, x, y, null);
        }
        g2.dispose();
    }
}
