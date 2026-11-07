package controller;

import dao.PenggunaDAO;
import model.Pengguna;
import config.Sesi;

public class AdminController {

    private PenggunaDAO penggunaDAO;

    public AdminController() {
        penggunaDAO = new PenggunaDAO();
    }

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

    public void hapusSesi() {
        Sesi.hapus();
    }
}
