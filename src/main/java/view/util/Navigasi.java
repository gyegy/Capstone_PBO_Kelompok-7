package view.util;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Mengatur perpindahan antar layar AEROCARE. Setiap layar utama adalah JFrame
 * terpisah; kelas ini yang membuka, menutup, dan memusatkannya.
 */
public final class Navigasi {

    private Navigasi() {
    }

    public static void buka(JFrame form) {
        form.setLocationRelativeTo(null);
        form.setVisible(true);
    }

    public static void tutupDanBuka(final JFrame lama, final JFrame baru) {
        SwingUtilities.invokeLater(() -> {
            if (lama != null) {
                lama.dispose();
            }
            baru.setLocationRelativeTo(null);
            baru.setVisible(true);
        });
    }
}
