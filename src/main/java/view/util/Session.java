package view.util;

import model.Pengguna;

/**
 * Menyimpan pengguna yang sedang masuk agar seluruh form dapat membaca
 * peran, nama, dan ID tanpa saling mengoper parameter.
 */
public final class Session {

    private static Pengguna pengguna;

    private Session() {
    }

    public static void masuk(Pengguna p) {
        pengguna = p;
    }

    public static Pengguna pengguna() {
        return pengguna;
    }

    public static boolean adaSesi() {
        return pengguna != null;
    }

    public static String peran() {
        return pengguna == null ? "" : UiKit.atau(pengguna.getPeran(), "");
    }

    public static String nama() {
        return pengguna == null ? "" : UiKit.atau(pengguna.getNamaLengkap(), "");
    }

    public static int id() {
        return pengguna == null ? 0 : pengguna.getIdPengguna();
    }

    public static boolean admin() {
        return "Admin".equalsIgnoreCase(peran());
    }

    public static boolean supervisor() {
        return "Supervisor".equalsIgnoreCase(peran());
    }

    public static boolean petugas() {
        return "Petugas".equalsIgnoreCase(peran());
    }

    public static void keluar() {
        pengguna = null;
    }
}
