package view.util;

/**
 * Kontrak untuk setiap halaman yang ditampilkan di dalam kerangka utama.
 * Dipakai oleh MainFrame untuk memuat data dan menyesuaikan lebar komponen
 * ketika jendela diubah ukurannya.
 */
public interface HalamanAeroCare {

    /** Nama halaman untuk jejak navigasi, mis. "Dashboard". */
    String judul();

    /** Dipanggil setiap kali halaman ditampilkan agar data selalu segar. */
    void muatData();

    /**
     * Menyesuaikan lebar komponen yang lentur ketika area konten berubah ukuran.
     *
     * @param lebar lebar area konten dalam piksel
     * @param tinggi tinggi area konten dalam piksel
     */
    void sesuaikanUkuran(int lebar, int tinggi);
}
