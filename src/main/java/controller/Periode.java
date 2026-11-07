package controller;

import java.sql.Date;

/**
 * Rentang tanggal opsional (kosong = tanpa batas) dengan format yyyy-MM-dd.
 */
public final class Periode {

    public static final Date TGL_MIN = Date.valueOf("1900-01-01");
    public static final Date TGL_MAX = Date.valueOf("2999-12-31");

    private final Date dari;
    private final Date sampai;

    private Periode(Date dari, Date sampai) {
        this.dari = dari;
        this.sampai = sampai;
    }

    public static Periode kosong() {
        return new Periode(null, null);
    }

    public static Periode dari(Date dari, Date sampai) {
        return new Periode(dari, sampai);
    }

    // Parse teks tanggal. Melempar IllegalArgumentException (pesan siap tampil)
    // bila format salah atau tanggal awal setelah tanggal akhir.
    public static Periode parse(String teksDari, String teksSampai) {
        Date d1 = null;
        Date d2 = null;

        try {
            if (teksDari != null && !teksDari.trim().isEmpty()) {
                d1 = Date.valueOf(teksDari.trim());
            }
            if (teksSampai != null && !teksSampai.trim().isEmpty()) {
                d2 = Date.valueOf(teksSampai.trim());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Rentang tanggal tidak valid (format yyyy-MM-dd)");
        }

        if (d1 != null && d2 != null && d1.after(d2)) {
            throw new IllegalArgumentException("Rentang tanggal tidak valid");
        }
        return new Periode(d1, d2);
    }

    // null = tanpa batas
    public Date getDari() {
        return dari;
    }

    public Date getSampai() {
        return sampai;
    }

    public Date batasAwal() {
        return dari == null ? TGL_MIN : dari;
    }

    public Date batasAkhir() {
        return sampai == null ? TGL_MAX : sampai;
    }

    public boolean memuat(Date tanggal) {
        return tanggal != null
                && !tanggal.before(batasAwal())
                && !tanggal.after(batasAkhir());
    }

    public String teksDari() {
        return dari == null ? "" : dari.toString();
    }

    public String teksSampai() {
        return sampai == null ? "" : sampai.toString();
    }

    public String label() {
        if (dari == null && sampai == null) {
            return "Semua periode";
        }
        return (dari == null ? "..." : dari.toString()) + " s/d "
                + (sampai == null ? "..." : sampai.toString());
    }
}
