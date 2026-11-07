package view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class ViewHelper {

    private static final Locale ID = Locale.forLanguageTag("id-ID");
    private static final String[] POLA_TANGGAL = {
        "dd MMM yyyy", "d MMM yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy"
    };

    private ViewHelper() {
    }

    public static String tanggal(java.util.Date d) {
        return d == null ? "-" : new SimpleDateFormat("dd MMM yyyy", ID).format(d);
    }

    public static String waktu(java.util.Date d) {
        return d == null ? "-" : new SimpleDateFormat("dd MMM yyyy HH:mm", ID).format(d);
    }

    public static String kodeLaporan(int id) {
        return String.format("L-%04d", id);
    }

    public static String kodeTugas(int id) {
        return String.format("T-%03d", id);
    }

    public static int angka(Object teks) {
        if (teks == null) {
            return -1;
        }
        String digit = teks.toString().replaceAll("\\D", "");
        if (digit.isEmpty() || digit.length() > 9) {
            return -1;
        }
        return Integer.parseInt(digit);
    }

    public static Date parseTanggal(String teks) {
        if (teks == null || teks.trim().isEmpty()) {
            return null;
        }
        String t = teks.trim();
        for (String pola : POLA_TANGGAL) {
            SimpleDateFormat f = new SimpleDateFormat(pola, ID);
            f.setLenient(false);
            try {
                return new Date(f.parse(t).getTime());
            } catch (ParseException e) {
                // coba pola berikutnya
            }
        }
        throw new IllegalArgumentException("Format tanggal tidak dikenali: " + t);
    }

    public static void namaiKartu(Container c, String... nama) {
        Component[] kartu = c.getComponents();
        if (!(c.getLayout() instanceof CardLayout) || kartu.length != nama.length) {
            return;
        }
        c.removeAll();
        for (int i = 0; i < kartu.length; i++) {
            c.add(kartu[i], nama[i]);
        }
        ((CardLayout) c.getLayout()).show(c, nama[0]);
    }
}
