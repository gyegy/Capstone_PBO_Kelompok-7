package controller;

import dao.FasilitasDAO;
import java.util.List;
import model.Fasilitas;
import model.LaporanKerusakan;

/**
 * Controller Kelola Data Fasilitas (Admin): lihat, cari, tambah, ubah, hapus.
 * Dipakai FasilitasFrame dan FasilitasDialog.
 */
public class FasilitasController extends BaseController {

    private final FasilitasDAO fasilitasDAO = new FasilitasDAO();

    // READ: semua fasilitas atau hasil pencarian
    public List<Fasilitas> getDaftar(String kataKunci) {
        if (kataKunci == null || kataKunci.trim().isEmpty()) {
            return fasilitasDAO.getAllFasilitas();
        }
        return fasilitasDAO.searchFasilitas(kataKunci.trim());
    }

    private String validasi(String nama, String jenis, String lokasi, String keterangan) {
        if (nama.isEmpty() || jenis.isEmpty() || lokasi.isEmpty()
                || nama.length() > 30 || jenis.length() > 30
                || lokasi.length() > 50 || keterangan.length() > 60) {
            return "Data tidak lengkap atau melebihi batas";
        }
        return null;
    }

    private String teks(String s) {
        return s == null ? "" : s.trim();
    }

    // CREATE (ADMIN_id_pengguna = admin yang sedang login)
    public Hasil tambah(String nama, String jenis, String lokasi,
            String kondisi, String keterangan) {

        nama = teks(nama);
        jenis = teks(jenis);
        lokasi = teks(lokasi);
        keterangan = teks(keterangan);

        String salah = validasi(nama, jenis, lokasi, keterangan);
        if (salah != null) {
            return Hasil.peringatan(salah);
        }

        Fasilitas f = new Fasilitas();
        f.setNamaFasilitas(nama);
        f.setJenisFasilitas(jenis);
        f.setLokasi(lokasi);
        f.setKondisi(kondisi);
        f.setKeterangan(keterangan.isEmpty() ? null : keterangan);
        f.setAdminIdPengguna(idLogin());

        return fasilitasDAO.tambahFasilitas(f)
                ? Hasil.berhasil("Fasilitas berhasil ditambahkan")
                : Hasil.kesalahan("Data fasilitas gagal disimpan. Periksa koneksi database.");
    }

    // UPDATE
    public Hasil ubah(Fasilitas lama, String nama, String jenis, String lokasi,
            String kondisi, String keterangan) {

        nama = teks(nama);
        jenis = teks(jenis);
        lokasi = teks(lokasi);
        keterangan = teks(keterangan);

        String salah = validasi(nama, jenis, lokasi, keterangan);
        if (salah != null) {
            return Hasil.peringatan(salah);
        }

        Fasilitas f = new Fasilitas();
        f.setIdFasilitas(lama.getIdFasilitas());
        f.setNamaFasilitas(nama);
        f.setJenisFasilitas(jenis);
        f.setLokasi(lokasi);
        f.setKondisi(kondisi);
        f.setKeterangan(keterangan.isEmpty() ? null : keterangan);
        f.setAdminIdPengguna(lama.getAdminIdPengguna());

        return fasilitasDAO.updateFasilitas(f)
                ? Hasil.berhasil("Data fasilitas berhasil diperbarui")
                : Hasil.kesalahan("Data fasilitas gagal disimpan. Periksa koneksi database.");
    }

    // Peringatan bila fasilitas tidak boleh dihapus (sudah punya riwayat laporan)
    public String cekHapus(Fasilitas f) {
        for (LaporanKerusakan l : semuaLaporan()) {
            if (l.getFasilitasIdFasilitas() == f.getIdFasilitas()) {
                return "Fasilitas tidak dapat dihapus (memiliki riwayat laporan)";
            }
        }
        return null;
    }

    // DELETE
    public Hasil hapus(Fasilitas f) {
        String larangan = cekHapus(f);
        if (larangan != null) {
            return Hasil.peringatan(larangan);
        }
        return fasilitasDAO.hapusFasilitas(f.getIdFasilitas())
                ? Hasil.berhasil("Fasilitas berhasil dihapus")
                : Hasil.kesalahan("Fasilitas gagal dihapus");
    }
}
