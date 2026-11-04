package view;

import controller.LoginController;
import model.Pengguna;
import java.awt.*;
import javax.swing.*;

public class LoginView extends javax.swing.JFrame {
    private javax.swing.JPanel canvas;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JButton btnMasuk;

    public LoginView() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("AEROCARE - Masuk");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        canvas = new javax.swing.JPanel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnMasuk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(920, 560));
        canvas.setBackground(new Color(16, 25, 48));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE));

        canvas.setLayout(new GridBagLayout());
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 36, 30, 36));
        card.setPreferredSize(new Dimension(360, 390));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("✦  AEROCARE");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        logo.setForeground(new Color(37, 99, 235));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Masuk ke AEROCARE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 19));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel("Sistem Manajemen Pemeliharaan Fasilitas");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(new Color(111, 125, 147));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername.setMaximumSize(new Dimension(290, 38));
        txtPassword.setMaximumSize(new Dimension(290, 38));
        btnMasuk.setText("Masuk");
        btnMasuk.setBackground(new Color(37, 99, 235));
        btnMasuk.setForeground(Color.WHITE);
        btnMasuk.setFocusPainted(false);
        btnMasuk.setMaximumSize(new Dimension(290, 40));
        btnMasuk.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(20));
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));
        card.add(new JLabel("Username"));
        card.add(Box.createVerticalStrut(6));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(14));
        card.add(new JLabel("Password"));
        card.add(Box.createVerticalStrut(6));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(22));
        card.add(btnMasuk);
        canvas.add(card);

        btnMasuk.addActionListener(e -> prosesMasuk());
        txtPassword.addActionListener(e -> prosesMasuk());
        pack();
    }
    // </editor-fold>

    private void prosesMasuk() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password wajib diisi.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pengguna pengguna = new LoginController().prosesLogin(username, password);
        if (pengguna == null) {
            JOptionPane.showMessageDialog(this,
                "Login gagal. Periksa username, password, atau status pengguna.",
                "Gagal masuk", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame next;
        switch (pengguna.getPeran()) {
            case "Admin" -> next = new DashboardAdmin();
            case "Supervisor" -> next = new DashboardSupervisor();
            case "Petugas" -> next = new DashboardPetugas();
            default -> {
                JOptionPane.showMessageDialog(this, "Peran pengguna tidak dikenali.",
                    "Gagal masuk", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        next.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
