package view.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * Palet warna, tipografi, dan ukuran yang dipakai seluruh form AEROCARE.
 * Nilai diambil langsung dari rancangan Figma.
 */
public final class Theme {

    // ---------- Warna utama ----------
    public static final Color SIDEBAR      = new Color(0x0F172A);
    public static final Color SIDEBAR_HOVER = new Color(0x1E293B);
    public static final Color SURFACE      = new Color(0xFFFFFF);
    public static final Color CANVAS       = new Color(0xF1F5F9);
    public static final Color BORDER       = new Color(0xE2E8F0);
    public static final Color BORDER_SOFT  = new Color(0xF1F5F9);

    public static final Color TEXT         = new Color(0x0F172A);
    public static final Color TEXT_MUTED   = new Color(0x64748B);
    public static final Color TEXT_FAINT   = new Color(0x94A3B8);
    public static final Color TEXT_ON_DARK = new Color(0xE2E8F0);
    public static final Color TEXT_DIM     = new Color(0x94A3B8);

    // ---------- Warna per peran ----------
    public static final Color ADMIN        = new Color(0x2563EB);
    public static final Color ADMIN_DARK   = new Color(0x1D4ED8);
    public static final Color ADMIN_SOFT   = new Color(0xDBEAFE);
    public static final Color SUPERVISOR   = new Color(0x7C3AED);
    public static final Color SUPERVISOR_DARK = new Color(0x6D28D9);
    public static final Color SUPERVISOR_SOFT = new Color(0xEDE9FE);
    public static final Color PETUGAS      = new Color(0xEA580C);
    public static final Color PETUGAS_DARK = new Color(0xC2410C);
    public static final Color PETUGAS_SOFT = new Color(0xFFEDD5);

    // ---------- Warna status ----------
    public static final Color GREEN        = new Color(0x15803D);
    public static final Color GREEN_SOFT   = new Color(0xDCFCE7);
    public static final Color AMBER        = new Color(0xB45309);
    public static final Color AMBER_SOFT   = new Color(0xFEF3C7);
    public static final Color RED          = new Color(0xB91C1C);
    public static final Color RED_SOFT     = new Color(0xFEE2E2);
    public static final Color BLUE         = new Color(0x1D4ED8);
    public static final Color BLUE_SOFT    = new Color(0xDBEAFE);
    public static final Color VIOLET       = new Color(0x6D28D9);
    public static final Color VIOLET_SOFT  = new Color(0xEDE9FE);
    public static final Color SLATE_SOFT   = new Color(0xF1F5F9);
    public static final Color HEADER_TABEL = new Color(0xF8FAFC);

    // ---------- Tipografi ----------
    public static final String FAMILY = pilihKeluargaFont();

    private Theme() {
    }

    private static String pilihKeluargaFont() {
        // Segoe UI tersedia di Windows (dipakai desainer Figma),
        // Inter/SansSerif menjadi cadangan di sistem lain.
        String[] kandidat = {"Segoe UI", "Inter", "Helvetica Neue", "Sans Serif"};
        String[] tersedia = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        for (String kandidatNama : kandidat) {
            for (String nama : tersedia) {
                if (nama.equalsIgnoreCase(kandidatNama)) {
                    return nama;
                }
            }
        }
        return Font.SANS_SERIF;
    }

    public static Font font(int gaya, float ukuran) {
        return new Font(FAMILY, gaya, Math.round(ukuran));
    }

    public static Font regular(float ukuran) {
        return font(Font.PLAIN, ukuran);
    }

    public static Font medium(float ukuran) {
        return font(Font.PLAIN, ukuran);
    }

    public static Font bold(float ukuran) {
        return font(Font.BOLD, ukuran);
    }

    /** Warna aksen sesuai peran pengguna. */
    public static Color aksen(String peran) {
        if ("Supervisor".equalsIgnoreCase(peran)) {
            return SUPERVISOR;
        }
        if ("Petugas".equalsIgnoreCase(peran)) {
            return PETUGAS;
        }
        return ADMIN;
    }

    public static Color aksenGelap(String peran) {
        if ("Supervisor".equalsIgnoreCase(peran)) {
            return SUPERVISOR_DARK;
        }
        if ("Petugas".equalsIgnoreCase(peran)) {
            return PETUGAS_DARK;
        }
        return ADMIN_DARK;
    }

    /** Latar lembut untuk lencana status. */
    public static Color latarStatus(String status) {
        if (status == null) {
            return SLATE_SOFT;
        }
        switch (status) {
            case "Selesai":
            case "Baik":
            case "Aktif":
            case "Rendah":
                return GREEN_SOFT;
            case "Menunggu":
            case "Perlu Perbaikan":
            case "Sedang":
            case "Pemeriksaan":
            case "Perbaikan":
                return AMBER_SOFT;
            case "Rusak":
            case "Tinggi":
                return RED_SOFT;
            case "Diproses":
            case "Pengujian":
                return BLUE_SOFT;
            case "Tidak Aktif":
            case "Nonaktif":
                return SLATE_SOFT;
            default:
                return SLATE_SOFT;
        }
    }

    /** Warna teks untuk lencana status. */
    public static Color teksStatus(String status) {
        if (status == null) {
            return TEXT_MUTED;
        }
        switch (status) {
            case "Selesai":
            case "Baik":
            case "Aktif":
            case "Rendah":
                return GREEN;
            case "Menunggu":
            case "Perlu Perbaikan":
            case "Sedang":
            case "Pemeriksaan":
            case "Perbaikan":
                return AMBER;
            case "Rusak":
            case "Tinggi":
                return RED;
            case "Diproses":
            case "Pengujian":
                return BLUE;
            case "Tidak Aktif":
            case "Nonaktif":
                return TEXT_MUTED;
            default:
                return TEXT_MUTED;
        }
    }
}
