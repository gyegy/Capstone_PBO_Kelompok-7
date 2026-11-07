package view;

import config.Sesi;
import dao.DetailPenugasanDAO;
import dao.LaporanKerusakanDAO;
import dao.PenggunaDAO;
import dao.PenugasanDAO;
import dao.ProgresPenangananDAO;
import dao.RekapitulasiDAO;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import model.Fasilitas;
import model.LaporanKerusakan;
import model.Penugasan;
import model.Pengguna;
import model.Petugas;
import model.ProgresPenanganan;

/**
 * Utilitas pendukung tampilan Supervisor (bukan GUI): pengambilan dan
 * pengolahan data untuk layar Penugasan, Pantau Progres, dan Rekap.
 * Hanya memanggil DAO yang sudah ada.
 */
public final class SupervisorUtil {

    // Batas rentang bila periode rekap dikosongkan
    public static final Date TGL_MIN = Date.valueOf("1900-01-01");
    public static final Date TGL_MAX = Date.valueOf("2999-12-31");

    private SupervisorUtil() {
    }

    public static int idSupervisor() {
        return Integer.parseInt(Sesi.getIdPengguna());
    }

    public static int urutPrioritas(String prioritas) {
        if ("Tinggi".equals(prioritas)) {
            return 0;
        }
        if ("Sedang".equals(prioritas)) {
            return 1;
        }
        return 2;
    }

    // id_laporan -> penugasan (penugasan terbaru bila ada lebih dari satu)
    public static Map<Integer, Penugasan> penugasanPerLaporan() {
        Map<Integer, Penugasan> peta = new HashMap<>();
        for (Penugasan p : new PenugasanDAO().getAllPenugasan()) {
            peta.putIfAbsent(p.getLaporanIdLaporan(), p);
        }
        return peta;
    }

    // Laporan berstatus Menunggu dan belum memiliki penugasan.
    // (Status laporan tetap Menunggu sampai Petugas mencatat progres pertama,
    // jadi laporan yang sudah ditugaskan harus disaring lewat tabel PENUGASAN.)
    public static List<LaporanKerusakan> laporanMenungguPenugasan() {
        Set<Integer> sudahDitugaskan = penugasanPerLaporan().keySet();
        List<LaporanKerusakan> hasil = new ArrayList<>();

        for (LaporanKerusakan l : new LaporanKerusakanDAO().getAllLaporan()) {
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
    public static List<Petugas> petugasAktif() {
        List<Petugas> hasil = new ArrayList<>();
        for (Pengguna u : new PenggunaDAO().getAllPengguna()) {
            if (u instanceof Petugas && "Aktif".equals(u.getStatusAktif())) {
                hasil.add((Petugas) u);
            }
        }
        return hasil;
    }

    public static Map<Integer, String> namaPengguna() {
        Map<Integer, String> peta = new HashMap<>();
        for (Pengguna u : new PenggunaDAO().getAllPengguna()) {
            peta.put(u.getIdPengguna(), u.getNamaLengkap());
        }
        return peta;
    }

    // id_penugasan -> daftar id petugas (selain Digantikan)
    public static Map<Integer, List<Integer>> petugasPerPenugasan() {
        Map<Integer, List<Integer>> peta = new HashMap<>();
        for (int[] k : new DetailPenugasanDAO().getKeterlibatan()) {
            peta.computeIfAbsent(k[1], x -> new ArrayList<>()).add(k[0]);
        }
        return peta;
    }

    // Nama petugas pada sebuah penugasan, petugas utama paling depan
    public static String namaPetugas(Penugasan p,
            Map<Integer, List<Integer>> petugas,
            Map<Integer, String> nama, boolean tandaiUtama) {

        List<Integer> ids = new ArrayList<>(
                petugas.getOrDefault(p.getIdPenugasan(), new ArrayList<>()));

        boolean adaUtama = ids.remove(Integer.valueOf(p.getIdPengguna()));
        if (adaUtama) {
            ids.add(0, p.getIdPengguna());
        }
        if (ids.isEmpty()) {
            return "-";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(nama.getOrDefault(ids.get(i), "?"));
            if (tandaiUtama && adaUtama && i == 0) {
                sb.append(" (utama)");
            }
        }
        return sb.toString();
    }

    // Progres paling akhir sebuah penugasan (null bila belum ada)
    public static ProgresPenanganan progresTerakhir(int idPenugasan) {
        List<ProgresPenanganan> daftar =
                new ProgresPenangananDAO().getAllProgres(idPenugasan);
        return daftar.isEmpty() ? null : daftar.get(daftar.size() - 1);
    }

    // Waktu progres "Selesai" pada sebuah penugasan (null bila belum selesai)
    public static Timestamp waktuSelesai(int idPenugasan) {
        Timestamp hasil = null;
        for (ProgresPenanganan r : new ProgresPenangananDAO().getAllProgres(idPenugasan)) {
            if ("Selesai".equals(r.getStatusProgres())) {
                hasil = r.getWaktuUpdate();
            }
        }
        return hasil;
    }

    // Penugasan yang laporannya belum Selesai
    public static int jumlahPenugasanBerjalan() {
        Map<Integer, LaporanKerusakan> laporan = ViewUtil.petaLaporan();
        int n = 0;
        for (Penugasan p : new PenugasanDAO().getAllPenugasan()) {
            LaporanKerusakan l = laporan.get(p.getLaporanIdLaporan());
            if (l != null && !"Selesai".equals(l.getStatus())) {
                n++;
            }
        }
        return n;
    }

    // Laporan yang progres "Selesai"-nya tercatat pada bulan berjalan
    public static int jumlahSelesaiBulanIni() {
        Map<Integer, LaporanKerusakan> laporan = ViewUtil.petaLaporan();
        YearMonth sekarang = YearMonth.now();
        int n = 0;

        for (Penugasan p : new PenugasanDAO().getAllPenugasan()) {
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

    public static List<LaporanKerusakan> laporanPeriode(Date dari, Date sampai) {
        Date d1 = dari == null ? TGL_MIN : dari;
        Date d2 = sampai == null ? TGL_MAX : sampai;
        List<LaporanKerusakan> hasil = new ArrayList<>();

        for (LaporanKerusakan l : new LaporanKerusakanDAO().getAllLaporan()) {
            if (l.getTanggalLaporan() != null
                    && !l.getTanggalLaporan().before(d1)
                    && !l.getTanggalLaporan().after(d2)) {
                hasil.add(l);
            }
        }
        return hasil;
    }

    public static String formatHari(double hari) {
        return String.format(new Locale("id", "ID"), "%.1f hari", hari);
    }

    // ------------------------------------------------------------ Rekap

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

    // Rekap dashboard: status, prioritas, fasilitas berulang, beban petugas,
    // dan waktu penyelesaian. Periode boleh null (= tanpa batas).
    public static HasilRekap hitungRekap(Date dari, Date sampai) {
        HasilRekap h = new HasilRekap();
        Date d1 = dari == null ? TGL_MIN : dari;
        Date d2 = sampai == null ? TGL_MAX : sampai;

        h.angka = new RekapitulasiDAO().getRekapLaporan(d1, d2);

        List<LaporanKerusakan> laporan = laporanPeriode(dari, sampai);
        Map<Integer, Fasilitas> fasilitas = ViewUtil.petaFasilitas();
        Map<Integer, Penugasan> penugasan = penugasanPerLaporan();

        // Fasilitas dengan gangguan berulang (>= 2 laporan pada periode)
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

        Map<Integer, String> nama = namaPengguna();
        for (Map.Entry<Integer, Set<Integer>> e : tugasPerPetugas.entrySet()) {
            BarisBeban b = new BarisBeban();
            b.nama = nama.getOrDefault(e.getKey(), "?");
            b.penugasan = e.getValue().size();

            Set<Integer> laporanDitangani = new HashSet<>();
            for (Integer idTugas : e.getValue()) {
                String status = statusPerPenugasan.get(idTugas);
                if ("Diproses".equals(status) || "Selesai".equals(status)) {
                    laporanDitangani.add(laporanPerPenugasan.get(idTugas));
                }
            }
            b.laporanDitangani = laporanDitangani.size();
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
}
