
package com.mycompany.aerocare;

import dao.PenggunaDAO;

public class AeroCare {

    public static void main(String[] args) {

        PenggunaDAO dao = new PenggunaDAO();

        // Ganti dengan ID pengguna yang ingin dinonaktifkan
        int idPengguna = 22;

        boolean berhasil = dao.hapusPengguna(idPengguna);

        if (berhasil) {
            System.out.println("Pengguna berhasil dinonaktifkan!");
        } else {
            System.out.println("Gagal menonaktifkan pengguna.");
        }
    }
}
 