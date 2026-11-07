package controller;

import config.Sesi;
import dao.PenggunaDAO;
import model.Pengguna;

public class AuthController extends BaseController {

    private final PenggunaDAO penggunaDAO = new PenggunaDAO();

    // Pesan kesalahan input, atau null bila input lengkap
    public String validasiLogin(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return "Username dan password wajib diisi";
        }
        return null;
    }

    // Cek ke tabel PENGGUNA (akun aktif + password cocok); null bila gagal
    public Pengguna login(String username, String password) {
        return penggunaDAO.login(username, password);
    }

    public void simpanSesi(Pengguna pengguna) {
        Sesi.simpan(
                String.valueOf(pengguna.getIdPengguna()),
                pengguna.getNamaLengkap(),
                pengguna.getPeran()
        );
    }
}
