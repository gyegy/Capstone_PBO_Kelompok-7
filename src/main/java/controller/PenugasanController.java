package controller;

import dao.PenugasanDAO;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.LaporanKerusakan;
import model.Penugasan;
import model.Pengguna;
import model.Petugas;

/**
 * Controller sisi Supervisor: ringkasan menu, penugasan laporan ke petugas,
 * dan pemantauan progres. Dipakai SupervisorMenuFrame, PenugasanFrame,
 * PantauProgresFrame, dan RiwayatPenugasanFrame.
 */
public class PenugasanController extends BaseController {

    private final PenugasanDAO penugasanDAO = new PenugasanDAO();

    private int urutPrioritas(String prioritas) {
        if ("Tinggi".equals(prioritas)) {
            return 0;
        }
        if ("Sedang".equals(prioritas)) {
            return 1;
        }
        return 2;
    }

    // ---------------------------------------------------------- Ringkasan

    // Laporan berstatus Menunggu dan belum memiliki penugasan, urut prioritas.
    // (Status laporan tetap Menunggu sampai Petugas mencatat progres pertama,
    // jadi laporan yang sudah ditugaskan disaring lewat tabel PENUGASAN.)
    public List<LaporanKerusakan> getLaporanMenunggu() {
        Set<Integer> sudahDitugaskan = penugasanPerLaporan().keySet();
        List<LaporanKerusakan> hasil = new ArrayList<>();

        for (LaporanKerusakan l : semuaLaporan()) {
            if ("Menunggu".equals(l.getStatus())
                    && !sudahDitugaskan.contains(l.getIdLaporan())) {
                hasil.add(l);
            }
        }

        hasil.sort(Comparator
                .comparingInt((LaporanKerusakan l) -> urutPrioritas(l.getPrioritas()))
                .thenComparingInt(LaporanKerusakan::getIdLaporan));
        return hasil;
    }

    // Petugas dengan status akun Aktif
    public List<Petugas> getPetugasAktif() {
        List<Petugas> hasil = new ArrayList<>();
        for (Pengguna u : new dao.PenggunaDAO().getAllPengguna()) {
            if (u instanceof Petugas && "Aktif".equals(u.getStatusAktif())) {
                hasil.add((Petugas) u);
            }
        }
        return hasil;
    }

    // Penugasan yang laporannya belum Selesai
    public int jumlahPenugasanBerjalan() {
        Map<Integer, LaporanKerusakan> laporan = petaLaporan();
        int n = 0;
        for (Penugasan p : semuaPenugasan()) {
            LaporanKerusakan l = laporan.get(p.getLaporanIdLaporan());
            if (l != null && !"Selesai".equals(l.getStatus())) {
                n++;
            }
        }
        return n;
    }

    // Laporan yang progres "Selesai"-nya tercatat pada bulan berjalan
    public int jumlahSelesaiBulanIni() {
        Map<Integer, LaporanKerusakan> laporan = petaLaporan();
        YearMonth sekarang = YearMonth.now();
        int n = 0;

        for (Penugasan p : semuaPenugasan()) {
            LaporanKerusakan l = laporan.get(p.getLaporanIdLaporan());
            if (l == null || !"Selesai".equals(l.getStatus())) {
                continue;
            }
            Timestamp t = waktuSelesai(p.getIdPenugasan());
            if (t != null && YearMonth.from(t.toLocalDateTime()).equals(sekarang)) {
                n++;
            }
        }
        return n;
    }

    // ---------------------------------------------------------- Penugasan

    // CREATE penugasan + detail_penugasan (satu baris per petugas, Aktif).
    // Petugas pertama pada daftar = Petugas Utama (penugasan.id_pengguna).
    public Hasil buatPenugasan(LaporanKerusakan laporan,
            List<Petugas> urutanPetugas, String catatan) {

        if (laporan == null) {
            return Hasil.peringatan("Pilih satu laporan terlebih dahulu");
        }
        if (urutanPetugas == null || urutanPetugas.isEmpty()) {
            return Hasil.peringatan("Pilih minimal satu petugas");
        }

        catatan = catatan == null ? "" : catatan.trim();
        if (catatan.length() > 150) {
            return Hasil.peringatan("Catatan melebihi 150 karakter");
        }

        // laporan yang sudah punya penugasan tidak boleh ditugaskan lagi
        if (penugasanPerLaporan().containsKey(laporan.getIdLaporan())) {
            return Hasil.peringatan("Laporan ini sudah memiliki penugasan");
        }

        List<Integer> idPetugas = new ArrayList<>();
        for (Petugas p : urutanPetugas) {
            idPetugas.add(p.getIdPengguna());
        }

        Penugasan p = new Penugasan();
        p.setWaktuPenugasan(Date.valueOf(LocalDate.now()));
        p.setCatatanPenugasan(catatan.isEmpty() ? null : catatan);
        p.setSupervisorIdPengguna(idLogin());
        p.setLaporanIdLaporan(laporan.getIdLaporan());
        p.setIdPengguna(idPetugas.get(0)); // Petugas utama

        return penugasanDAO.tambahPenugasan(p, idPetugas)
                ? Hasil.berhasil("Penugasan berhasil dibuat")
                : Hasil.kesalahan("Penugasan gagal disimpan");
    }
}
