package controller;

import dao.PenggunaDAO;
import model.Pengguna;

public class LoginController {
    private PenggunaDAO penggunaDAO;

    public LoginController() {
        penggunaDAO = new PenggunaDAO();
    }

    public Pengguna prosesLogin(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        Pengguna pengguna = penggunaDAO.login(username);

        if (pengguna == null) {
            return null;
        }

        if (!pengguna.getPassword().equals(password)) {
            return null;
        }

        if (!pengguna.getStatusAktif().equals("Aktif")) {
            return null;
        }

        return pengguna;
    }
}
