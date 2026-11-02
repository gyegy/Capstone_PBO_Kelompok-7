package view;

import controller.LoginController;
import model.Pengguna;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    private LoginController controller;

    public LoginView() {

        controller = new LoginController();

        setTitle("Login AeroCare");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Login");

        panel.add(lblUsername);
        panel.add(txtUsername);

        panel.add(lblPassword);
        panel.add(txtPassword);

        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel);

        btnLogin.addActionListener(e -> prosesLogin());
    }

    private void prosesLogin() {

        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        Pengguna pengguna = controller.prosesLogin(username, password);

        if (pengguna != null) {

            JOptionPane.showMessageDialog(this,
                    "Login berhasil. Selamat datang "
                    + pengguna.getNamaLengkap());

            JOptionPane.showMessageDialog(this,
                    "Hak akses: " + pengguna.getPeran());

        } else {

            JOptionPane.showMessageDialog(this,
                    "Username atau password salah, atau akun tidak aktif.");

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
