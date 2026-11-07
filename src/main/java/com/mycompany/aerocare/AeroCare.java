package com.mycompany.aerocare;

import config.Koneksi;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import view.LoginFrame;

public class AeroCare {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            if (!databaseSiap()) {
                JOptionPane.showMessageDialog(null,
                        "Tidak dapat terhubung ke database 'aerocare'.\n"
                        + "Pastikan MySQL sudah berjalan dan pengaturan\n"
                        + "pada config/Koneksi.java sudah benar.",
                        "AeroCare - Koneksi Database",
                        JOptionPane.WARNING_MESSAGE);
            }

            new LoginFrame().setVisible(true);
        });
    }

    private static boolean databaseSiap() {
        try (Connection conn = Koneksi.getConnection()) {
            return conn != null;
        } catch (Exception e) {
            return false;
        }
    }
}
