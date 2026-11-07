package view;

import dao.DetailPenugasanDAO;
import dao.FasilitasDAO;
import dao.LaporanKerusakanDAO;
import dao.PenugasanDAO;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import model.Fasilitas;
import model.LaporanKerusakan;
import model.Penugasan;

/**
 * Utilitas pendukung tampilan (bukan GUI): format teks dan pengambilan
 * data tugas Petugas. Hanya memanggil DAO yang sudah ada.
 */
public final class ViewUtil {

    private static final Locale ID = new Locale("id", "ID");

    private ViewUtil() {
    }

    public static String idLaporan(int id) {
        return String.format("L-%04d", id);
    }

    public static String idTugas(int id) {
        return String.format("T-%03d", id);
    }

    public static String tanggal(Date d) {
        return d == null ? "-" : new SimpleDateFormat("dd MMM yyyy", ID).format(d);
    }

    public static String waktu(Timestamp t) {
        return t == null ? "-" : new SimpleDateFormat("dd MMM yyyy HH:mm", ID).format(t);
    }

    public static Map<Integer, Fasilitas> petaFasilitas() {
        Map<Integer, Fasilitas> peta = new HashMap<>();
        for (Fasilitas f : new FasilitasDAO().getAllFasilitas()) {
            peta.put(f.getIdFasilitas(), f);
        }
        return peta;
    }

    public static Map<Integer, LaporanKerusakan> petaLaporan() {
        Map<Integer, LaporanKerusakan> peta = new HashMap<>();
        for (LaporanKerusakan l : new LaporanKerusakanDAO().getAllLaporan()) {
            peta.put(l.getIdLaporan(), l);
        }
        return peta;
    }

    // Penugasan milik Petugas (lewat DETAIL_PENUGASAN).
    // hanyaAktif = true -> keterlibatan Aktif dan laporan belum Selesai
    public static List<Penugasan> penugasanPetugas(int idPetugas, boolean hanyaAktif) {
        List<Integer> ids = new DetailPenugasanDAO()
                .getIdPenugasanByPetugas(idPetugas, hanyaAktif);
        Map<Integer, LaporanKerusakan> laporan = petaLaporan();
        List<Penugasan> hasil = new ArrayList<>();

        for (Penugasan p : new PenugasanDAO().getAllPenugasan()) {
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
}
