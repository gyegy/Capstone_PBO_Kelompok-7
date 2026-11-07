package controller;

import dao.DetailPenugasanDAO;
import dao.PenggunaDAO;
import dao.ProgresPenangananDAO;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.LaporanKerusakan;
import model.Penugasan;
import model.Pengguna;
import model.Petugas;
import model.ProgresPenanganan;

/**
 * Controller sisi Petugas: ringkasan menu, tugas saya, perbarui progres,
 * dan riwayat progres. Dipakai PetugasMenuFrame, TugasSayaFrame,
 * ProgresFrame, dan RiwayatProgresFrame.
 */
public class PetugasController extends BaseController {

    private final ProgresPenangananDAO progresDAO = new ProgresPenangananDAO();

    // ---------------------------------------------------------- Ringkasan

    public static class Ringkasan {
        public int tugasAktif;
        public int diproses;
        public int prioritasTinggi;
        public int selesaiBulanIni;
    }

    // Data Petugas yang sedang login (spesialisasi & shift); null bila tak ada
    public Petugas getProfilLogin() {
        for (Pengguna u : new PenggunaDAO().getAllPengguna()) {
            if (u.getIdPengguna() == idLogin() && u instanceof Petugas) {
                return (Petugas) u;
            }
        }
        return null;
    }

    public Ringkasan getRingkasan() {
        Ringkasan r = new Ringkasan();
        Map<Integer, LaporanKerusakan> laporan = petaLaporan();
        List<Penugasan> aktif = getTugasAktif();

        r.tugasAktif = aktif.size();
        for (Penugasan p : aktif) {
            LaporanKerusakan l = laporan.get(p.getLaporanIdLaporan());
            if (l == null) {
                continue;
            }
            if ("Diproses".equals(l.getStatus())) {
                r.diproses++;
            }
            if ("Tinggi".equals(l.getPrioritas())) {
                r.prioritasTinggi++;
            }
        }

        LocalDate hariIni = LocalDate.now();
        for (RiwayatItem item : getRiwayat()) {
            ProgresPenanganan p = item.progres;
            if ("Selesai".equals(p.getStatusProgres()) && p.getWaktuUpdate() != null) {
                LocalDate d = p.getWaktuUpdate().toLocalDateTime().toLocalDate();
                if (d.getYear() == hariIni.getYear() && d.getMonth() == hariIni.getMonth()) {
                    r.selesaiBulanIni++;
                }
            }
        }
        return r;
    }

    // ------------------------------------------------------------- Tugas

    // Penugasan milik Petugas login (lewat DETAIL_PENUGASAN).
    // hanyaAktif = true -> keterlibatan Aktif dan laporan belum Selesai
    public List<Penugasan> getPenugasan(boolean hanyaAktif) {
        List<Integer> ids = new DetailPenugasanDAO()
                .getIdPenugasanByPetugas(idLogin(), hanyaAktif);
        Map<Integer, LaporanKerusakan> laporan = petaLaporan();
        List<Penugasan> hasil = new ArrayList<>();

        for (Penugasan p : semuaPenugasan()) {
            if (!ids.contains(p.getIdPenugasan())) {
                continue;
            }
            LaporanKerusakan l = laporan.get(p.getLaporanIdLaporan());
            if (hanyaAktif && (l == null || "Selesai".equals(l.getStatus()))) {
                continue;
            }
            hasil.add(p);
        }
        return hasil;
    }

    // READ: tugas aktif Petugas login
    public List<Penugasan> getTugasAktif() {
        return getPenugasan(true);
    }

    // ----------------------------------------------------------- Progres

    // Teks bawaan bila Petugas tidak mengisi tindakan (kolom opsional di alur,
    // tetapi wajib terisi di DAO)
    private String tindakanBawaan(String status) {
        switch (status) {
            case "Pemeriksaan":
                return "Pemeriksaan awal dan pencatatan kondisi";
            case "Perbaikan":
                return "Perbaikan komponen atau penggantian bagian rusak";
            case "Pengujian":
                return "Uji fungsi setelah perbaikan";
            default:
                return "Pemeriksaan akhir dan dokumentasi hasil";
        }
    }

    // CREATE: simpan progres. status null = belum dipilih. DAO memperbarui
    // status laporan / kondisi fasilitas ("Selesai" menutup penugasan).
    public Hasil simpanProgres(Penugasan tugas, String status, String tindakan) {
        if (tugas == null) {
            return Hasil.peringatan("Pilih tugas pada tabel terlebih dahulu");
        }
        if (status == null) {
            return Hasil.peringatan("Status progres wajib dipilih");
        }

        tindakan = tindakan == null ? "" : tindakan.trim();
        if (tindakan.length() > 150) {
            return Hasil.peringatan("Tindakan penanganan melebihi batas 150 karakter");
        }
        if (tindakan.isEmpty()) {
            tindakan = tindakanBawaan(status);
        }

        // Petugas hanya boleh memperbarui tugasnya sendiri
        boolean milikSaya = false;
        for (Penugasan p : getTugasAktif()) {
            if (p.getIdPenugasan() == tugas.getIdPenugasan()) {
                milikSaya = true;
                break;
            }
        }
        if (!milikSaya) {
            return Hasil.peringatan("Tugas ini tidak tersedia untuk Anda");
        }

        ProgresPenanganan progres = new ProgresPenanganan();
        progres.setWaktuUpdate(new Timestamp(System.currentTimeMillis()));
        progres.setStatusProgres(status);
        progres.setTindakanPenanganan(tindakan);
        progres.setPenugasanIdPenugasan(tugas.getIdPenugasan());

        if (!progresDAO.tambahProgres(progres)) {
            return Hasil.kesalahan("Progres gagal disimpan");
        }

        if ("Selesai".equals(status)) {
            return Hasil.berhasil("Progres dicatat, penugasan selesai.\nLaporan "
                    + String.format("L-%04d", tugas.getLaporanIdLaporan())
                    + " kini berstatus Selesai dan kondisi fasilitas menjadi Baik.");
        }
        return Hasil.berhasil("Progres berhasil dicatat");
    }

    // ----------------------------------------------------------- Riwayat

    public static class RiwayatItem {
        public ProgresPenanganan progres;
        public Penugasan tugas;
    }

    // READ: progres dari semua penugasan Petugas login, terbaru di atas
    public List<RiwayatItem> getRiwayat() {
        List<RiwayatItem> hasil = new ArrayList<>();

        for (Penugasan p : getPenugasan(false)) {
            for (ProgresPenanganan r : progresDAO.getAllProgres(p.getIdPenugasan())) {
                RiwayatItem item = new RiwayatItem();
                item.progres = r;
                item.tugas = p;
                hasil.add(item);
            }
        }

        hasil.sort((a, b) -> b.progres.getWaktuUpdate()
                .compareTo(a.progres.getWaktuUpdate()));
        return hasil;
    }
}
