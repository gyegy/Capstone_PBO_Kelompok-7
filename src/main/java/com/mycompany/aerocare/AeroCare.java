package com.mycompany.aerocare;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.LoginView;

public class AeroCare {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(AeroCare.class.getName())
                    .log(java.util.logging.Level.WARNING, "Gagal menerapkan look and feel sistem", ex);
            }
            new LoginView().setVisible(true);
        });
    }
}
