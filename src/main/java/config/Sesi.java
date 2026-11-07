package config;

public final class Sesi {

    private static String idPengguna;
    private static String namaLengkap;
    private static String peran;

    private Sesi() {
        // Mencegah pembuatan objek Sesi
    }

    public static void simpan(
            String id,
            String nama,
            String peranLogin
    ) {
        idPengguna = id;
        namaLengkap = nama;
        peran = peranLogin;
    }

    public static String getIdPengguna() {
        return idPengguna;
    }

    public static String getNamaLengkap() {
        return namaLengkap;
    }

    public static String getPeran() {
        return peran;
    }

    public static void hapus() {
        idPengguna = null;
        namaLengkap = null;
        peran = null;
    }
}
