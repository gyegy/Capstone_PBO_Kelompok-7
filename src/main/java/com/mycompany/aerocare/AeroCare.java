package com.mycompany.aerocare;

import admin.Dashboard;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AeroCare extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Dashboard dashboard;

    public AeroCare() {
        initComponents();
    }

    private void initComponents() {
        setTitle("AeroCare - Sistem Penerbangan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Halaman-halaman aplikasi
        dashboard = new Dashboard();
        cardPanel.add(dashboard, "dashboard");
        // Nanti tambahkan: cardPanel.add(loginPanel, "login"); dst.

        add(cardPanel);
    }

    // Pindah halaman: ganti "dashboard" dengan nama kartu lain
    public void showCard(String namaKartu) {
        cardLayout.show(cardPanel, namaKartu);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new AeroCare().setVisible(true);
        });
    }
}
