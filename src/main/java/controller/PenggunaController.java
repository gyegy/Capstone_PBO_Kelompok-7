package controller;

import dao.PenggunaDAO;
import java.util.List;
import model.Admin;
import model.Pengguna;
import model.Petugas;
import model.Supervisor;

/**
 * Controller Kelola Data Pengguna (Admin): lihat, cari, tambah, ubah,
 * aktifkan / nonaktifkan. Dipakai PenggunaFrame dan PenggunaDialog.
 */
public class PenggunaController extends BaseController {

    private final PenggunaDAO penggunaDAO = new PenggunaDAO();

    // READ: semua pengguna atau hasil pencarian
    public List<Pengguna> getDaftar(String kataKunci) {
        if (kataKunci == null || kataKunci.trim().isEmpty()) {
            return penggunaDAO.getAllPengguna();
        }
        return penggunaDAO.searchPengguna(kataKunci.trim());
    }

    private static final String BATAS = "Data tidak lengkap atau melebihi batas: ";

    private String kosongJadiNull(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    // data1/data2 = data khusus peran:
    //   Admin -> hak akses | Supervisor -> area pengawasan
    //   Petugas -> spesialisasi, shift kerja
    private String validasiDataKhusus(String peran, String data1) {
        if ("Admin".equals(peran)) {
            String s = kosongJadiNull(data1);
            if (s != null && s.length() > 50) {
                return BATAS + "Hak akses maks. 50 karakter";
            }
        } else if ("Supervisor".equals(peran)) {
            String s = kosongJadiNull(data1);
            if (s != null && s.length() > 50) {
                return BATAS + "Area pengawasan maks. 50 karakter";
            }
        }
        return null;
    }

    private String normalisasiData1(String peran, String data1) {
        if ("Admin".equals(peran) || "Supervisor".equals(peran)) {
            return kosongJadiNull(data1);
        }
        return data1;
    }

    // CREATE
    public Hasil tambah(String nama, String username, String password,
            String email, String telepon, String peran,
            String data1, String data2) {

        nama = nama == null ? "" : nama.trim();
        username = username == null ? "" : username.trim();
        password = password == null ? "" : password;
        email = email == null ? "" : email.trim();
        telepon = telepon == null ? "" : telepon.trim();

        if (nama.isEmpty() || nama.length() > 60) {
            return Hasil.peringatan(BATAS + "Nama lengkap wajib diisi (maks. 60 karakter)");
        }
        if (username.isEmpty() || username.length() > 30) {
            return Hasil.peringatan(BATAS + "Username wajib diisi (maks. 30 karakter)");
        }
        if (password.length() < 6 || password.length() > 30) {
            return Hasil.peringatan(BATAS + "Password 6-30 karakter");
        }
        if (email.length() > 50) {
            return Hasil.peringatan(BATAS + "Email maks. 50 karakter");
        }
        if (telepon.isEmpty() || telepon.length() > 20) {
            return Hasil.peringatan(BATAS + "No. telepon wajib diisi (maks. 20 karakter)");
        }
        String salah = validasiDataKhusus(peran, data1);
        if (salah != null) {
            return Hasil.peringatan(salah);
        }

        // username tidak boleh kembar
        for (Pengguna p : penggunaDAO.getAllPengguna()) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                return Hasil.peringatan("Username sudah digunakan");
            }
        }

        boolean ok = penggunaDAO.tambahPengguna(nama, username, password,
                kosongJadiNull(email), telepon, peran, "Aktif",
                normalisasiData1(peran, data1), data2);

        return ok
                ? Hasil.berhasil("Pengguna berhasil ditambahkan")
                : Hasil.kesalahan("Data pengguna gagal disimpan. Periksa koneksi database.");
    }

    // UPDATE: username, peran, dan status tidak diubah; password kosong =
    // password lama dipertahankan
    public Hasil ubah(Pengguna lama, String nama, String password,
            String email, String telepon, String data1, String data2) {

        nama = nama == null ? "" : nama.trim();
        password = password == null ? "" : password;
        email = email == null ? "" : email.trim();
        telepon = telepon == null ? "" : telepon.trim();

        if (nama.isEmpty() || nama.length() > 60) {
            return Hasil.peringatan(BATAS + "Nama lengkap wajib diisi (maks. 60 karakter)");
        }
        if (!password.isEmpty() && (password.length() < 6 || password.length() > 30)) {
            return Hasil.peringatan(BATAS + "Password baru 6-30 karakter");
        }
        if (email.length() > 50) {
            return Hasil.peringatan(BATAS + "Email maks. 50 karakter");
        }
        if (telepon.isEmpty() || telepon.length() > 20) {
            return Hasil.peringatan(BATAS + "No. telepon wajib diisi (maks. 20 karakter)");
        }
        String salah = validasiDataKhusus(lama.getPeran(), data1);
        if (salah != null) {
            return Hasil.peringatan(salah);
        }

        boolean ok = penggunaDAO.updatePengguna(lama.getIdPengguna(), nama,
                lama.getUsername(), password, kosongJadiNull(email),
                telepon, lama.getStatusAktif(),
                normalisasiData1(lama.getPeran(), data1), data2);

        return ok
                ? Hasil.berhasil("Data pengguna berhasil diperbarui")
                : Hasil.kesalahan("Data pengguna gagal disimpan. Periksa koneksi database.");
    }

    public boolean isAktif(Pengguna p) {
        return "Aktif".equals(p.getStatusAktif());
    }

    // Peringatan bila status pengguna tidak boleh diubah (null = boleh)
    public String cekUbahStatus(Pengguna p) {
        if (isAktif(p) && p.getIdPengguna() == idLogin()) {
            return "Akun sendiri tidak dapat dinonaktifkan";
        }
        return null;
    }

    // Aktif -> Nonaktif, Nonaktif -> Aktif
    public Hasil ubahStatus(Pengguna p) {
        String larangan = cekUbahStatus(p);
        if (larangan != null) {
            return Hasil.peringatan(larangan);
        }

        boolean ok = isAktif(p)
                ? penggunaDAO.hapusPengguna(p.getIdPengguna())
                : aktifkanKembali(p);

        return ok
                ? Hasil.berhasil("Status pengguna berhasil diperbarui")
                : Hasil.kesalahan("Status pengguna gagal diperbarui");
    }

    // DAO hanya punya "nonaktifkan"; aktifkan lagi lewat updatePengguna
    // (password dikosongkan = password lama dipertahankan)
    private boolean aktifkanKembali(Pengguna p) {
        String data1 = null;
        String data2 = null;

        if (p instanceof Admin) {
            data1 = ((Admin) p).getHakAkses();
        } else if (p instanceof Supervisor) {
            data1 = ((Supervisor) p).getAreaPengawasan();
        } else if (p instanceof Petugas) {
            data1 = ((Petugas) p).getSpesialisasi();
            data2 = ((Petugas) p).getShiftKerja();
        }

        return penggunaDAO.updatePengguna(p.getIdPengguna(),
                p.getNamaLengkap(), p.getUsername(), "", p.getEmail(),
                p.getNoTelepon(), "Aktif", data1, data2);
    }
}
