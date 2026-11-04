package view.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Pemformat tanggal, angka, dan ID tampilan (mis. L-0105, T-022, P001).
 */
public final class FormatUi {

    private static final SimpleDateFormat TANGGAL =
            new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
    private static final SimpleDateFormat TANGGAL_WAKTU =
            new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("id", "ID"));
    private static final NumberFormat ANGKA =
            NumberFormat.getInstance(new Locale("id", "ID"));

    private FormatUi() {
    }

    public static String tanggal(Date nilai) {
        return nilai == null ? "-" : TANGGAL.format(nilai);
    }

    public static String tanggalWaktu(Date nilai) {
        return nilai == null ? "-" : TANGGAL_WAKTU.format(nilai);
    }

    public static String angka(int nilai) {
        return ANGKA.format(nilai);
    }

    public static String idLaporan(int id) {
        return String.format("L-%04d", id);
    }

    public static String idTugas(int id) {
        return String.format("T-%03d", id);
    }

    public static String idFasilitas(int id) {
        return String.format("F%03d", id);
    }

    public static String idPengguna(int id) {
        return String.format("P%03d", id);
    }
}
