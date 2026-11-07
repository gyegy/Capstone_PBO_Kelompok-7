package controller;

import dao.DetailPenugasanDAO;
import dao.RekapitulasiDAO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import model.Fasilitas;
import model.LaporanKerusakan;
import model.Penugasan;
import model.ProgresPenanganan;

/**
 * Controller Rekap Laporan (Supervisor): status, prioritas, fasilitas
 * berulang, beban petugas, waktu penyelesaian, dan riwayat gangguan satu
 * fasilitas. Dipakai RekapFrame dan RiwayatGangguanFrame.
 */
public class RekapController extends BaseController {

    public static class BarisFasilitas {
        public int idFasilitas;
        public String nama;
        public String jenis;
        public int jumlah;
    }

    public static class BarisBeban {
        public String nama;
        public int penugasan;
        public int laporanDitangani;
    }

    public static class HasilRekap {
        public Map<String, Integer> angka = new LinkedHashMap<>();
        public List<BarisFasilitas> berulang = new ArrayList<>();
        public List<BarisBeban> beban = new ArrayList<>();
        public double rataHari = -1;
        public int jumlahSelesai = 0;
    }

    // Rekap dashboard. Periode boleh kosong (= tanpa batas).
    // "Fasilitas berulang" = minimal 2 laporan pada periode;
    // "Laporan ditangani" = laporan berstatus Diproses atau Selesai.
    public HasilRekap hitungRekap(Periode periode) {
        HasilRekap h = new HasilRekap();

        h.angka = new RekapitulasiDAO().getRekapLaporan(
                periode.batasAwal(), periode.batasAkhir());

        List<LaporanKerusakan> laporan = laporanPeriode(periode);
        Map<Integer, Fasilitas> fasilitas = petaFasilitas();
        Map<Integer, Penugasan> penugasan = penugasanPerLaporan();

        // Fasilitas dengan gangguan berulang
        Map<Integer, Integer> perFasilitas = new LinkedHashMap<>();
        for (LaporanKerusakan l : laporan) {
            perFasilitas.merge(l.getFasilitasIdFasilitas(), 1, Integer::sum);
        }
        for (Map.Entry<Integer, Integer> e : perFasilitas.entrySet()) {
            if (e.getValue() < 2) {
                continue;
            }
            Fasilitas f = fasilitas.get(e.getKey());
            BarisFasilitas b = new BarisFasilitas();
            b.idFasilitas = e.getKey();
            b.nama = f == null ? "-" : f.getNamaFasilitas();
            b.jenis = f == null ? "-" : f.getJenisFasilitas();
            b.jumlah = e.getValue();
            h.berulang.add(b);
        }
        h.berulang.sort(Comparator.comparingInt((BarisFasilitas b) -> -b.jumlah)
                .thenComparing(b -> b.nama));

        // Beban petugas melalui DETAIL_PENUGASAN
        Map<Integer, String> statusPerPenugasan = new HashMap<>();
        Map<Integer, Integer> laporanPerPenugasan = new HashMap<>();
        for (LaporanKerusakan l : laporan) {
            Penugasan p = penugasan.get(l.getIdLaporan());
            if (p != null) {
                statusPerPenugasan.put(p.getIdPenugasan(), l.getStatus());
                laporanPerPenugasan.put(p.getIdPenugasan(), l.getIdLaporan());
            }
        }

        Map<Integer, Set<Integer>> tugasPerPetugas = new TreeMap<>();
        for (int[] k : new DetailPenugasanDAO().getKeterlibatan()) {
            if (statusPerPenugasan.containsKey(k[1])) {
                tugasPerPetugas.computeIfAbsent(k[0], x -> new HashSet<>()).add(k[1]);
            }
        }

        Map<Integer, String> nama = petaNamaPengguna();
        for (Map.Entry<Integer, Set<Integer>> e : tugasPerPetugas.entrySet()) {
            BarisBeban b = new BarisBeban();
            b.nama = nama.getOrDefault(e.getKey(), "?");
            b.penugasan = e.getValue().size();

            Set<Integer> ditangani = new HashSet<>();
            for (Integer idTugas : e.getValue()) {
                String status = statusPerPenugasan.get(idTugas);
                if ("Diproses".equals(status) || "Selesai".equals(status)) {
                    ditangani.add(laporanPerPenugasan.get(idTugas));
                }
            }
            b.laporanDitangani = ditangani.size();
            h.beban.add(b);
        }
        h.beban.sort(Comparator.comparingInt((BarisBeban b) -> -b.penugasan)
                .thenComparing(b -> b.nama));

        // Waktu penyelesaian: waktu_update progres Selesai - tanggal_laporan
        double total = 0;
        int jumlah = 0;
        for (LaporanKerusakan l : laporan) {
            if (!"Selesai".equals(l.getStatus())) {
                continue;
            }
            Penugasan p = penugasan.get(l.getIdLaporan());
            if (p == null) {
                continue;
            }
            Timestamp t = waktuSelesai(p.getIdPenugasan());
            if (t == null) {
                continue;
            }
            total += (t.getTime() - l.getTanggalLaporan().getTime()) / 86400000.0;
            jumlah++;
        }
        h.jumlahSelesai = jumlah;
        h.rataHari = jumlah == 0 ? -1 : total / jumlah;

        return h;
    }

    // --------------------------------------------------- Riwayat gangguan

    public static class BarisGangguan {
        public LaporanKerusakan laporan;
        public ProgresPenanganan progresTerakhir;   // null = belum ada
    }

    public static class RiwayatGangguan {
        public Fasilitas fasilitas;                  // null bila tidak ditemukan
        public List<BarisGangguan> baris = new ArrayList<>();
    }

    // Laporan satu fasilitas pada periode, terbaru di atas, beserta progres
    // terakhir penanganannya
    public RiwayatGangguan getRiwayatGangguan(int idFasilitas, Periode periode) {
        RiwayatGangguan hasil = new RiwayatGangguan();
        hasil.fasilitas = petaFasilitas().get(idFasilitas);

        List<LaporanKerusakan> daftar = new ArrayList<>();
        for (LaporanKerusakan l : laporanPeriode(periode)) {
            if (l.getFasilitasIdFasilitas() == idFasilitas) {
                daftar.add(l);
            }
        }
        daftar.sort((a, b) -> {
            int c = b.getTanggalLaporan().compareTo(a.getTanggalLaporan());
            return c != 0 ? c : Integer.compare(b.getIdLaporan(), a.getIdLaporan());
        });

        Map<Integer, Penugasan> tugas = penugasanPerLaporan();
        for (LaporanKerusakan l : daftar) {
            Penugasan p = tugas.get(l.getIdLaporan());
            BarisGangguan b = new BarisGangguan();
            b.laporan = l;
            b.progresTerakhir = p == null ? null : progresTerakhir(p.getIdPenugasan());
            hasil.baris.add(b);
        }
        return hasil;
    }
}
