package controller;

import dao.FasilitasDAO;
import dao.LaporanKerusakanDAO;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Fasilitas;
import model.LaporanKerusakan;

/**
 * Controller Pengelolaan Laporan Kerusakan (Admin): catat, lihat, ubah
 * (hanya status Menunggu). Dipakai LaporanFrame dan LaporanDialog.
 */
public class LaporanController extends BaseController {

    private final LaporanKerusakanDAO laporanDAO = new LaporanKerusakanDAO();
    private final FasilitasDAO fasilitasDAO = new FasilitasDAO();

    // READ: laporan where ADMIN_id_pengguna = admin yang login
    public List<LaporanKerusakan> getLaporanAdminLogin() {
        List<LaporanKerusakan> hasil = new ArrayList<>();
        for (LaporanKerusakan l : laporanDAO.getAllLaporan()) {
            if (l.getAdminIdPengguna() == idLogin()) {
                hasil.add(l);
            }
        }
        return hasil;
    }

    // id_fasilitas -> "Nama - Lokasi"
    public Map<Integer, String> petaNamaFasilitas() {
        Map<Integer, String> peta = new HashMap<>();
        for (Fasilitas f : fasilitasDAO.getAllFasilitas()) {
            peta.put(f.getIdFasilitas(), f.getNamaFasilitas() + " - " + f.getLokasi());
        }
        return peta;
    }

    public boolean adaYangBisaDiubah(List<LaporanKerusakan> daftar) {
        for (LaporanKerusakan l : daftar) {
            if ("Menunggu".equals(l.getStatus())) {
                return true;
            }
        }
        return false;
    }

    public boolean bisaDiubah(LaporanKerusakan l) {
        return "Menunggu".equals(l.getStatus());
    }

    // CREATE. fasilitas / prioritas null = belum dipilih.
    // Tanggal = hari ini, status = Menunggu (diatur DAO), admin = admin login.
    public Hasil catat(Fasilitas fasilitas, String deskripsi, String prioritas) {
        deskripsi = deskripsi == null ? "" : deskripsi.trim();

        if (fasilitas == null || deskripsi.isEmpty() || prioritas == null) {
            return Hasil.peringatan("Data laporan belum lengkap");
        }
        if (deskripsi.length() > 150) {
            return Hasil.peringatan("Deskripsi melebihi batas 150 karakter");
        }

        LaporanKerusakan laporan = new LaporanKerusakan();
        laporan.setDeskripsiKerusakan(deskripsi);
        laporan.setPrioritas(prioritas);
        laporan.setFasilitasIdFasilitas(fasilitas.getIdFasilitas());
        laporan.setTanggalLaporan(Date.valueOf(LocalDate.now()));
        laporan.setAdminIdPengguna(idLogin());

        if (!laporanDAO.tambahLaporan(laporan)) {
            return Hasil.kesalahan("Laporan gagal disimpan. Periksa koneksi database.");
        }

        // kondisi fasilitas otomatis menjadi Rusak
        fasilitas.setKondisi("Rusak");
        fasilitasDAO.updateFasilitas(fasilitas);

        return Hasil.berhasil("Laporan berhasil dicatat"
                + cariIdBaru(fasilitas, deskripsi));
    }

    // UPDATE: fasilitas tidak dapat diganti; hanya deskripsi & prioritas
    public Hasil ubah(LaporanKerusakan lama, String deskripsi, String prioritas) {
        deskripsi = deskripsi == null ? "" : deskripsi.trim();

        if (deskripsi.isEmpty() || prioritas == null) {
            return Hasil.peringatan("Data laporan belum lengkap");
        }
        if (deskripsi.length() > 150) {
            return Hasil.peringatan("Deskripsi melebihi batas 150 karakter");
        }

        LaporanKerusakan laporan = new LaporanKerusakan();
        laporan.setIdLaporan(lama.getIdLaporan());
        laporan.setDeskripsiKerusakan(deskripsi);
        laporan.setPrioritas(prioritas);
        laporan.setFasilitasIdFasilitas(lama.getFasilitasIdFasilitas());
        laporan.setTanggalLaporan(lama.getTanggalLaporan());
        laporan.setAdminIdPengguna(lama.getAdminIdPengguna());

        return laporanDAO.updateLaporan(laporan)
                ? Hasil.berhasil("Laporan berhasil diperbarui")
                : Hasil.kesalahan("Laporan gagal diperbarui. Laporan hanya dapat diubah "
                        + "selama berstatus Menunggu dan belum ditugaskan.");
    }

    // ID laporan baru = laporan milik admin ini untuk fasilitas & deskripsi tsb
    private String cariIdBaru(Fasilitas fasilitas, String deskripsi) {
        for (LaporanKerusakan l : laporanDAO.getAllLaporan()) {
            if (l.getAdminIdPengguna() == idLogin()
                    && l.getFasilitasIdFasilitas() == fasilitas.getIdFasilitas()
                    && deskripsi.equals(l.getDeskripsiKerusakan())) {
                return " (ID laporan: " + String.format("L-%04d", l.getIdLaporan()) + ")";
            }
        }
        return "";
    }
}
