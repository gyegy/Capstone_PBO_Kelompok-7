package view.table;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 * Model tabel untuk daftar yang harus dipilih pengguna: daftar laporan yang
 * menunggu penugasan, daftar petugas aktif, dan daftar fasilitas pada form
 * laporan. Kolom pertama selalu kolom pilih (tombol radio atau kotak
 * centang), sedangkan kolom lainnya berisi teks apa adanya.
 *
 * Nilai tiap baris disimpan sebagai objek utuh sehingga layar dapat mengambil
 * kembali data aslinya lewat {@link #get(int)}.
 */
public class PilihanTableModel extends AbstractTableModel {

    private final String[] kolom;
    private final List<Object> data = new ArrayList<>();
    private final List<Object[]> baris = new ArrayList<>();
    private final Set<Integer> dipilih = new LinkedHashSet<>();
    private final boolean banyak;

    /**
     * @param banyak {@code true} untuk kotak centang (boleh banyak baris),
     *               {@code false} untuk tombol radio (hanya satu baris)
     * @param kolom nama kolom teks, tanpa kolom pilih
     */
    public PilihanTableModel(boolean banyak, String... kolom) {
        this.banyak = banyak;
        this.kolom = kolom;
    }

    /** Mengisi tabel; setiap elemen data menjadi satu baris. */
    public void setData(List<? extends Object> asal, List<Object[]> isi) {
        data.clear();
        baris.clear();
        dipilih.clear();
        if (asal != null) {
            data.addAll(asal);
        }
        if (isi != null) {
            baris.addAll(isi);
        }
        fireTableDataChanged();
    }

    /**
     * Mengganti isi teks tiap baris tanpa menyentuh pilihan yang sedang
     * aktif. Dipakai layar penugasan saat kolom Peran harus diperbarui
     * setelah petugas dipilih.
     */
    public void setIsi(List<Object[]> isi) {
        baris.clear();
        if (isi != null) {
            baris.addAll(isi);
        }
        dipilih.removeIf(indeks -> indeks >= baris.size());
        fireTableDataChanged();
    }

    /** Menetapkan pilihan secara langsung, menggantikan pilihan lama. */
    public void setPilihTetap(List<Integer> indeks) {
        dipilih.clear();
        if (indeks != null) {
            for (Integer i : indeks) {
                if (i != null && i >= 0 && i < baris.size()) {
                    dipilih.add(i);
                }
            }
        }
        fireTableDataChanged();
    }

    /** Mengosongkan tabel beserta semua pilihan. */
    public void kosongkan() {
        setData(null, null);
    }

    public Object get(int indeks) {
        return indeks < data.size() ? data.get(indeks) : null;
    }

    public boolean isBanyak() {
        return banyak;
    }

    public boolean dipilih(int indeks) {
        return dipilih.contains(indeks);
    }

    /** Satu-satunya indeks terpilih; -1 bila tidak ada. */
    public int indeksTerpilih() {
        return dipilih.isEmpty() ? -1 : dipilih.iterator().next();
    }

    /** Seluruh indeks terpilih, urut sesuai urutan pemilihan. */
    public List<Integer> indeksTerpilihSemua() {
        return new ArrayList<>(dipilih);
    }

    /** Objek pada satu-satunya baris terpilih, atau {@code null}. */
    public Object dataTerpilih() {
        int indeks = indeksTerpilih();
        return indeks < 0 ? null : get(indeks);
    }

    /**
     * Memilih satu baris. Pada mode banyak, baris yang sudah terpilih tetap
     * terpilih; pada mode tunggal, pilihan lama dilepas.
     */
    public void pilih(int indeks) {
        if (indeks < 0 || indeks >= data.size()) {
            return;
        }
        if (!banyak) {
            dipilih.clear();
            dipilih.add(indeks);
        } else if (!dipilih.remove(indeks)) {
            dipilih.add(indeks);
        }
        fireTableRowsUpdated(0, Math.max(0, data.size() - 1));
    }

    public void pilihSemua(boolean pilih) {
        dipilih.clear();
        if (pilih) {
            for (int i = 0; i < data.size(); i++) {
                dipilih.add(i);
            }
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return baris.size();
    }

    @Override
    public int getColumnCount() {
        return kolom.length + 1;
    }

    @Override
    public String getColumnName(int kolomKe) {
        if (kolomKe == 0) {
            return "";
        }
        return kolom[kolomKe - 1];
    }

    @Override
    public Object getValueAt(int indeks, int kolomKe) {
        if (kolomKe == 0) {
            return dipilih(indeks) ? "1" : "";
        }
        Object[] isi = baris.get(indeks);
        int posisi = kolomKe - 1;
        return posisi < isi.length ? isi[posisi] : "";
    }

    @Override
    public boolean isCellEditable(int indeks, int kolomKe) {
        return false;
    }
}
