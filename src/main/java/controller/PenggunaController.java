package controller;

import dao.PenggunaDAO;
import model.Pengguna;
import java.util.List;

public class PenggunaController {
    private PenggunaDAO penggunaDAO;

    public PenggunaController() {
        penggunaDAO = new PenggunaDAO();
    }

    public List<Pengguna> tampilkanSemuaPengguna() {
        return penggunaDAO.getAllPengguna();
    }
}
