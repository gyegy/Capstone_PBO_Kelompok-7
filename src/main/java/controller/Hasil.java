package controller;

/**
 * Hasil sebuah operasi di controller. View cukup menampilkan pesan
 * sesuai jenisnya (informasi / peringatan validasi / kesalahan sistem).
 */
public final class Hasil {

    public enum Jenis { BERHASIL, PERINGATAN, KESALAHAN }

    private final Jenis jenis;
    private final String pesan;

    private Hasil(Jenis jenis, String pesan) {
        this.jenis = jenis;
        this.pesan = pesan;
    }

    public static Hasil berhasil(String pesan) {
        return new Hasil(Jenis.BERHASIL, pesan);
    }

    // Data yang diisi pengguna belum valid
    public static Hasil peringatan(String pesan) {
        return new Hasil(Jenis.PERINGATAN, pesan);
    }

    // Proses gagal di sisi sistem / database
    public static Hasil kesalahan(String pesan) {
        return new Hasil(Jenis.KESALAHAN, pesan);
    }

    public boolean isBerhasil() {
        return jenis == Jenis.BERHASIL;
    }

    public boolean isPeringatan() {
        return jenis == Jenis.PERINGATAN;
    }

    public Jenis getJenis() {
        return jenis;
    }

    public String getPesan() {
        return pesan;
    }
}
